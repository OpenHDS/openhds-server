package org.openhds.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.openhds.service.SitePropertiesService;

public class DateConverter implements Converter {

	private String dateFormat;
	private SitePropertiesService siteProperties;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Date cal = null;	
								
		try {			
			if(siteProperties.getEthiopianCalendar()){
				//Convert from Ethiopian to Gregorian Calendar 
				Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.getDefault());
				DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy").withChronology(chron_eth);
				DateTime dt_eth = dtf.parseDateTime(value); 
				DateTime dt_greg = dt_eth.withChronology(GregorianChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa")));
				cal = dt_greg.toGregorianCalendar().getTime();
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
		if (value == null || !(value instanceof Date))
			return null;
		
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String formattedDate = new String();
		
		if(siteProperties.getEthiopianCalendar()){
			// Create Ethiopian Chronology
			Chronology chron_eth = EthiopicChronology.getInstance(DateTimeZone.getDefault());
			
			DateTime dt_greg = new DateTime((Date)value).withChronology(GregorianChronology.getInstance(DateTimeZone.forID("Africa/Addis_Ababa")));
			DateTime dt_eth = dt_greg.withChronology(chron_eth);
			
			formattedDate =  DateTimeFormat.forPattern("dd/MM/yyyy").print(dt_eth);
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
