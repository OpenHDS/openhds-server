package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.openhds.domain.model.DemRates;

public class DemRatesCrudImpl extends EntityCrudImpl<DemRates, String> {
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date startDate;
    Date endDate;
	
	public DemRatesCrudImpl(Class<DemRates> entityClass) {
        super(entityClass);
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
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		entityItem.setEndDate(cal);
	}
}
