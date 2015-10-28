package org.openhds.domain.service.impl;

import org.openhds.domain.service.SitePropertiesService;

public class SitePropertiesServiceImpl implements SitePropertiesService {
     
	String locale;
	String dateFormat;
	String earliestEnumerationDate;
	String earliestEventDate;
	String specialStudyLocation;
	String unknownIdentifier;
	String inmigrationCode;
	String outmigrationCode;
	String birthCode;
	String deathCode;
	String deathOfHOHCode;
	String enumerationCode;
	String marriageCode;
	String divorceSeparationCode;
	String miscarriageCode;
	String notApplicableCode;
	String liveBirthCode;
	String stillBirthCode;
	String abortionCode;
	String maleCode;
	String femaleCode;
	String dataStatusWarningCode;
	String dataStatusValidCode;
	String dataStatusFatalCode;
	String dataStatusVoidCode;
	String dataStatusPendingCode;
	String dataStatusClosedCode;
	String yesResponse;
	String noResponse;	
	int minimumAgeOfParents;
	int minimumAgeOfHouseholdHead;
	int minimumAgeOfMarriage;
	int minimumAgeOfPregnancy;
	boolean autocomplete;
	boolean ethiopianCalendar;
	boolean valueExtensionsForMembershipCodesEnabled;
	String visitAt;
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
   
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getEarliestEnumerationDate() {
		return earliestEnumerationDate;
	}

	public void setEarliestEnumerationDate(String earliestEnumerationDate) {
		this.earliestEnumerationDate = earliestEnumerationDate;
	}
	
	public String getEarliestEventDate() {
		return earliestEventDate;
	}
	
	public void setEarliestEventDate(String earliestEventDate) {
		this.earliestEventDate = earliestEventDate;
	}
	
    public boolean isAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(boolean autocomplete) {
		this.autocomplete = autocomplete;
	}
					
    public String getSpecialStudyLocation() {
		return specialStudyLocation;
	}

	public void setSpecialStudyLocation(String specialStudyLocation) {
		this.specialStudyLocation = specialStudyLocation;
	}
	
	public boolean isValueExtensionsForMembershipCodesEnabled() {
		return valueExtensionsForMembershipCodesEnabled;
	}

	public void setValueExtensionsForMembershipCodesEnabled(boolean valueExtensionsForMembershipCodesEnabled) {
		this.valueExtensionsForMembershipCodesEnabled = valueExtensionsForMembershipCodesEnabled;
	}
								
	public String getUnknownIdentifier() {
		return unknownIdentifier;
	}

	public void setUnknownIdentifier(String unknownIdentifier) {
		this.unknownIdentifier = unknownIdentifier;
	}
	
	public String getInmigrationCode() {
		return inmigrationCode;
	}

	public void setInmigrationCode(String inmigrationCode) {
		this.inmigrationCode = inmigrationCode;
	}

	public String getOutmigrationCode() {
		return outmigrationCode;
	}

	public void setOutmigrationCode(String outmigrationCode) {
		this.outmigrationCode = outmigrationCode;
	}

	public String getBirthCode() {
		return birthCode;
	}

	public void setBirthCode(String birthCode) {
		this.birthCode = birthCode;
	}

	public String getDeathCode() {
		return deathCode;
	}

	public void setDeathCode(String deathCode) {
		this.deathCode = deathCode;
	}

	public String getEnumerationCode() {
		return enumerationCode;
	}

	public void setEnumerationCode(String enumerationCode) {
		this.enumerationCode = enumerationCode;
	}

	public String getMarriageCode() {
		return marriageCode;
	}

	public void setMarriageCode(String marriageCode) {
		this.marriageCode = marriageCode;
	}

	public String getDivorceSeparationCode() {
		return divorceSeparationCode;
	}

	public void setDivorceSeparationCode(String divorceSeparationCode) {
		this.divorceSeparationCode = divorceSeparationCode;
	}

	public String getNotApplicableCode() {
		return notApplicableCode;
	}

	public void setNotApplicableCode(String notApplicableCode) {
		this.notApplicableCode = notApplicableCode;
	}
	
	public String getLiveBirthCode() {
		return liveBirthCode;
	}

	public void setLiveBirthCode(String liveBirthCode) {
		this.liveBirthCode = liveBirthCode;
	}

