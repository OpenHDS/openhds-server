package org.openhds.report.beans;

import java.sql.Date;

public class InvalidIndividualsBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6720273896651603970L;
	
	String individualExtId;
	String locationExtId;
	Date startDate;	
	
	public InvalidIndividualsBean(){
		
	}
	
	public InvalidIndividualsBean(String invididualId, String locationId, Date startDate){
		this.individualExtId = invididualId;
		this.locationExtId = locationId;
		this.startDate = startDate;
	}
	
	public String getIndividualExtId() {
		return individualExtId;
	}
	public void setIndividualExtId(String individualId) {
		this.individualExtId = individualId;
	}
	public String getLocationExtId() {
		return locationExtId;
	}
	public void setLocationExtId(String locationId) {
		this.locationExtId = locationId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}		
}