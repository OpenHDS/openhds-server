package org.openhds.web.cvt;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.domain.service.SitePropertiesService;

public class MembershipStartTypeCodeConverter implements Converter {
	
	SitePropertiesService siteProperties;

	String birthValue;
	String inmigrationValue;
	String marriageValue;
	String enumerationValue;
	
	MembershipStartTypeCodeConverter(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		birthValue = siteProperties.getBirthCode();
		inmigrationValue = siteProperties.getInmigrationCode();
		marriageValue = siteProperties.getMarriageCode();
		enumerationValue = siteProperties.getEnumerationCode();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {	
		
		if (arg2 != null && arg2.equals(birthValue) || arg2.equals(inmigrationValue) ||
				arg2.equals(marriageValue) || arg2.equals(enumerationValue)) 
			return arg2;
		else {
			FacesMessage message = new FacesMessage("Invalid code entered for Membership Start Type.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);
		}
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof String) {
			if (arg2.equals(birthValue) || arg2.equals(inmigrationValue) ||
					arg2.equals(marriageValue) || arg2.equals(enumerationValue)) 
				return arg2.toString();
		}
		return null;
	}
}
