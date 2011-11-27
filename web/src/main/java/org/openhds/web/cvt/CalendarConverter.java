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

public class CalendarConverter implements Converter {

	private String dateFormat;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	
			Calendar cal = null;	
								
		try {
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			Date date = formatter.parse(value);
			cal = Calendar.getInstance();
			cal.setTime(date);
			
		} 
		catch (ParseException e) {			
			FacesMessage msg = new FacesMessage("Please provide a valid date");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return cal;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value == null)
			return null;
		
		Calendar calendar = (Calendar) value;
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(calendar.getTime());
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
