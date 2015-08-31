package org.openhds.domain.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeneralSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6338817073217742486L;
	private int minimumAgeOfParents;	
	private int minimumAgeOfHouseholdHead;
	private int minMarriageAge;	
	private int minimumAgeOfPregnancy;
	private String visitAt;
	private String earliestEventDate;

	@XmlElement(name = "minMarriageAge")
	public int getMinMarriageAge() {
		return minMarriageAge;
	}

	public void setMinMarriageAge(int minMarriageAge) {
		this.minMarriageAge = minMarriageAge;
	}

	@XmlElement(name = "minAgeOfParents")
	public int getMinimumAgeOfParents() {
		return minimumAgeOfParents;
	}

	public void setMinimumAgeOfParents(int minimumAgeOfParents) {
		this.minimumAgeOfParents = minimumAgeOfParents;
	}

	@XmlElement(name = "minAgeOfHouseholdHead")
	public int getMinimumAgeOfHouseholdHead() {
		return minimumAgeOfHouseholdHead;
	}

	public void setMinimumAgeOfHouseholdHead(int minimumAgeOfHouseholdHead) {
		this.minimumAgeOfHouseholdHead = minimumAgeOfHouseholdHead;
	}

	@XmlElement(name = "minAgeOfPregnancy")
	public int getMinimumAgeOfPregnancy() {
		return minimumAgeOfPregnancy;
	}

	public void setMinimumAgeOfPregnancy(int minimumAgeOfPregnancy) {
		this.minimumAgeOfPregnancy = minimumAgeOfPregnancy;
	}
	
	@XmlElement(name = "visitLevel")
	public String getVisitAt(){
		return this.visitAt;
	}
	
	public void setVisitAt(String visitAt){
		this.visitAt = visitAt;
	}

	@XmlElement(name = "earliestEventDate")
	public String getEarliestEventDate() {
		return earliestEventDate;
	}

	public void setEarliestEventDate(String earliestEventDate) {
		this.earliestEventDate = earliestEventDate;
	}
	
}
