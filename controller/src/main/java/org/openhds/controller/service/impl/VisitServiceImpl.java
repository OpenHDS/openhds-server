package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.idgeneration.VisitGenerator;
import org.openhds.controller.service.VisitService;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.EntityType;
import org.openhds.domain.model.Extension;
import org.openhds.domain.model.Round;
import org.openhds.domain.model.Visit;

@SuppressWarnings("unchecked")
public class VisitServiceImpl implements VisitService {

	private GenericDao genericDao;
	private VisitGenerator generator;
	
	public VisitServiceImpl(GenericDao genericDao, VisitGenerator generator) {
		this.genericDao = genericDao;
		this.generator = generator;
	}

	public Visit evaluateVisit(Visit entityItem) throws ConstraintViolations {
				
		if (!checkValidRoundNumber(entityItem.getRoundNumber())) 
    		throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");	
		
		if (generator.isGenerated())
			return generateId(entityItem);
		
		generator.validateIdLength(entityItem.getExtId(), generator.getIdScheme());
		
        return entityItem;
    }
	
	public Visit generateId(Visit entityItem) throws ConstraintViolations {
		entityItem.setExtId(generator.generateId(entityItem));
		return entityItem;
	}
	
	public Visit checkVisit(Visit persistedItem, Visit entityItem) throws ConstraintViolations {
		
		if (!checkValidRoundNumber(entityItem.getRoundNumber())) 
    		throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");			
		return entityItem;
	}
	
	public void validateGeneralVisit(Visit visit) throws ConstraintViolations {
		if (!checkValidRoundNumber(visit.getRoundNumber())) 
    		throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");			
	}
	
	/**
	 * Checks if the provided round number exists
	 */
	public boolean checkValidRoundNumber(Integer roundNumber) {
		
		Round round = genericDao.findByProperty(Round.class, "roundNumber", roundNumber);
		if (round != null)
			return true;
		return false;
	}
    
    /**
     * Retrieves all Visit extId's that contain the term provided.
     * Used in performing autocomplete.
     */
    public List<String> getVisitExtIds(String term) {
    	List<String> ids = new ArrayList<String>();
    	List<Visit> list = genericDao.findAll(Visit.class, true);
    	Iterator<Visit> itr = list.iterator();
    	while(itr.hasNext()) {
    		Visit item = itr.next();
    		if (item.getExtId().toLowerCase().contains(term.toLowerCase())) 
    			ids.add(item.getExtId());
    	}
    	return ids;
    }
    
    public Visit findVisitById(String visitId, String msg) throws Exception {
        Visit visit = genericDao.findByProperty(Visit.class, "extId", visitId);
        if (visit == null) {
            throw new Exception(msg);
        }
        return visit;
    }
    
    
    /**
     * The following extension methods are called from the Crud in retrieving 
     * the associated extensions, grouped by entity 
     */
    public Visit initializeExtensions(Visit entityItem) {
    	List<ClassExtension> list = genericDao.findListByProperty(ClassExtension.class, "roundNumber", entityItem.getRoundNumber());
    	
    	for (ClassExtension ce : list) {    		
    		Extension extension = new Extension();
    		extension.setEntity(entityItem);
    		extension.setClassExtension(ce);
    		entityItem.getExtensions().add(extension);
    	}
    	return entityItem;
    }
    
    public Visit addExtensions(Visit entityItem, EntityType name) {
    	    	
    	List<ClassExtension> list = getExtensionsByEntityClassAndRoundNumber(name, entityItem.getRoundNumber());
    	
    	for (ClassExtension ce : list) {    		
    		Extension extension = new Extension();
    		extension.setEntity(entityItem);
    		extension.setClassExtension(ce);
    		entityItem.getExtensions().add(extension);
    	}
    	return entityItem;
    }
    
    public List<ClassExtension> getExtensionsByEntityClassAndRoundNumber(EntityType entityType, int roundNum) {
    	
    	final EntityType entityName = entityType;
    	final int visitRoundNum = roundNum;
    	
    	ValueProperty roundNumber = new ValueProperty() {
			public String getPropertyName() {
				return "roundNumber";
			}
			public Object getValue() {
				return visitRoundNum;
			}	
		};
		
		ValueProperty indivType = new ValueProperty() {
			public String getPropertyName() {
				return "entityClass";
			}
			public Object getValue() {
				return entityName;
			}	
		};  	
    	
    	return genericDao.findListByMultiProperty(ClassExtension.class, roundNumber, indivType);
    } 
}
