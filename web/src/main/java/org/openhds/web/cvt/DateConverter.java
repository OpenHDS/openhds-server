package org.openhds.web.cvt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhds.domain.service.SitePropertiesService;

public class DateConverter implements Converter {

	private String dateFormat;
	private SitePropertiesService siteProperties;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
		System.out.println("DateConverter::getAsObject()");
		Date cal = null;	
								
		try {			
			if(siteProperties.getEthiopianCalendar()){
				//Convert from Ethiopian to Gregorian Calendar 
//				Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa"));
				Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.getDefault());
				DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy").withChronology(chron_eth);
				DateTime dt_eth = dtf.parseDateTime(value); 
//				dt_eth.plusHours(3);
				DateTime dt_greg = dt_eth.withChronology(GregorianChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa")));
				cal = dt_greg.toGregorianCalendar().getTime();
				
				
				System.out.println("DateConverter::getAsObject: Received: " + value + " | Converted back to gregorian: " + cal);
//				DateTime dtLMDGreg = getCurrentEthiopianDateDisplay().withChronology(GregorianChronology.getInstance());
//	            DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM yyyy");
//	            String str = fmt.print(dtLMDGreg);
			}
			else{
				DateFormat formatter = new SimpleDateFormat(dateFormat);
				Date date = formatter.parse(value);
				cal = date;
			}
			
		} 
		catch (ParseException e) {			
			FacesMessage msg = new FacesMessage("Please provide a valid date");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
		catch(Exception e){
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Please provide a valid date");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return cal;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

//		System.out.println("SiteProperties: " + siteProperties.getEthiopianCalendar());
		
		System.out.println("Of class: " + value.getClass());
				
		if (value == null || !(value instanceof Date))
			return null;
		
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String formattedDate = new String();
		
		if(siteProperties.getEthiopianCalendar()){
//			System.out.println("Use Ethiopian Calendar");
			// Create Ethiopian Chronology
			Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.getDefault());
			
			// Create Ethiopian Date/Time (Y, M, D, H, M, S, mS)
			//DateTime dt_eth = new DateTime(2003, 2, 30, 0, 0, 0, 0, chron_eth);

			// Convert to Gregorian Date/Time
			//DateTime dt_greg = dt_eth.withChronology(GregorianChronology.getInstance());

			//System.out.println("Ethiopian Date: " + DateTimeFormat.fullDate().print(dt_eth));
			//System.out.println("Gregorian Date: " + DateTimeFormat.fullDate().print(dt_greg));
			
			DateTime dt_greg = new DateTime((Date)value).withChronology(GregorianChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa")));
			DateTime dt_eth = dt_greg.withChronology(chron_eth);
//			dt_eth = new DateTime(2003, 13, 3, 0, 0, 0, 0, chron_eth);
			
//			System.out.println("Ethiopian Date: " + DateTimeFormat.forPattern("dd/MM/yyyy").print(dt_eth));
//			System.out.println("Gregorian Date: " + DateTimeFormat.forPattern(dateFormat).print(dt_greg));
			
			formattedDate =  DateTimeFormat.forPattern("dd/MM/yyyy").print(dt_eth);
			
			System.out.println("DateConverter::getAsString: Received: " + dt_greg.toString() + " | Converted to ethiopian: " + formattedDate);
		}
		else{
			Date calendar = (Date) value;
			formattedDate = formatter.format(calendar); 
		}
		return formattedDate;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public SitePropertiesService getSitePropertiesService() {
		return this.siteProperties;
	}

	public void setSitePropertiesService(SitePropertiesService sitePropertiesService) {
		this.siteProperties= sitePropertiesService;
	}
}
