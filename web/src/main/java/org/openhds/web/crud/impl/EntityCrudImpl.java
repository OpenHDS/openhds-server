package org.openhds.web.crud.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhds.domain.constraint.Searchable;
import org.openhds.dao.service.Dao;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.web.crud.EntityCrud;
import org.openhds.web.cvt.EntityConverter;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.web.service.JsfService;
import org.openhds.web.service.WebFlowService;
import org.openhds.web.ui.NavigationMenuBean;
import org.openhds.web.ui.PagingState;
import org.springframework.binding.message.MessageContext;

/**
 * Generic implementation of the EntityCrud interface
 * This class is currently being used by all entities in the system
 * It is being wired up in the applicationContext.xml file
 *
 * @param <T>
 * @param <PK>
 */

public class EntityCrudImpl<T, PK extends Serializable> implements EntityCrud<T, PK> {

    static Log log = LogFactory.getLog(EntityCrudImpl.class);
    
    // This is currently being used to create a new
    // instance of the entity type
    protected Class<T> entityClass;
    
    String outcomePrefix;
    
    // The current entity (or "backing bean") being worked on
    // wired from the applicationContext.xml file
    protected T entityItem;
   
    PagingState pager;
    PagingState filteredPager;
    protected Dao<T, PK> dao;
    GenericDao genericDao;
    SitePropertiesService properties;
    
	List<T> pagedItems;
    List<T> entitiesByProperty;
    List<T> entitiesByExample;
    List<T> allEntities;
    HashMap<T, Class<?>> searchableFieldsMap = new HashMap<T, Class<?>>();
    NavigationMenuBean navMenuBean;
    
	String propertyName;
    String searchString;
    List<SelectItem> searchableFieldsList;
    Boolean isSearch = false;

	// used to convert an entity from a string to object, or
    // object to a string
    protected EntityConverter<T> converter;
   
    // helper service
    public JsfService jsfService;
    public WebFlowService webFlowService;
   
	protected EntityFilter<T> entityFilter;
	
	protected boolean showListing = true;
	
	interface EntityFilter<T> {
		List<T> getFilteredEntityList(T entityItem);
	}

	/**
	 * Service that provides the business logic for creating, editing and deleting entities
	 */
	protected EntityService entityService;
	
	@SuppressWarnings("unchecked")
	protected EntityValidationService entityValidationService;

	private String navigateTo;
	
