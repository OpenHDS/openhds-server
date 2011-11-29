package org.openhds.web.crud.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.controller.service.PregnancyService;

public class PregnancyObservationCrudImpl extends EntityCrudImpl<PregnancyObservation, String> {

	EntityValidationService<PregnancyObservation> entityValidator;
	PregnancyService service;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date edd;
    Date recordedDate;
	
	public PregnancyObservationCrudImpl(Class<PregnancyObservation> entityClass) {
        super(entityClass);
        entityFilter = new PregnancyObservationEntityFilter();
    }
	
	@Override
    public String create() {
		try { 
			service.evaluatePregnancyObservation(entityItem);		
	        entityItem.setInsertDate(Calendar.getInstance());
	        entityItem.setStatus(properties.getDataStatusPendingCode());
	        return super.create();
    	}		
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
	
	@Override
	public String edit() {
		String outcome = super.edit();
		
		if (outcome != null) {
			return "pretty:pregnancyobservationEdit";
		}
		
		return outcome;
	}
	
	@Override
	public String delete() {		
		PregnancyObservation obs = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
		obs.setStatus(properties.getDataStatusClosedCode());
		
        try {
			entityService.delete(obs);
		} catch (SQLException e) {
			jsfService.addError("Could not delete the persistent entity");
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		}

        return listSetup();
	}
	
    public Date getRecordedDate() { 	
    	if (entityItem.getRecordedDate() == null)
    		return new Date();
    	
    	return entityItem.getRecordedDate().getTime();
	}

	public void setRecordedDate(Date recordedDate) throws ParseException {	
		Calendar cal = Calendar.getInstance();
		cal.setTime(recordedDate);
		entityItem.setRecordedDate(cal);
	}
	
	public Date getEdd() { 	
    	if (entityItem.getExpectedDeliveryDate() == null)
    		return new Date();
    	
    	return entityItem.getExpectedDeliveryDate().getTime();
	}

	public void setEdd(Date edd) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(edd);
		entityItem.setExpectedDeliveryDate(cal);
	}
    
	public EntityValidationService<PregnancyObservation> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(EntityValidationService<PregnancyObservation> entityValidator) {
		this.entityValidator = entityValidator;
	}

	public PregnancyService getService() {
		return service;
	}

	public void setService(PregnancyService service) {
		this.service = service;
	}
	
	private class PregnancyObservationEntityFilter implements EntityFilter<PregnancyObservation> {

		public List<PregnancyObservation> getFilteredEntityList(PregnancyObservation entityItem) {
			return service.getPregnancyObservationByIndividual(entityItem.getMother());
		}
	}
}
