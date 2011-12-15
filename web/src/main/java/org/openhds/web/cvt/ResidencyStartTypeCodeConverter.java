package org.openhds.web.cvt;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.domain.service.SitePropertiesService;

public class ResidencyStartTypeCodeConverter implements Converter {
	
	SitePropertiesService siteProperties;

	String inmigrationValue;
	String birthValue;
	String enumerationValue;
	
	ResidencyStartTypeCodeConverter(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		inmigrationValue = siteProperties.getInmigrationCode();
		birthValue = siteProperties.getBirthCode();
		enumerationValue = siteProperties.getEnumerationCode();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {	
		
		if (arg2 != null && arg2.equals(inmigrationValue) || arg2.equals(birthValue) || arg2.equals(enumerationValue)) 
			return arg2;
		else {
			FacesMessage message = new FacesMessage("Residency Start Type can only contain the values " + inmigrationValue + ", " + birthValue + " or " + enumerationValue);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);
		}
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof String) {
			if (arg2.equals(inmigrationValue) || arg2.equals(birthValue) || arg2.equals(enumerationValue)) 
				return arg2.toString();
		}
		return null;
	}
}
