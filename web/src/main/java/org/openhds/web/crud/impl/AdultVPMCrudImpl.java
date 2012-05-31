package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.openhds.domain.model.AdultVPM;

public class AdultVPMCrudImpl extends EntityCrudImpl<AdultVPM, String> {
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;
    
    public AdultVPMCrudImpl(Class<AdultVPM> entityClass) {
		super(entityClass);
	}

    @Override
    public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:adultvpmEdit";
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
}
