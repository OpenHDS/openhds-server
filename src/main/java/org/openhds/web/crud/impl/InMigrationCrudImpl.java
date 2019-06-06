package org.openhds.web.crud.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.InMigration;
import org.openhds.domain.ReasonForMigration;
import org.openhds.domain.Residency;
import org.openhds.service.InMigrationService;

public class InMigrationCrudImpl extends EntityCrudImpl<InMigration, String> {

	InMigrationService service;

    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;
	
    public InMigrationCrudImpl(Class<InMigration> entityClass) {
        super(entityClass);
        entityFilter = new InMigrationEntityFilter();
    }
    
    @Override
    public String createSetup() {
    	String outcome = super.createSetup(); 
    	return outcome.replace("inmigration", "migration");
    }
    
    public List<SelectItem> getReasonsForMigration() {
    	List<SelectItem> output = new ArrayList<SelectItem>();
		List<ReasonForMigration> items = jsfService.arrayToList(ReasonForMigration.values());
		
		for (ReasonForMigration pt : items) {
			output.add(new SelectItem(pt, pt.getReason()));
		}
		return output;
    }

	@Override
	public String create() {
    	try {
    		createInMigration();
		} catch (Exception e) {
			jsfService.addError(e.getMessage());
			return null;
		}
		
		return listSetup();
	}

	private void createInMigration() throws ConstraintViolations, SQLException, Exception {
		service.evaluateInMigration(entityItem);
		service.createInMigration(entityItem);
	}

	@Override
	public String edit() {
    	try {
			service.evaluateInMigrationOnEdit(entityItem);		
	        super.edit();
	        
	        return "pretty:migrationsEdit";
    	}		
    	catch(Exception e) {
    		jsfService.addError(e.getMessage());
    	}	  	
    	return null;
	}    
	
	@Override
	public String editSetup() {
		// when saving an in migration, it requires crawling several associations that
		// are lazy loaded by hibernate. This "hack" will crawl those associations so that
		// they are loaded before the entity is detached from the hibernate session. There
		// is a need to provide for a better approach to doing this, but it seems like that would take
		// a fair bit of refactoring. Possibly something that could be done in a future iteration
		// This patch is meant to make things work for now
		String outcome = super.editSetup();
		
		// force the individuals past residency records to be loaded as required for
		// integrity constraint checks by residencyService
		if (entityItem != null) {
			Set<Residency> res = entityItem.getIndividual().getAllResidencies();
			res.size();
		}
		
		return outcome;
	}
	
	public String startInternal() {
		createSetup();
		
		return "internal_inmigration";
	}
	
	public InMigrationService getService() {
		return service;
	}

	public void setService(InMigrationService service) {
		this.service = service;
	}
	
	private class InMigrationEntityFilter implements EntityFilter<InMigration> {

		public List<InMigration> getFilteredEntityList(InMigration entityItem) {
			if (entityItem.getIndividual() != null && entityItem.getIndividual().getUuid() != null) {				
				// its possible for an in migration to have an individual that is not persisted
				// yet during an external in migration. If an individual that is not yet persisted
				// is passed to this service, a TransientObjectException will be thrown
				return service.getInMigrationsByIndividual(entityItem.getIndividual());				
			}
			
			return new ArrayList<InMigration>();
		}
		
	}
	
	/**
	 * (droberge):
	 * This is a work around for a JSF issue I was experiencing. The InMigration entity has a field
	 * of the wrapper type Boolean. JSF doesn't seem to want to accept the wrapper, and only accepts
	 * the primitive type. See http://www.icefaces.org/JForum/posts/list/5474.page for a discussion
	 * @return
	 */
	public boolean isUnknownIndividual() {
		return entityItem.isUnknownIndividual() ? true : false;
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
}
