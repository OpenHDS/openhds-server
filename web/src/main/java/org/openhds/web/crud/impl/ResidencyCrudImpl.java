package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Residency;
import org.openhds.controller.service.ResidencyService;

public class ResidencyCrudImpl extends EntityCrudImpl<Residency, String> {
	
	private ResidencyService residencyService;

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

	public ResidencyService getResidencyService() {
		return residencyService;
	}

	public void setResidencyService(ResidencyService residencyService) {
		this.residencyService = residencyService;
	}
}