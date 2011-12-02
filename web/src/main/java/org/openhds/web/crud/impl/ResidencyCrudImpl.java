package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.AuditableCollectedEntity;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.controller.service.ResidencyService;

public class ResidencyCrudImpl extends EntityCrudImpl<Residency, String> {
	
	private ResidencyService residencyService;

	// a list of associated events for a residency. 
	// only used when attempting to delete a residency
	private List<AuditableCollectedEntity> events;

	public ResidencyCrudImpl(Class<Residency> entityClass) {
		super(entityClass);
	}

	@Override
	public String create() {
		try {
			entityItem.setEndType(properties.getNotApplicableCode());
			residencyService.evaluateResidency(entityItem);
			return super.create();
		} catch (ConstraintViolations e) {
			jsfService.addError(e.getMessage());
		}
		
		return null;
	}
	
    @Override
	public String delete() {
    	Residency persistentObject = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	List<AuditableCollectedEntity> entities = residencyService.getResidencyAssociatedEvents(persistentObject);
    	if (entities.size() > 0) {
    		events = entities;
    		showListing = false;
    		return "residency_delete_failure";
    	} 
    	
    	return super.delete();
    }
    
    public String getStartEventName() {
    	for(AuditableCollectedEntity en : events) {
    		if (en instanceof InMigration) {
    			return "In Migration";
    		}
    		
    		if (en instanceof PregnancyOutcome) {
    			return "Birth";
    		}
    	}
    	
    	return "";
    }
    
    public String getStartEventUuid() {
    	for(AuditableCollectedEntity en : events) {
    		if (en instanceof InMigration || en instanceof PregnancyOutcome) {
    			return en.getUuid();
    		}
    	}    	
    	
    	return "";
    }
    
    public String getEndEventName() {
    	for(AuditableCollectedEntity en: events) {
    		if (en instanceof Death) {
    			return "Death";
    		}
    		
    		if (en instanceof OutMigration) {
    			return "Out Migration";
    		}
    	}
    	
    	return "";
    }
    
    public String getEndEventUuid() {
    	for(AuditableCollectedEntity en: events) {
    		if (en instanceof Death || en instanceof OutMigration) {
    			return en.getUuid();
    		}
    	}    	
    	
    	return "";
    }

	public Date getStartDate() {
    	
    	if (entityItem.getStartDate() == null) {
    		return new Date();
    	}
    	
    	return entityItem.getStartDate().getTime();
	}

	public void setStartDate(Date startDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		entityItem.setStartDate(cal);
	}

	public ResidencyService getResidencyService() {
		return residencyService;
	}

	public void setResidencyService(ResidencyService residencyService) {
		this.residencyService = residencyService;
	}
}
