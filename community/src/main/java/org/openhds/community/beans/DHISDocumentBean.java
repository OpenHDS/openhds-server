package org.openhds.community.beans;

import java.util.Calendar;
import java.util.Date;

public class DHISDocumentBean {
	
	Date startDate;
	Date endDate;
	Calendar sDate;
	Calendar eDate;

	String period;
		
	public Date getStartDate() {
    	if (sDate == null)
    		return new Date();
    	
    	return sDate.getTime();
	}
	
	public void setStartDate(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		sDate = cal;
	}
	
	public Date getEndDate() {
		if (eDate == null)
    		return new Date();
    	
    	return eDate.getTime();
	}
	
	public void setEndDate(Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		eDate = cal;
	}
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Calendar getsDate() {
		return sDate;
	}

	public void setsDate(Calendar sDate) {
		this.sDate = sDate;
	}

	public Calendar geteDate() {
		return eDate;
	}

	public void seteDate(Calendar eDate) {
		this.eDate = eDate;
	}
}
