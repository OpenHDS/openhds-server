package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.OutMigration;
import org.openhds.controller.service.OutMigrationService;

public class OutMigrationCrudImpl extends EntityCrudImpl<OutMigration, String> {
	
	OutMigrationService service;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;

	public OutMigrationCrudImpl(Class<OutMigration> entityClass) {
		super(entityClass);
		entityFilter = new OutMigrationEntityFilter();
	}
	
    @Override
    public String create() {
    	// fix: during the update flow, the individual used for the out migration becomes detached from the hibernate
    	// session. this call re-attaches the individual who changes made to it will be persisted back
    	entityItem.setIndividual(dao.merge(entityItem.getIndividual()));
    	
    	try {
			service.evaluateOutMigration(entityItem);		
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
    		return "pretty:outMigEdit";
    	}	
    	return null;
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

	public OutMigrationService getService() {
		return service;
	}

	public void setService(OutMigrationService service) {
		this.service = service;
	}

	private class OutMigrationEntityFilter implements EntityFilter<OutMigration> {

		public List<OutMigration> getFilteredEntityList(OutMigration entityItem) {
			return service.getOutMigrations(entityItem.getIndividual());
		}
		
	}
}
