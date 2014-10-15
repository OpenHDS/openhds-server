package org.openhds.web.cvt;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.domain.service.SitePropertiesService;

public class MembershipEndTypeCodeConverter implements Converter {
	
	SitePropertiesService siteProperties;

	String deathValue;
	String outmigrationValue;
	String notApplicableValue;
	String deathOfHOHValue;
	
	MembershipEndTypeCodeConverter(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		deathValue = siteProperties.getDeathCode();
		outmigrationValue = siteProperties.getOutmigrationCode();
		notApplicableValue = siteProperties.getNotApplicableCode();
		deathOfHOHValue = siteProperties.getDeathOfHOHCode();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {	
		
		if (arg2 != null && arg2.equals(deathValue) || arg2.equals(outmigrationValue) ||
				arg2.equals(notApplicableValue) || arg2.equals(deathOfHOHValue)) 
			return arg2;
		else {
			FacesMessage message = new FacesMessage("Invalid code entered for Membership End Type.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);
		}
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof String) {
			if (arg2.equals(deathValue) || arg2.equals(outmigrationValue) ||
					arg2.equals(notApplicableValue) || arg2.equals(deathOfHOHValue)) 
				return arg2.toString();
		}
		return null;
	}
}
