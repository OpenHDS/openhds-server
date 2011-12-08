package org.openhds.report.beans;

/**
 * JavaBean used for the Household Registration Book report
 * @author Dave
 */
public class HouseholdRegisterBean {
	
	String houseNumber;
	String locationId;
	String name;
	String dob;
	String familyName;
	String individualId;
	String gender;
	String father;
	String mother;
	String lowestLevelName;
	String lowestLevelValue;
	
	public String getHouseNumber() {
		return houseNumber;
	}
	
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	
	public String getLocationId() {
		return locationId;
	}
	
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getIndividualId() {
		return individualId;
	}
	
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getFather() {
		return father;
	}
	
	public void setFather(String father) {
		this.father = father;
	}
	
	public String getMother() {
		return mother;
	}
	
	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getLowestLevelName() {
		return lowestLevelName;
	}

	public void setLowestLevelName(String lowestLevelName) {
		this.lowestLevelName = lowestLevelName;		
	}

	public String getLowestLevelValue() {
		return lowestLevelValue;
	}

	public void setLowestLevelValue(String parentLocation) {
		this.lowestLevelValue = parentLocation;
	}
}
