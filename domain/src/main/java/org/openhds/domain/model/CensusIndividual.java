package org.openhds.domain.model;

public class CensusIndividual {
	String uuid;
	
	String socialGroupExtId;
	
	String socialGroupHeadExtId;

	String collectedBy;
	
	String locationExtId;
	
	Individual  individual;
	
	String bIsToA;
	
	Individual spouse;
	
	public String getSocialGroupExtId() {
		return socialGroupExtId;
	}
	
	public void setSocialGroupExtId(String socialGroupExtId) {
		this.socialGroupExtId = socialGroupExtId;
	}
	
	public String getLocationExtId() {
		return locationExtId;
	}
	
	public void setLocationExtId(String locationExtId) {
		this.locationExtId = locationExtId;
	}
	
	public Individual getIndividual() {
		return individual;
	}
	
	public void setIndividual(Individual individual) {
		this.individual = individual ;
	}

	public String getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(String collectedBy) {
		this.collectedBy = collectedBy;
	}

	public String getbIsToA() {
		return bIsToA;
	}

	public void setbIsToA(String bIsToA) {
		this.bIsToA = bIsToA;
	}

	public String getSocialGroupHeadExtId() {
		return socialGroupHeadExtId;
	}

	public void setSocialGroupHeadExtId(String socialGroupHeadExtId) {
		this.socialGroupHeadExtId = socialGroupHeadExtId;
	}

	public Individual getSpouse() {
		return spouse;
	}

	public void setSpouse(Individual spouse) {
		this.spouse = spouse;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
