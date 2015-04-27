package org.openhds.report.beans;

import java.sql.Date;

public class ActiveIndividualsBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6720273896651603970L;
	
	String individualExtId;
	String locationExtId;
	String firstName;
	String middleName;
	String lastName;
	String gender;
	Date dob;
	String father;
	String mother;
	String subVillage;
	String socialGroup;
	Date startDate;
	String startType;
	String familyName;
	String subVillageName;
	String villageCode;
	String villageName;
	String head;
	Date dobHead;
		
	public ActiveIndividualsBean(String invididualId, String locationId, Date startDate){
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public String getSubVillage() {
		return subVillage;
	}

	public void setSubVillage(String subVillage) {
		this.subVillage = subVillage;
	}

	public String getSocialGroup() {
		return socialGroup;
	}

	public void setSocialGroup(String socialGroup) {
		this.socialGroup = socialGroup;
	}

	public String getStartType() {
		return startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getSubVillageName() {
		return subVillageName;
	}

	public void setSubVillageName(String subVillageName) {
		this.subVillageName = subVillageName;
	}

	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Date getDobHead() {
		return dobHead;
	}

	public void setDobHead(Date dobHead) {
		this.dobHead = dobHead;
	}		
}