package org.openhds.web.vld;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
		
		System.out.println("CheckMinimumEnumerationStartDateValidator: " + properties.getEthiopianCalendar() + " " + value.getClass());
		
		try {
			earliestEnumerationDate = calendarUtil.parseDate(properties.getEarliestEnumerationDate());
		} catch(Exception e) { }
			
		Calendar recordedDate = null;
		
		if(properties.getEthiopianCalendar()){
			Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.getDefault());
			DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy").withChronology(chron_eth);
			DateTime dt_eth = new DateTime((Date)value).withChronology(chron_eth); 
//			DateTime dt_greg = dt_eth.withChronology(GregorianChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa")));
			recordedDate = dt_eth.toGregorianCalendar();
			System.out.println("recorded date back in gregorian: " + recordedDate.getTime());
		}
		else{
			Date date = (Date)value;
			recordedDate = Calendar.getInstance();
			recordedDate.setTime(date);
		}
		
		if (recordedDate.before(earliestEnumerationDate)) {
			FacesMessage message = new FacesMessage("The specified date is before the minimum date of enumeration : " + properties.getEarliestEnumerationDate());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}
