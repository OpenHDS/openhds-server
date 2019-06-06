package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Round;
import org.openhds.service.RoundService;

public class RoundCrudImpl extends EntityCrudImpl<Round, String> {

	RoundService service;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date startDate;
    Date endDate;

	public RoundCrudImpl(Class<Round> entityClass) {
		super(entityClass);
	}
	
	@Override 
	public String createSetup() {
	        reset(false, true);
	        showListing=false;
	        entityItem = newInstance();
	        navMenuBean.setNextItem(entityClass.getSimpleName());
	        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
	        return outcomePrefix + "_create";
	    }
	
	@Override
	public String create() {
		try {
			service.evaluateRound(entityItem);
		} catch (ConstraintViolations e) {
			jsfService.addError(e.getMessage());
			return null;
		}
		
		return super.create();
	}

	@Override
	public String edit() {
		try {
			service.evaluateRound(entityItem);
		} catch (ConstraintViolations e) {
			jsfService.addError(e.getMessage());
			return null;
		}
		
		return super.edit();
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
    		 return new Date();
    	
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

	public RoundService getService() {
		return service;
	}

	public void setService(RoundService service) {
		this.service = service;
	}
}
