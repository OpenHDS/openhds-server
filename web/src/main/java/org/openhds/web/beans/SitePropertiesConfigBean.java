package org.openhds.web.beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.openhds.controller.service.SiteConfigService;
import org.openhds.web.service.JsfService;
import org.springframework.core.io.ClassPathResource;

public class SitePropertiesConfigBean {
			
	String unknownIdentifier;
	String yesResponse;
	String noResponse;
	String inmigration;
	String outmigration;
	String birth;
	String death;
	String deathOfHOH;
	String enumeration;
	String marriage;
	String notApplicable;
	String liveBirth;
	String stillBirth;
	String miscarriage;
	String abortion;
	String male;
	String female;
	String dataStatusWarning;
	String dataStatusValid;
	String dataStatusFatal;
	String dataStatusVoid;
	String dataStatusPending;
	String dataStatusClosed;
	String dateFormat;
	String autocomplete;
	String locale;
	String earliestEnumerationDate;
	String visitAt;

	Integer minAgeOfParenthood;
	Integer minAgeOfHouseholdHead;
	Integer minAgeOfMarriage;
	Integer minAgeOfPregnancy;
	
	Date dateOfEnumeration;
	
	JsfService jsfService;
	
	SiteConfigService siteConfigService;
	
	public void create() {
		Properties properties = readCodeProperties();
		properties.put("unknownIdentifier", unknownIdentifier);
		properties.put("yesResponse", yesResponse);
		properties.put("noResponse", noResponse);
		properties.put("inmigration", inmigration);
		properties.put("outmigration", outmigration);
		properties.put("birth", birth);
		properties.put("death", death);
		properties.put("deathOfHOH", deathOfHOH);
		properties.put("enumeration", enumeration);
		properties.put("marriage", marriage);	
		properties.put("notApplicable", notApplicable);
		properties.put("liveBirth", liveBirth);
		properties.put("stillBirth", stillBirth);
		properties.put("miscarriage", miscarriage);
		properties.put("abortion", abortion);
		properties.put("male", male);
		properties.put("female", female);	
		properties.put("dataStatusWarning", dataStatusWarning);
		properties.put("dataStatusValid", dataStatusValid);
		properties.put("dataStatusFatal", dataStatusFatal);
		properties.put("dataStatusVoid", dataStatusVoid);
		properties.put("dataStatusPending", dataStatusPending);
		properties.put("dataStatusClosed", dataStatusClosed);	
		properties.put("dateFormat", dateFormat);
		properties.put("autocomplete", autocomplete);
		properties.put("visitAt", visitAt);
		properties.put("locale", locale);
		properties.put("minAgeOfParenthood", minAgeOfParenthood.toString());
		properties.put("minAgeOfHouseholdHead", minAgeOfHouseholdHead.toString());
		properties.put("minAgeOfMarriage", minAgeOfMarriage.toString());
		properties.put("minAgeOfPregnancy", minAgeOfPregnancy.toString());
		properties.put("earliestEnumerationDate", earliestEnumerationDate);
		writePropertyFile(properties);
		updateIdLength();
	}
	
	@PostConstruct
	public void onStartup(){		
		updateIdLength();
	}	
	
	/* Update the value of openhds.visitIdLength
	/* Depending on if the visit level is at the location (12) or socialGroup level (14)
	 */
	private void updateIdLength(){		
		int newVisitIdLength = visitAt.equalsIgnoreCase("location")? 12 : 14;
		int currentVisitIdLength = siteConfigService.getVisitIdLength();
		
		if(newVisitIdLength != currentVisitIdLength){
			siteConfigService.setVisitIdLength(newVisitIdLength);
		}
	}
			
    public Date getDateOfEnumeration() throws ParseException {
    	
    	if (earliestEnumerationDate == null)
    		return new Date();
    	
    	DateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date = formatter.parse(earliestEnumerationDate);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal.getTime();
	}

	public void setDateOfEnumeration(Date dateOfEnumeration) throws ParseException {
		SimpleDateFormat sdFormat = new SimpleDateFormat(dateFormat);
		earliestEnumerationDate = sdFormat.format(dateOfEnumeration);
	}
			
	public Properties readCodeProperties() {
		FileInputStream fis = null;
		Properties prop = null;
		
		try {
			fis = new FileInputStream(
					new ClassPathResource("codes.properties").getFile());
			if (fis != null) {
				prop = new Properties();
				prop.load(fis);
			}
			fis.close();	
		} catch (Exception e) {
			jsfService.addMessage("Error in reading Property file. Exception : " + e.getMessage());
			prop = null;
		}
		return prop;
	}
	
	public void writePropertyFile(Properties props) {
		FileOutputStream fos = null;
		try {	
			fos = new FileOutputStream(
					new ClassPathResource("codes.properties").getFile());
			props.store(fos, "Code Configuration updated");
		} catch (Exception e) {
			jsfService.addMessage("Error writing Property file. Exception : " + e.getMessage());
			return;
		}
		jsfService.addMessage("Code Configuration updated successfully. Redeploy the web application for changes to take effect.");
	}
	
