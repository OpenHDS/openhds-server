package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.openhds.domain.model.Note;

public class NoteCrudImpl extends EntityCrudImpl<Note, String> {
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date observationDate;
	
    public NoteCrudImpl(Class<Note> entityClass) {
        super(entityClass);
    }

    public Date getObservationDate() {
    	if (entityItem.getObservationDate() == null)
    		return new Date();
    	return entityItem.getObservationDate().getTime();
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


	public void setObservationDate(Date observationDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(observationDate);
		entityItem.setObservationDate(cal);
	}
}
