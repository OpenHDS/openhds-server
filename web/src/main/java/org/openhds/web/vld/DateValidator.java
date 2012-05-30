package org.openhds.web.vld;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.constraint.AppContextAware;

public class DateValidator extends AppContextAware {
	
	SitePropertiesService properties;
	
	DateValidator() {
		properties = (SitePropertiesService)context.getBean("siteProperties");
	}

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		Calendar cal = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(properties.getDateFormat());
			sdf.setLenient(false);
			Date date = sdf.parse(value.toString());
			cal = Calendar.getInstance();
			cal.setTime(date);
		}
		catch (Exception e) { 
			FacesMessage message = new FacesMessage("The specified date invalid : " + value.toString());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}
