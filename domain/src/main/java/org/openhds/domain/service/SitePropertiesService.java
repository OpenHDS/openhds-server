package org.openhds.domain.service;

public interface SitePropertiesService {
	
	String getLocale();
	String getDateFormat();
	String getEarliestEnumerationDate();		
    String getSpecialStudyLocation();			
	String getUnknownIdentifier();	
	String getInmigrationCode();
	String getOutmigrationCode();
	String getBirthCode();
	String getDeathCode();
	String getEnumerationCode();
	String getMarriageCode();
	String getNotApplicableCode();
	String getLiveBirthCode();
	String getStillBirthCode();
	String getMiscarriageCode();
	String getAbortionCode();
	String getDataStatusWarningCode();
	String getDataStatusValidCode();
	String getDataStatusFatalCode();
	String getDataStatusVoidCode();
	String getDataStatusPendingCode();
	String getDataStatusClosedCode();
	String getYesResponse();
	String getNoResponse();
	String getMaleCode();
	String getFemaleCode();

	int getMinimumAgeOfParents();
	int getMinimumAgeOfHouseholdHead();
	int getMinimumAgeOfMarriage();
	int getMinimumAgeOfPregnancy();

	boolean isAutocomplete();	
	boolean isValueExtensionsForMembershipCodesEnabled();
}
