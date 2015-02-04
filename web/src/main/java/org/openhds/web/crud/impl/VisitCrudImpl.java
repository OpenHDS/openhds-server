package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.EntityType;
import org.openhds.domain.model.Extension;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.ExtensionService;
import org.openhds.controller.service.VisitService;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.binding.message.MessageContext;

public class VisitCrudImpl extends EntityCrudImpl<Visit, String> {

	VisitService service;
    ExtensionService extensionService;
    SitePropertiesService siteProperties;
	
	// used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date visitDate;
    
    // paramater used for determining if extensions are to be initialized or not.
    boolean extensionsInitialized;
	
	public VisitCrudImpl(Class<Visit> entityClass) {
        super(entityClass);
    }
	
	/**
	 * Overridden method for setting extensionsInitialized paramater.
	 */
    public String createSetup() {
    	extensionsInitialized = false;
        reset(false, true);
        if(isFlow){
        showListing = false;
        }else{
        	showListing = true;
        }
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }
	
    /**
     * Given the round number, we can initialize the extensions for a Visit
     */
    public void initializeExtensions() {
    	service.initializeExtensions(entityItem);
    	extensionsInitialized = true;
    }
    
    /**
     * Clears all extensions for the entityItem
     */
    public void clearExtensions() {
    	entityItem.getExtensions().clear();
    	extensionsInitialized = false;
    }
    
    /**
     * Clears all extensions for Individual
     */
    public void clearIndividualExtensions() {
    	clearExtensionsByType(EntityType.INDIVIDUAL);
    }
    
    /**
     * Clears all extensions for SocialGroup
     */
    public void clearSocialGroupExtensions() {
    	clearExtensionsByType(EntityType.SOCIALGROUP);
    }
    
    /**
     * Given the entity type, clear extensions for the current entity item.
     * Note: at this point, entity item is a transient object.
     */
    private void clearExtensionsByType(EntityType entityType) {
    	List<Extension> list = new ArrayList<Extension>();
    	int size = service.getExtensionsByEntityClassAndRoundNumber(entityType, entityItem.getRoundNumber()).size();
    	
    	for (Extension e : entityItem.getExtensions()) {
    		if (e.getClassExtension().getEntityClass().equals(entityType) &&
    			e.getClassExtension().getRoundNumber().equals(entityItem.getRoundNumber()))
    			list.add(e);
    	}	
    	
    	int count = 0;
    	for (Extension e : list) {	
    		if (count >= size)
    			entityItem.getExtensions().remove(e);
    		count++;
    	}
    	extensionsInitialized = false;
    }
        
    /**
     * Grab all extensions for the visit
     */
    public List<Extension> getVisitExtensions() {
    	List<Extension> list = null;
    	try {
    		list = getAllVisitExtensions();
    	} catch (ConstraintViolations e) {
            jsfService.addError(e.getMessage());
    	}
    	return list;
    }
        
    /**
     * Grab all extensions of Location for the visit
     */
    public List<Extension> getLocationExtensions() {	
    	return getAllLocationExtensions();
    }
    
    /**
     * Grab all extensions of SocialGroup for the visit
     */
    public List<Extension> getSocialGroupExtensions() {	
    	return getExtensionsByType(EntityType.SOCIALGROUP);
    }
    
    /**
     * Grab all extensions of Individual for the visit
     */
    public List<Extension> getIndividualExtensions() {	
    	return getExtensionsByType(EntityType.INDIVIDUAL);
    }
    
    /**
     * Returns only extensions of type Visit and sets the entityExtId automatically.
     * Note: at this point the entity item is a transient object.
     */
    public List<Extension> getAllVisitExtensions() throws ConstraintViolations {
    	List<Extension> list = new ArrayList<Extension>();
    	for (Extension e : entityItem.getExtensions()) {		
    		if (e.getClassExtension().getEntityClass().equals(EntityType.VISIT)) {
    			e.setEntityExtId(service.generateId(entityItem).getExtId());
    			list.add(e);
    		}
    	}
    	return list;
    }
    
    /**
     * Returns only extensions of type Location and sets the entityExtId automatically.
     * Note: at this point the entity item is a transient object.
     */
    public List<Extension> getAllLocationExtensions() {
    	List<Extension> list = new ArrayList<Extension>();
    	for (Extension e : entityItem.getExtensions()) {		
    		if (e.getClassExtension().getEntityClass().equals(EntityType.LOCATION)) {
    			e.setEntityExtId(entityItem.getVisitLocation().getExtId());
    			list.add(e);
    		}
    	}
    	return list;
    }
    
    /**
     * Returns only extensions of the type specified.
     * Note: at this point the entity item is a transient object.
     */
    public List<Extension> getExtensionsByType(EntityType entityType) {
    	List<Extension> list = new ArrayList<Extension>();
    	if (entityItem != null) {
	    	for (Extension e : entityItem.getExtensions()) {		
	    		if (e.getClassExtension().getEntityClass().equals(entityType)) 
	    			list.add(e);		
	    	}
    	}
    	return list;
    }
    
    // the entityitem, the pojo, can have its fields set before being created by super create
    // Note that super create calls dao.create(entityItem);
    
    @Override
    public String create() {

        try {
            service.createVisit(entityItem);
            return onCreateComplete();
        } catch (ConstraintViolations e) {
            jsfService.addError(e.getMessage());
        } catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
        return null;
    }
    
    @Override
    public String edit() {
    	
    	Visit persistedItem = (Visit)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));

        try {
        	service.checkVisit(persistedItem, entityItem);
            extensionService.evaluateExtensions(entityItem);
        	super.edit();
        	
        	return "pretty:visitEdit";
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
		} catch(Exception e) {
        	jsfService.addError(e.getMessage());
		}
        return null;
    }
    
    @Override
    public boolean commit(MessageContext messageContext) {
        try {
            service.createVisit(entityItem);
            return true;
        } catch (ConstraintViolations e) {
            webFlowService.createMessage(messageContext, e.getMessage());
        } 

        return false;
    }
     
    public Date getVisitDate() {
    	
    	if (entityItem.getVisitDate() == null)
    		return new Date();
    	
    	return entityItem.getVisitDate().getTime();
	}

	public void setVisitDate(Date visitDate) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		entityItem.setVisitDate(cal);
	}
	
	public void addIndividualExtension() {
		service.addExtensions(entityItem, EntityType.INDIVIDUAL);
	}
	
	public void addSocialGroupExtension() {
		service.addExtensions(entityItem, EntityType.SOCIALGROUP);
	}
	     
    public ExtensionService getExtensionService() {
		return extensionService;
	}

	public void setExtensionService(ExtensionService extensionService) {
		this.extensionService = extensionService;
	}

	public VisitService getService() {
		return service;
	}

	public void setService(VisitService service) {
		this.service = service;
	}
	
	public boolean isExtensionsInitialized() {
		return extensionsInitialized;
	}

	public void setExtensionsInitialized(boolean extensionsInitialized) {
		this.extensionsInitialized = extensionsInitialized;
	}
	
	public SitePropertiesService getSiteProperties() {
		return siteProperties;
	}

	public void setSiteProperties(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
	}
}
