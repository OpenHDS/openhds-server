package org.openhds.domain.model;

public class CensusIndividual {
	String uuid;
	
	SocialGroup socialGroup;

	FieldWorker collectedBy;
	
	Location location;
	
	Individual  individual;
	
	String bIsToA;
	
	Individual spouse;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public SocialGroup getSocialGroup() {
		return socialGroup;
	}

	public void setSocialGroup(SocialGroup socialGroup) {
		this.socialGroup = socialGroup;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Individual getIndividual() {
		return individual;
	}
	
	public void setIndividual(Individual individual) {
		this.individual = individual ;
	}

	public FieldWorker getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(FieldWorker collectedBy) {
		this.collectedBy = collectedBy;
	}

	public String getbIsToA() {
		return bIsToA;
	}

	public void setbIsToA(String bIsToA) {
		this.bIsToA = bIsToA;
	}


	public Individual getSpouse() {
		return spouse;
	}

	public void setSpouse(Individual spouse) {
		this.spouse = spouse;
	}


}
