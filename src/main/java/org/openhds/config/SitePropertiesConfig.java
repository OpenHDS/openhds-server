package org.openhds.config;

import org.openhds.service.JsfService;
import org.openhds.service.SiteConfigService;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.impl.SitePropertiesServiceImpl;
import org.openhds.web.beans.SitePropertiesConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:codes.properties", "classpath:site-config.properties"})
public class SitePropertiesConfig {
	
	@Value("${locale}")
	String locale;
	
	@Value("${dateFormat}")
	String dateFormat;
	
	@Value("${autocomplete}")
	String autocomplete;
	
	@Value("${unknownIdentifier}")
	String unknownIdentifier;
	
	@Value("${inmigration}")
	String inmigrationCode;
	
	@Value("${outmigration}")
	String outmigrationCode;
	
	@Value("${birth}")
	String birthCode;
	
	@Value("${death}")
	String deathCode;
	
	@Value("${deathOfHOH}")
	String deathOfHOHCode;
	
	@Value("${enumeration}")
	String enumerationCode;
	
	@Value("${marriage}")
	String marriageCode;
	
	@Value("${divorceSeparation}")
	String divorceSeparationCode;
	
	@Value("${notApplicable}")
	String notApplicableCode;
	
	@Value("${liveBirth}")
	String liveBirthCode;
	
	@Value("${stillBirth}")
	String stillBirthCode;
	
	@Value("${miscarriage}")
	String miscarriageCode;
	
	@Value("${abortion}")
	String abortionCode;
	
	@Value("${male}")
	String maleCode;
	
	@Value("${female}")
	String femaleCode;
	
	@Value("${dataStatusWarning}")
	String dataStatusWarningCode;
	
	@Value("${dataStatusValid}")
	String dataStatusValidCode;
	
	@Value("${dataStatusFatal}")
	String dataStatusFatalCode;
	
	@Value("${dataStatusVoid}")
	String dataStatusVoidCode;
	
	@Value("${dataStatusPending}")
	String dataStatusPendingCode;
	
	@Value("${dataStatusClosed}")
	String dataStatusClosedCode;
	
	@Value("${minAgeOfParenthood}")
	String minimumAgeOfParents;
	
	@Value("${minAgeOfHouseholdHead}")
	String minimumAgeOfHouseholdHead;
	
	@Value("${minAgeOfMarriage}")
	String minimumAgeOfMarriage;
	
	@Value("${minAgeOfPregnancy}")
	String minimumAgeOfPregnancy;
	
	@Value("${yesResponse}")
	String yesResponse;
	
	@Value("${noResponse}")
	String noResponse;
	
	@Value("${earliestEnumerationDate}")
	String earliestEnumerationDate;
	
	@Value("${earliestEventDate}")
	String earliestEventDate;
	
	@Value("${specialStudyLocation}")
	String specialStudyLocation;
	
	@Value("${visitAt}")
	String visitAt;
	
	@Value("${ethiopianCalendar}")
	String ethiopianCalendar;
	
	@Bean
	public SitePropertiesService siteProperties() {
		SitePropertiesServiceImpl properties = new SitePropertiesServiceImpl();
		properties.setLocale(locale);
		properties.setDateFormat(dateFormat);
		properties.setAutocomplete(Boolean.parseBoolean(autocomplete));
		properties.setUnknownIdentifier(unknownIdentifier);
		properties.setInmigrationCode(inmigrationCode);
		properties.setOutmigrationCode(outmigrationCode);
		properties.setBirthCode(birthCode);
		properties.setDeathCode(deathCode);
		properties.setDeathOfHOHCode(deathOfHOHCode);
		properties.setEnumerationCode(enumerationCode);
		properties.setMarriageCode(marriageCode);
		properties.setDivorceSeparationCode(divorceSeparationCode);
		properties.setNotApplicableCode(notApplicableCode);
		properties.setLiveBirthCode(liveBirthCode);
		properties.setStillBirthCode(stillBirthCode);
		properties.setMiscarriageCode(miscarriageCode);
		properties.setAbortionCode(abortionCode);
		properties.setMaleCode(femaleCode);
		properties.setFemaleCode(femaleCode);
		properties.setDataStatusWarningCode(dataStatusWarningCode);
		properties.setDataStatusValidCode(dataStatusValidCode);
		properties.setDataStatusFatalCode(dataStatusFatalCode);
		properties.setDataStatusVoidCode(dataStatusVoidCode);
		properties.setDataStatusClosedCode(dataStatusClosedCode);
		properties.setMinimumAgeOfHouseholdHead(Integer.parseInt(minimumAgeOfHouseholdHead));
		properties.setMinimumAgeOfMarriage(Integer.parseInt(minimumAgeOfMarriage));
		properties.setMinimumAgeOfParents(Integer.parseInt(minimumAgeOfParents));
		properties.setMinimumAgeOfPregnancy(Integer.parseInt(minimumAgeOfPregnancy));
		properties.setYesResponse(yesResponse);
		properties.setNoResponse(noResponse);
		properties.setEarliestEnumerationDate(earliestEnumerationDate);
		properties.setEarliestEventDate(earliestEventDate);
		properties.setSpecialStudyLocation(specialStudyLocation);
		properties.setVisitAt(visitAt);
		properties.setEthiopianCalendar(Boolean.parseBoolean(ethiopianCalendar));
		return properties;
	}
	
	@Bean
	public SitePropertiesConfigBean sitePropertiesConfigBean(JsfService jsfService, SiteConfigService siteConfigService, SitePropertiesService siteProperties) {
		SitePropertiesConfigBean config = new SitePropertiesConfigBean();
		config.setJsfService(jsfService);
		config.setSiteConfigService(siteConfigService);
		config.setSitePropertiesService(siteProperties);
		config.setLocale(locale);
		config.setDateFormat(dateFormat);
		config.setAutocomplete(autocomplete);
		config.setUnknownIdentifier(unknownIdentifier);
		config.setInmigration(inmigrationCode);
		config.setOutmigration(outmigrationCode);
		config.setBirth(birthCode);
		config.setDeath(deathCode);
		config.setDeathOfHOH(deathOfHOHCode);
		config.setEnumeration(enumerationCode);
		config.setMarriage(marriageCode);
		config.setDivorceSeparation(divorceSeparationCode);
		config.setNotApplicable(notApplicableCode);
		config.setLiveBirth(liveBirthCode);
		config.setStillBirth(stillBirthCode);
		config.setMiscarriage(miscarriageCode);
		config.setAbortion(abortionCode);
		config.setMale(femaleCode);
		config.setFemale(femaleCode);
		config.setDataStatusWarning(dataStatusWarningCode);
		config.setDataStatusValid(dataStatusValidCode);
		config.setDataStatusFatal(dataStatusFatalCode);
		config.setDataStatusVoid(dataStatusVoidCode);
		config.setDataStatusClosed(dataStatusClosedCode);
		config.setMinAgeOfHouseholdHead(Integer.parseInt(minimumAgeOfHouseholdHead));
		config.setMinAgeOfMarriage(Integer.parseInt(minimumAgeOfMarriage));
		config.setMinAgeOfParenthood(Integer.parseInt(minimumAgeOfParents));
		config.setMinAgeOfPregnancy(Integer.parseInt(minimumAgeOfPregnancy));
		config.setYesResponse(yesResponse);
		config.setNoResponse(noResponse);
		config.setEarliestEnumerationDate(earliestEnumerationDate);
		config.setEarliestEventDate(earliestEventDate);
		config.setVisitAt(visitAt);
		config.setEthiopianCalendar(ethiopianCalendar);
		return config;
	}
}
