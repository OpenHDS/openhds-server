package org.openhds.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.service.SitePropertiesService;

public class PregnancyTypeCodeConverter implements Converter {
	
	SitePropertiesService siteProperties;

	String liveBirthValue;
	String stillBirthValue;
	String miscarriageValue;
	String abortionValue;
	
	PregnancyTypeCodeConverter(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		liveBirthValue = siteProperties.getLiveBirthCode();
		stillBirthValue = siteProperties.getStillBirthCode();
		miscarriageValue = siteProperties.getMiscarriageCode();
		abortionValue = siteProperties.getAbortionCode();
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if (value != null && value.equals(liveBirthValue) || value.equals(stillBirthValue) ||
				value.equals(stillBirthValue) || value.equals(abortionValue) || 
				value.equals(miscarriageValue)) 
			return value;
		else {
			FacesMessage msg = new FacesMessage("Not a valid Pregnancy Outcome Type.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (value instanceof String) {
			if (value.equals(liveBirthValue) || value.equals(stillBirthValue) ||
				value.equals(stillBirthValue) || value.equals(abortionValue) || 
				value.equals(miscarriageValue))
			return value.toString();
		}
		
		return null;
	}

}
