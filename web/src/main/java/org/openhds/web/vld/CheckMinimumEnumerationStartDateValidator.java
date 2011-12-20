package org.openhds.web.vld;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;

/**
 * Used only from the baseline from the openfaces datechooser when validating 
 * the minimum enumeration start date
 */
public class CheckMinimumEnumerationStartDateValidator extends AppContextAware {

	SitePropertiesService properties;
	CalendarUtil calendarUtil;
	
	CheckMinimumEnumerationStartDateValidator() {
		calendarUtil = (CalendarUtil)context.getBean("calendarUtil");
		properties = (SitePropertiesService)context.getBean("siteProperties");
	}

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		Calendar earliestEnumerationDate = null;
		
		try {
			earliestEnumerationDate = calendarUtil.parseDate(properties.getEarliestEnumerationDate());
		} catch(Exception e) { }
			
		Date date = (Date)value;
		Calendar recordedDate = Calendar.getInstance();
		recordedDate.setTime(date);
		
		if (recordedDate.before(earliestEnumerationDate)) {
			FacesMessage message = new FacesMessage("The specified date is before the minimum date of enumeration : " + properties.getEarliestEnumerationDate());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}
