package org.openhds.web.crud.impl;

import java.util.Calendar;
import java.util.Date;
import org.openhds.domain.PostNeoNatalVPM;

public class PostNeoNatalVPMCrudImpl extends EntityCrudImpl<PostNeoNatalVPM, String> {

	 // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;
    
	public PostNeoNatalVPMCrudImpl(Class<PostNeoNatalVPM> entityClass) {
		super(entityClass);
	}

	@Override
	public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:postneonatalvpmEdit";
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
