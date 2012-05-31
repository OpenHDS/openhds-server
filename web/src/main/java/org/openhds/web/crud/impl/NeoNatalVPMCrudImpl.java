package org.openhds.web.crud.impl;

import java.util.Calendar;
import java.util.Date;

import org.openhds.domain.model.NeoNatalVPM;

public class NeoNatalVPMCrudImpl extends EntityCrudImpl<NeoNatalVPM, String> {

	 // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;
    
	public NeoNatalVPMCrudImpl(Class<NeoNatalVPM> entityClass) {
		super(entityClass);
	}

	@Override
	public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:neonatalvpmEdit";
    	}
    	
    	return null;		
	}

	public Date getRecordedDate() {
		if (entityItem.getRecordedDate() == null)
		    return new Date();  	
		return entityItem.getRecordedDate().getTime();
	}

	public void setRecordedDate(Date interviewDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(interviewDate);
		entityItem.setRecordedDate(cal);
	}
}
