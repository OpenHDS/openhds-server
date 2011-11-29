package org.openhds.web.crud.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Death;
import org.openhds.controller.service.DeathService;
import org.springframework.binding.message.MessageContext;

public class DeathCrudImpl extends EntityCrudImpl<Death, String> {
	
	EntityValidationService<Death> entityValidator;
	DeathService service;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date deathDate;

	public DeathCrudImpl(Class<Death> entityClass) {
        super(entityClass);
        entityFilter = new DeathEntityFilter();
    }

    @Override
    public String create() {
        try {       	
        	if (entityValidator.checkConstraints(entityItem) == false) {	
	        	service.evaluateDeath(entityItem);
	        	service.createDeath(entityItem);
	        	super.create();
        	}
        } 
        catch(ConstraintViolations e) {
        	jsfService.addError(e.getMessage());
        } catch (ConstraintViolationException e) {
        	jsfService.addError(e.getMessage());
		} catch (SQLException e) {
			jsfService.addError(e.getMessage());
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
		}

        return null;
    }
    
    @Override
    public String delete() {
    	
    	Death death = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	service.deleteDeath(death);
    	
        try {
			entityService.delete(death);
		} catch (SQLException e) {
			jsfService.addError("Could not delete the persistent entity");
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		}

        return listSetup();
    }
    
    @Override
    public boolean commit(MessageContext messageContext) {
    	try {
    		service.evaluateDeath(entityItem);
    		service.createDeath(entityItem);
    		return super.commit(messageContext);
    		
    	} catch(Exception e) {
    		webFlowService.createMessage(messageContext, e.getMessage());
    	}    	
    	return false;
    }
    
    @Override
    public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:deathEdit";
    	}
    	
    	return outcome;
    }
    
    public Date getDeathDate() {
    	if (entityItem.getDeathDate() == null)
    		return new Date();
    	
    	return entityItem.getDeathDate().getTime();
	}

	public void setDeathDate(Date deathDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(deathDate);
		entityItem.setDeathDate(cal);
	}
		    
    public DeathService getService() {
		return service;
	}

	public void setService(DeathService service) {
		this.service = service;
	}
	
	public EntityValidationService<Death> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(EntityValidationService<Death> entityValidator) {
		this.entityValidator = entityValidator;
	}

	private class DeathEntityFilter implements EntityFilter<Death> {

		public List<Death> getFilteredEntityList(Death entityItem) {
			List<Death> deaths = service.getDeathsByIndividual(entityItem.getIndividual());
			Iterator<Death> itr = deaths.iterator();
			while(itr.hasNext()) {
				if (itr.next().isDeleted()) {
					itr.remove();
				}
			}
			
			return deaths;
		}
		
	}
}