	public String getStillBirthCode() {
		return stillBirthCode;
	}

	public void setStillBirthCode(String stillBirthCode) {
		this.stillBirthCode = stillBirthCode;
	}

	public String getMiscarriageCode() {
		return miscarriageCode;
	}

	public void setMiscarriageCode(String miscarriageCode) {
		this.miscarriageCode = miscarriageCode;
	}

	public String getAbortionCode() {
		return abortionCode;
	}

	public void setAbortionCode(String abortionCode) {
		this.abortionCode = abortionCode;
	}
	
	public String getDataStatusWarningCode() {
		return dataStatusWarningCode;
	}

	public void setDataStatusWarningCode(String dataStatusWarningCode) {
		this.dataStatusWarningCode = dataStatusWarningCode;
	}

	public String getDataStatusValidCode() {
		return dataStatusValidCode;
	}

	public void setDataStatusValidCode(String dataStatusValidCode) {
		this.dataStatusValidCode = dataStatusValidCode;
	}

	public String getDataStatusFatalCode() {
		return dataStatusFatalCode;
	}

	public void setDataStatusFatalCode(String dataStatusFatalCode) {
		this.dataStatusFatalCode = dataStatusFatalCode;
	}

	public String getDataStatusVoidCode() {
		return dataStatusVoidCode;
	}

	public void setDataStatusVoidCode(String dataStatusVoidCode) {
		this.dataStatusVoidCode = dataStatusVoidCode;
	}
	
	public String getDataStatusPendingCode() {
		return dataStatusPendingCode;
	}

	public void setDataStatusPendingCode(String dataStatusPendingCode) {
		this.dataStatusPendingCode = dataStatusPendingCode;
	}

	public String getDataStatusClosedCode() {
		return dataStatusClosedCode;
	}

	public void setDataStatusClosedCode(String dataStatusClosedCode) {
		this.dataStatusClosedCode = dataStatusClosedCode;
	}
	
	public int getMinimumAgeOfParents() {
		return minimumAgeOfParents;
	}

	public void setMinimumAgeOfParents(int minimumAgeOfParents) {
		this.minimumAgeOfParents = minimumAgeOfParents;
	}
	
	public int getMinimumAgeOfHouseholdHead() {
		return minimumAgeOfHouseholdHead;
	}

	public void setMinimumAgeOfHouseholdHead(int minimumAgeOfHouseholdHead) {
		this.minimumAgeOfHouseholdHead = minimumAgeOfHouseholdHead;
	}
	
	public int getMinimumAgeOfMarriage() {
		return minimumAgeOfMarriage;
	}

	public void setMinimumAgeOfMarriage(int minimumAgeOfMarriage) {
		this.minimumAgeOfMarriage = minimumAgeOfMarriage;
	}
	
	public int getMinimumAgeOfPregnancy() {
		return minimumAgeOfPregnancy;
	}

	public void setMinimumAgeOfPregnancy(int minimumAgeOfPregnancy) {
		this.minimumAgeOfPregnancy = minimumAgeOfPregnancy;
	}

	public String getYesResponse() {
		return yesResponse;
	}

	public void setYesResponse(String yesResponse) {
		this.yesResponse = yesResponse;
	}

	public String getNoResponse() {
		return noResponse;
	}

	public void setNoResponse(String noResponse) {
		this.noResponse = noResponse;
	}
	
	public String getMaleCode() {
		return maleCode;
	}
	
	public void setMaleCode(String maleCode) {
		this.maleCode = maleCode;
	}

	public void setFemaleCode(String femaleCode) {
		this.femaleCode = femaleCode;
	}
	
	public String getFemaleCode() {
		return femaleCode;
	}
	
	public void setDeathOfHOHCode(String deathofHOHCode) {
		this.deathOfHOHCode = deathofHOHCode;
	}	

	public String getDeathOfHOHCode() {
		return deathOfHOHCode;
	}
	
	public void setVisitAt(String visitAt){
		this.visitAt = visitAt;
	}
	
	public String getVisitAt(){
		return visitAt;
	}

    public boolean getEthiopianCalendar() {
		return ethiopianCalendar;
	}

	public void setEthiopianCalendar(boolean ethiopianCalendar) {
		this.ethiopianCalendar = ethiopianCalendar;
	}

}
