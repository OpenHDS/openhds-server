package org.openhds.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.service.SitePropertiesService;

public class ResidencyEndTypeCodeConverter implements Converter {
	
	SitePropertiesService siteProperties;

	String outmigrationValue;
	String deathValue;
	
	ResidencyEndTypeCodeConverter(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		outmigrationValue = siteProperties.getOutmigrationCode();
		deathValue = siteProperties.getDeathCode();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {	
		if ("".equals(arg2)) {
			return null;
		}
		if (arg2 != null && arg2.equals(outmigrationValue) || arg2.equals(deathValue)) 
			return arg2;
		else {
			FacesMessage message = new FacesMessage("Residency End Type can only contain the values " + outmigrationValue + " or " + deathValue);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);
		}
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof String) {
			if (arg2.equals(outmigrationValue) || arg2.equals(deathValue))
				return arg2.toString();
		}
		return null;
	}
}