    public EntityCrudImpl(Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException(
                    "entity class type required for crud");
        }
        if (log.isDebugEnabled()) {
            log.debug("creating crud for class: " + entityClass.getName());
        }
        this.entityClass = entityClass;
        this.outcomePrefix = entityClass.getSimpleName().toLowerCase();
        pager = new PagingState();
        filteredPager = new PagingState();

    }

	public void setJsfService(JsfService jsfService) {
        this.jsfService = jsfService;
    }
	
	public WebFlowService getWebFlowService() {
		return webFlowService;
	}

	public void setWebFlowService(WebFlowService webFlowService) {
		this.webFlowService = webFlowService;
	}

    public void setDao(Dao<T, PK> dao) {
        this.dao = dao;
    }

    public Dao<T, PK> getDao() {
        return dao;
    }
    
    public PagingState getPager() {
        if (pager.getTotalCount() < 0) {
        	if (isSearch) {
            	pager.setTotalCount(processSearchCriteria());
        	} else {
        		pager.setTotalCount(dao.getTotalCount());
        	}
        }
        return pager;
    }
    
    public PagingState getFilteredPager() {
        if (filteredPager.getTotalCount() < 0) {
        	if (isSearch) {
        		filteredPager.setTotalCount(processSearchCriteria());
        	} else {
        		filteredPager.setTotalCount(getFilteredPagedItems().size());
        	}
        }
        return filteredPager;
    }

    /**
     * Create a new instance of the entity type
     * @return the new object instance
     */
    protected T newInstance() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new FacesException("failed to instantiate entity "
                    + entityClass.getName(), e);
        }
    }

    /**
     *
     * @param resetPaging
     */
    protected void reset(boolean resetPaging, boolean resetEntityitem) {
    	if (resetEntityitem) {
    		entityItem = null;
    	}
        pager.setTotalCount(-1);
        pagedItems = null;
        if (resetPaging) {
            pager.setPageIndex(0);
        }
    }

    public String listSetup() {
        reset(true, true);
        return outcomePrefix + "_list";
    }

    public void validateCreate(FacesContext facesContext,
            UIComponent component, Object value) {
        T newItem = null;
        try {
            newItem = entityClass.newInstance();
        } catch (Exception e) {
            log.error("failed to create item instance", e);
        }
        String newItemString = converter.getAsString(FacesContext.getCurrentInstance(), null, newItem);
        String itemString = converter.getAsString(FacesContext.getCurrentInstance(), null, entityItem);
        if (!newItemString.equals(itemString)) {
            createSetup();
        }
    }

    public String createSetup() {
        reset(false, true);
        showListing = false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }

    /**
     * Persist the current entity item to the database
     */
    public String create() {
    	try {
			entityService.create(entityItem);
		} catch (IllegalArgumentException e) {
			jsfService.addError(e.getMessage());
			return null;
		} catch (ConstraintViolations e) {
			for(String msg : e.getViolations()) {
				jsfService.addError(msg);
			}
			return null;
		} catch (SQLException e) {
			jsfService.addError("Error creating record in database");
			return null;
		} catch(AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		} catch(Exception e) {
			jsfService.addError(e.getMessage());
			return null;
		}

		showListing = true;
        return listSetup();
    }

    public String detailSetup() {
    	showListing = false;
        navMenuBean.setNextItem(entityClass.getSimpleName());
    	navMenuBean.addCrumb(entityClass.getSimpleName() + " Detail");
        return scalarSetup(outcomePrefix + "_detail");
    }

    public String editSetup() {
    	showListing = false;
        navMenuBean.setNextItem(entityClass.getSimpleName());
    	navMenuBean.addCrumb(entityClass.getSimpleName() + " Edit");
    	return scalarSetup(outcomePrefix + "_edit");
    }

    /**
     * Update a persisted entity
     */
    public String edit() {
    	// verify that the entity being edited is valid
    	// there is a case when the user could open the edit form
    	// by manually navigating to the URL
    	String itemString = converter.getAsString(FacesContext.getCurrentInstance(), null, entityItem);
        String itemId = jsfService.getReqParam("itemId");
        
        if (itemString == null || itemString.length() == 0
                || !itemString.equals(itemId)) {
            String outcome = editSetup();
            if ((outcomePrefix + "_edit").equals(outcome)) {
                jsfService.addError("Could not edit item. Try again.");
            }
            return outcome;
        }

        // attempt to update
        try {
			entityService.save(entityItem);
        } catch(ConstraintViolations e) {
			for(String msg : e.getViolations()) {
				jsfService.addError(msg);
			}
			return null;
		} catch (SQLException e) {
			jsfService.addError("Error updating the current entity");
			return null;
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		} catch (Exception e) {
			jsfService.addError("Error updating entity");
			return null;
		}

		showListing = true;
        return detailSetup();
    }

    /**
     * Remove/delete an entity from persistence
     */
     public String delete() {

        T persistentObject = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
        
        try {
			entityService.delete(persistentObject);
		} catch (SQLException e) {
			jsfService.addError("Could not delete the persistent entity");
			return null;
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		}

		showListing = true;
        return listSetup();
    }

    @SuppressWarnings("unchecked")
    protected String scalarSetup(String outcome) {
        reset(false, true);
        entityItem = (T) jsfService.getObjViaReqParam("itemId", converter, null);
        if (entityItem == null) {
            String itemId = jsfService.getReqParam("itemId");
            jsfService.addError("The item with id " + itemId
                    + " no longer exists.");
        }
        return outcome;
    }

    public String next() {
        reset(false, false);
        getPager().nextPage();
        return outcomePrefix + "_list";
    }

    public String prev() {
        reset(false, false);
        getPager().previousPage();
        return outcomePrefix + "_list";
    }

    public String lastPage() {
    	reset(false, false);
    	// set pager to the total number of records
    	// it will automatically adjust and show the last page
    	getPager().setPageIndex((int)getPager().getTotalCount());
    	return outcomePrefix + "_list";
    }
    
    public String firstPage() {
    	reset(false, false);
    	getPager().setPageIndex(0);
    	return outcomePrefix + "_list";
    }
    
    public T getItem() {
        if (entityItem == null) {
            entityItem = newInstance();
        }
        return entityItem;
    }

    public List<T> getPagedItems() {

    	// normal paging of data records
        if (pagedItems == null)  
        	pagedItems = dao.findPaged(pager.getPageIncrement(), pager.getPageIndex());           
        
        // user has performed a search, so only grab a subset of those records
        else if (isSearch) 
        	pagedItems = generateSearchResults();

    	return pagedItems;
    }

    public String search() {
    	
    	// clear out any entity the user might be editing and reset the pager
    	reset(true, false);
    	
    	// make sure user entered a valid string to search
    	if(searchString != null && searchString.trim().length() != 0){
        	isSearch = true;
    	} else {
         	isSearch = false;
         	return outcomePrefix + "_list";
    	}
    	getPager().setTotalCount(processSearchCriteria());
    	    	
        return outcomePrefix + "_list";
    }
    
    private long processSearchCriteria() {
    	
    	if (searchableFieldsMap.get(propertyName).getName() != "java.lang.String") 
    		 return searchForEntitiesById(propertyName, searchableFieldsMap.get(propertyName), searchString, entityClass).size();
    	else 
	    	return dao.getCountByProperty(propertyName, searchString);
    }
    
    private List<T> generateSearchResults() {
    	
    	if (searchableFieldsMap.get(propertyName).getName() != "java.lang.String") 
    		 return searchForEntitiesById(propertyName, searchableFieldsMap.get(propertyName), searchString, entityClass);

	    return dao.findListByPropertyPagedInsensitive(propertyName, searchString, pager.getPageIndex(), pager.getPageIncrement());
    }
    
	public String clearSearch() {
		isSearch = false;
		searchString = null;
		propertyName = null;
		reset(true, false);
		return outcomePrefix + "_list";
	}
	
	@SuppressWarnings("unchecked")
	public List<T> searchForEntitiesById(String searchPropertyName, Class<?> propertyName, String propertyValue, Class<T> entityClass) {
		T item = (T) genericDao.findByProperty(propertyName, "extId", propertyValue);
		List<T> list = genericDao.findListByProperty(entityClass, searchPropertyName, item);
		return list;
	}
    
	public List<T> getEntitiesByProperty() {
		return entitiesByProperty;
	}

	public void setEntitiesByProperty(List<T> entitiesByProperty) {
		this.entitiesByProperty = entitiesByProperty;
		
	}

	public List<T> getEntitiesByExample() {
		return entitiesByExample;
	}

	public void setEntitiesByExample(List<T> entitiesByExample) {
		this.entitiesByExample = entitiesByExample;
	}    
    
    public List<T> getEntitiesByProperty(String propertyName, Object value) {
        entitiesByProperty = dao.findListByProperty(propertyName, value, true);
        return entitiesByProperty;
    }

    public List<T> getEntitiesByExample(T exampleInstance,String... excludeProperty) {
        entitiesByExample = dao.findByExample(exampleInstance,excludeProperty);
        return entitiesByExample;
    }
        
    public SelectItem[] getSelectItems() {
        return jsfService.getSelectItems(dao.findAll(true));
    }
    
    /*
     * Return SelectItem for a subset of enitities depending on the value of a
     * property being searched on
     */
    public SelectItem[] getSelectItemsByProperty(String propertyName, Object value) {
        return jsfService.getSelectItems(dao.findListByProperty(propertyName, value, true));
    }

    public void setConverter(EntityConverter<T> converter) {
        this.converter = converter;
    }

    public EntityConverter<T> getConverter() {
        return converter;
    }

    public void setItem(T entityItem) {
        this.entityItem = entityItem;
    }

    public void performAudit(T entityItem) {
    }
    
	/**
	 * Save and commit the entity
	 * @param messageContext used to relay messages back to the facelet
	 * @return true if commit succeeded, false otherwise
	 */
    public boolean commit(MessageContext messageContext) {
		// wrapped in a try catch in case commit fails
		try {
			entityService.create(entityItem);
		} catch(ConstraintViolations e) {
			// these exceptions are thrown by the service layer
			for(String msg : e.getViolations()) {
				webFlowService.createMessage(messageContext, msg);
			}
			return false;
		} catch(SQLException e) {
			// hibernate commit fails
			webFlowService.createMessage(messageContext, "There was an error committing the transaction. Please try again");
			return false;			
		} catch(Exception e) {
			webFlowService.createMessage(messageContext, "An unexpected error has occurred");
			return false;
		}
		
		return true;
	}
	
	public boolean save(MessageContext messageContext) {
			try {
				entityService.save(entityItem);
			} catch(ConstraintViolations e) {
				// these exceptions are thrown by the service layer
				for(String msg : e.getViolations()) {
					webFlowService.createMessage(messageContext, msg);
				}
				return false;
			} catch (SQLException e) {
				webFlowService.createMessage(messageContext, "There was an error saving the transaction. Please try again");
				return false;
			} catch (Exception e) {
				webFlowService.createMessage(messageContext, "An unexpected error has occurred");
				return false;
			}
			
			return true;
	}
	
	@SuppressWarnings("unchecked")
	public <S> boolean validateEntity(S entity, MessageContext messageContext) {
		try {
			entityValidationService.validateEntity(entity);
		} catch (ConstraintViolations e) {
			for(String msg : e.getViolations()) {
				webFlowService.createMessage(messageContext, msg);
			}
			return false;
		}
		
		return true;
	}

	
	/**
	 * Initialize the crud and backing bean
	 * This should be called when a flow is started
	 * @return
	 */
	public boolean initFlow() {
		this.createSetup();	
		return true;
	}

	/**
	 * Configure the backing bean to be viewed by the user
	 * @param id The id of the pregnancy outcome to retrieve from the db
	 * @return
	 */
	@SuppressWarnings("unchecked")
 	public boolean editByUuid(String id, MessageContext messageContext) {
		try {
			entityItem = (T) entityService.read(entityItem.getClass(), id);
			showListing = false;
		} catch (AuthorizationException e) {
			webFlowService.createMessage(messageContext, e.getMessage());
			return false;
		}
		
		return true;
	}

	public boolean setNavigateTo(String navigateTo) {
		this.navigateTo = navigateTo;
		
		return true;
	}

	public String getNavigateTo() {
		// need to check if user clicked on a regular jsf amendment form
		// or a web flow
		if (navigateTo.indexOf("flow") > 0) {
			// user clicked a flow link - just need to replace the _ with a .
			return navigateTo.replace('_', '.');
		} else {
			// user clicked a regular amendment form link
			return navigateTo.replace('_', '/') + ".faces";
		}
	}
	
	public List<T> getFilteredPagedItems() {
		if (entityFilter == null) {
			// filter not setup so can't filter entities
			// fall back to normal pagedItems
			return getPagedItems();
		}
		
		return entityFilter.getFilteredEntityList(entityItem);
	}
	
	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getSearchableFieldsList() {
		searchableFieldsList = new ArrayList<SelectItem>();
		Field[] f = entityClass.getDeclaredFields();

		for (Field ff : f) {	
				if (ff.isAnnotationPresent(Searchable.class)) {
					searchableFieldsMap.put((T) ff.getName(), ff.getType());
					SelectItem it = new SelectItem(ff.getName());
					searchableFieldsList.add(it);
				}
		}    	
		return searchableFieldsList;
	}
	
	public void setSearchableFieldsList(List<SelectItem> searchableFieldsList) {
		this.searchableFieldsList = searchableFieldsList;
	}

	public List<T> getAllEntities() {
		allEntities = dao.findAll(true);
		return allEntities;
	}

	public void setAllEntities(List<T> allEntities) {
		this.allEntities = allEntities;
	}
	
	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public boolean isShowListing() {
		return showListing;
	}

	public void setShowListing(boolean showListing) {
		this.showListing = showListing;
	}
	
	public NavigationMenuBean getNavMenuBean() {
		return navMenuBean;
	}

	public void setNavMenuBean(NavigationMenuBean navMenuBean) {
		this.navMenuBean = navMenuBean;
	}
	
	public SitePropertiesService getProperties() {
		return properties;
	}

	public void setProperties(SitePropertiesService properties) {
		this.properties = properties;
	}
	
	@SuppressWarnings("unchecked")
	public EntityValidationService getEntityValidationService() {
		return entityValidationService;
	}

	@SuppressWarnings("unchecked")
	public void setEntityValidationService(EntityValidationService entityValidationService) {
		this.entityValidationService = entityValidationService;
	}
}
