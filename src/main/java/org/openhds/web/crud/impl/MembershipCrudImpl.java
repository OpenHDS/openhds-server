package org.openhds.web.crud.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import org.openhds.service.EntityValidationService;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Membership;
import org.openhds.service.MembershipService;
import org.springframework.binding.message.MessageContext;

public class MembershipCrudImpl extends EntityCrudImpl<Membership, String> {

    EntityValidationService<Membership> entityValidator;
	MembershipService service;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date startDate;
    Date endDate;
	
    public MembershipCrudImpl(Class<Membership> entityClass) {
        super(entityClass);
        entityFilter = new RelationshipEntityFilter();
    }

	// the entityitem, the pojo, can have its fields set before being created by super create
    // Note that super create calls dao.create(entityItem);
    @Override
    public String create() {
    	    	
    	try {
    		entityItem.setEndType(properties.getNotApplicableCode());
    		if (entityValidator.checkConstraints(entityItem) == false) {	
	    		service.evaluateMembership(entityItem);		
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
    		service.evaluateMembership(entityItem);
    		
            //Gets the individual's memberships if any
            // Iterates through memberships and sets endType(OMG) and endDate
            if (!entityItem.getIndividual().getAllMemberships().isEmpty()) {
                Set<Membership> memberships = (Set<Membership>) entityItem.getIndividual().getAllMemberships();
                for (Membership mem : memberships) {
                	if (mem.getEndType().equals(properties.getNotApplicableCode())) {
    	                mem.setEndDate(entityItem.getStartDate());
    	                mem.setEndType(properties.getOutmigrationCode());
    	                try {
    						entityService.save(mem);
    					} catch (SQLException e) {
    						 throw new ConstraintViolations(
    				                    "There as a problem updating the database with the membership associated with the out migration");
    					}
                	}
                }
            }
    		return super.commit(messageContext);
    	} catch(ConstraintViolations e) {
    		webFlowService.createMessage(messageContext, e.getMessage());
    	}	
    	return false;
    }
    
    @Override
    public String edit() {
    	
    	Membership persistedItem = (Membership)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));

        try {
        	service.checkMembership(persistedItem, entityItem);
        	entityService.save(entityItem);
        } 
        catch(Exception e) {
        	jsfService.addError(e.getMessage());
        	return null;
		} 
        
        return "pretty:membershipEdit";
    }
    
    public boolean validateMembership(MessageContext messageContext) {
    	try {
			service.evaluateMembership(entityItem);
		} catch (ConstraintViolations e) {
			webFlowService.createMessage(messageContext, e.getMessage());
			return false;
		}    	
		
		return true;
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
        
	public EntityValidationService<Membership> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(
			EntityValidationService<Membership> entityValidator) {
		this.entityValidator = entityValidator;
	}
    
    public MembershipService getService() {
		return service;
	}

	public void setService(MembershipService service) {
		this.service = service;
	}
	
	private class RelationshipEntityFilter implements EntityFilter<Membership> {

		public List<Membership> getFilteredEntityList(Membership entityItem) {
			return service.getAllMemberships(entityItem.getIndividual());
		}	
	}
}