	public String getUnknownIdentifier() {
		return unknownIdentifier;
	}
	
	public void setUnknownIdentifier(String unknownIdentifier) {
		this.unknownIdentifier = unknownIdentifier;
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
	
	public String getInmigration() {
		return inmigration;
	}
	
	public void setInmigration(String inmigration) {
		this.inmigration = inmigration;
	}
	
	public String getOutmigration() {
		return outmigration;
	}
	
	public void setOutmigration(String outmigration) {
		this.outmigration = outmigration;
	}
	
	public String getBirth() {
		return birth;
	}
	
	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	public String getDeath() {
		return death;
	}
	
	public void setDeath(String death) {
		this.death = death;
	}
	
	public String getDeathOfHOH() {
		return deathOfHOH;
	}
	
	public void setDeathOfHOH(String deathOfHOH) {
		this.deathOfHOH = deathOfHOH;
	}	
	
	public String getEnumeration() {
		return enumeration;
	}
	
	public void setEnumeration(String enumeration) {
		this.enumeration = enumeration;
	}
	
	public String getMarriage() {
		return marriage;
	}
	
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	
	public String getNotApplicable() {
		return notApplicable;
	}
	
	public void setNotApplicable(String notApplicable) {
		this.notApplicable = notApplicable;
	}
	
	public String getLiveBirth() {
		return liveBirth;
	}
	
	public void setLiveBirth(String liveBirth) {
		this.liveBirth = liveBirth;
	}
	
	public String getStillBirth() {
		return stillBirth;
	}
	
	public void setStillBirth(String stillBirth) {
		this.stillBirth = stillBirth;
	}
	
	public String getMiscarriage() {
		return miscarriage;
	}
	
	public void setMiscarriage(String miscarriage) {
		this.miscarriage = miscarriage;
	}
	
	public String getAbortion() {
		return abortion;
	}
	
	public void setAbortion(String abortion) {
		this.abortion = abortion;
	}
	
	public String getMale() {
		return male;
	}
	
	public void setMale(String male) {
		this.male = male;
	}
	
	public String getFemale() {
		return female;
	}
	
	public void setFemale(String female) {
		this.female = female;
	}
	
	public String getDataStatusWarning() {
		return dataStatusWarning;
	}
	
	public void setDataStatusWarning(String dataStatusWarning) {
		this.dataStatusWarning = dataStatusWarning;
	}
	
	public String getDataStatusValid() {
		return dataStatusValid;
	}
	
	public void setDataStatusValid(String dataStatusValid) {
		this.dataStatusValid = dataStatusValid;
	}
	
	public String getDataStatusFatal() {
		return dataStatusFatal;
	}
	
	public void setDataStatusFatal(String dataStatusFatal) {
		this.dataStatusFatal = dataStatusFatal;
	}
	
	public String getDataStatusVoid() {
		return dataStatusVoid;
	}
	
	public void setDataStatusVoid(String dataStatusVoid) {
		this.dataStatusVoid = dataStatusVoid;
	}
	
	public String getDataStatusPending() {
		return dataStatusPending;
	}
	
	public void setDataStatusPending(String dataStatusPending) {
		this.dataStatusPending = dataStatusPending;
	}
	
	public String getDataStatusClosed() {
		return dataStatusClosed;
	}
	
	public void setDataStatusClosed(String dataStatusClosed) {
		this.dataStatusClosed = dataStatusClosed;
	}
	
	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}
	
	public String getEarliestEnumerationDate() {
		return earliestEnumerationDate;
	}

	public void setEarliestEnumerationDate(String earliestEnumerationDate) {
		this.earliestEnumerationDate = earliestEnumerationDate;
	}
	
	public Integer getMinAgeOfParenthood() {
		return minAgeOfParenthood;
	}

	public void setMinAgeOfParenthood(Integer minAgeOfParenthood) {
		this.minAgeOfParenthood = minAgeOfParenthood;
	}

	public Integer getMinAgeOfHouseholdHead() {
		return minAgeOfHouseholdHead;
	}

	public void setMinAgeOfHouseholdHead(Integer minAgeOfHouseholdHead) {
		this.minAgeOfHouseholdHead = minAgeOfHouseholdHead;
	}

	public Integer getMinAgeOfMarriage() {
		return minAgeOfMarriage;
	}

	public void setMinAgeOfMarriage(Integer minAgeOfMarriage) {
		this.minAgeOfMarriage = minAgeOfMarriage;
	}
	
	public Integer getMinAgeOfPregnancy() {
		return minAgeOfPregnancy;
	}

	public void setMinAgeOfPregnancy(Integer minAgeOfPregnancy) {
		this.minAgeOfPregnancy = minAgeOfPregnancy;
	}
	
	public String getVisitAt(){
		return this.visitAt;
	}
	
	public void setVisitAt(String visitAt){
		this.visitAt = visitAt;
	}
	
	public void setSiteConfigService(SiteConfigService siteConfigService){
		this.siteConfigService = siteConfigService;
	}
	
	public SiteConfigService getSiteConfigService(){
		return this.siteConfigService;
	}
}
