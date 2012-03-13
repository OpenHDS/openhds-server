package org.openhds.controller.beans;

/**
 * This is used in the DHIS export for counting up aggregate data by category.
 * A RecordItem contains the ageGroupName it represents. For example, 0-28 days.
 * It also contains counts for both male and female that fit into that particular
 * category. Do not use this class directly, just supply the proper parameters 
 * to the Period and this structure will be constructed automatically.
 */
public class RecordItem {
	
	String ageGroupName;
	
	int maleCount;
	int femaleCount;
		
	public RecordItem(String ageGroupName) {
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
}
