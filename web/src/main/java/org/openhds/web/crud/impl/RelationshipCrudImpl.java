package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Relationship;
import org.openhds.controller.service.RelationshipService;
import org.springframework.binding.message.MessageContext;

public class RelationshipCrudImpl extends EntityCrudImpl<Relationship, String> {

    EntityValidationService<Relationship> entityValidator;
	RelationshipService service;
	Individual selectedIndiv;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date startDate;
    Date endDate;
	
    public RelationshipCrudImpl(Class<Relationship> entityClass) {
        super(entityClass);
        entityFilter = new RelationshipEntityFilter();
    }

	@Override
    public String create() {
			
        try {  
        	entityItem.setEndType(properties.getNotApplicableCode());
        	if (entityValidator.checkConstraints(entityItem) == false) {
        		
        		service.evaluateRelationship(entityItem);	
    	        entityItem.setInsertDate(Calendar.getInstance());
    	        return super.create();
        	}		 
        }
        catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}	  	
    	return null;      
    }
	
    @Override
    public boolean commit(MessageContext messageContext) {
    	
    	try {
    		entityItem.setEndType(properties.getNotApplicableCode());
    		service.evaluateRelationship(entityItem);
    		return super.commit(messageContext);
    	} catch(ConstraintViolations e) {
    		webFlowService.createMessage(messageContext, e.getMessage());
    	}	
    	return false;
    }
	
    @Override
    public String edit() {
    	
    	Relationship persistedItem = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));

        try {
        	service.checkRelationship(persistedItem, entityItem);
        	entityService.save(entityItem);
        } 
        catch(Exception e) {
        	jsfService.addError(e.getMessage());
        	return null;
		} 

        return "pretty:relationshipEdit";
    }
    
    public Date getStartDate() {
    	
    	if (entityItem.getStartDate() == null)
    		return new Date();
    	
    	return entityItem.getStartDate().getTime();
	}

	public void setStartDate(Date startDate) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		entityItem.setStartDate(cal);
	}
	
    public Date getEndDate() {
    	
    	if (entityItem.getEndDate() == null)
    		return null;
    	
    	return entityItem.getEndDate().getTime();
	}

	public void setEndDate(Date endDate) throws ParseException {
		
		if (endDate == null)
			entityItem.setEndDate(null);
		
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			entityItem.setEndDate(cal);
		}
	}
    
	public EntityValidationService<Relationship> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(EntityValidationService<Relationship> entityValidator) {
		this.entityValidator = entityValidator;
	}
    
    public RelationshipService getService() {
		return service;
	}

	public void setService(RelationshipService service) {
		this.service = service;
	}
	
	public Individual getSelectedIndiv() {
		return selectedIndiv;
	}

	public void setSelectedIndiv(Individual selectedIndiv) {
		this.selectedIndiv = selectedIndiv;
	}
	
	private class RelationshipEntityFilter implements EntityFilter<Relationship> {

		public List<Relationship> getFilteredEntityList(Relationship entityItem) {
			return service.getAllRelationships(selectedIndiv);
		}	
	}
}
