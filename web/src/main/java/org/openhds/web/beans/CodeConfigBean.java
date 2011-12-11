package org.openhds.web.beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.openhds.web.service.JsfService;
import org.springframework.core.io.ClassPathResource;

public class CodeConfigBean {
	
	String unknownIdentifier;
	String yesResponse;
	String noResponse;
	String inmigration;
	String outmigration;
	String birth;
	String death;
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
	
	JsfService jsfService;
	
	public void create() {
		Properties properties = readCodeProperties();
		properties.put("unknownIdentifier", unknownIdentifier);
		properties.put("yesResponse", yesResponse);
		properties.put("noResponse", noResponse);
		properties.put("inmigration", inmigration);
		properties.put("outmigration", outmigration);
		properties.put("birth", birth);
		properties.put("death", death);
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
		writePropertyFile(properties);
	}
	
	public Properties readCodeProperties() {
		FileInputStream fis = null;
		Properties prop = null;
		
		try {
			fis = new FileInputStream(new ClassPathResource("codes.properties").getFile());
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
			fos = new FileOutputStream("src/main/resources/codes.properties");
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

}
