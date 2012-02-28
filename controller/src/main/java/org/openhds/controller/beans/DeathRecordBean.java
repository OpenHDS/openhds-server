package org.openhds.controller.beans;

public class DeathRecordBean {
	
	String ageGroupName;
	String locationExtId = "";
	
	int maleCount;
	int femaleCount;
		
	public DeathRecordBean(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}
	
	public String getAgeGroupName() {
		return ageGroupName;
	}
	
	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}
	
	public int getMaleCount() {
		return maleCount;
	}
	
	public void addMaleCount() {
		this.maleCount++;
	}
	
	public int getFemaleCount() {
		return femaleCount;
	}
	
	public void addFemaleCount() {
		this.femaleCount++;
	}
	
	public String getLocationExtId() {
		return locationExtId;
	}

	public void setLocationExtId(String locationExtId) {
		this.locationExtId = locationExtId;
	}
}
