package org.openhds.domain.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An adapter for converting between String and Calendar types when posting
 * to a restful webservice. Jax-rs requires custom marshalling if the field 
 * cannot be mapped. 
 */
public class CalendarAdapter extends XmlAdapter<String, Calendar> {
	
	private final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	private CalendarUtil calendarUtil;
	
	public CalendarAdapter(CalendarUtil calendarUtil) {
	    this.calendarUtil = calendarUtil;
	}
		
	public String marshal(Calendar v) throws Exception {
		return calendarUtil.formatDate(v);
	}

	public Calendar unmarshal(String v) throws Exception {
		DateFormat formatter = new SimpleDateFormat(SQL_DATE_FORMAT);
		formatter.setLenient(false);
		try {
			Date d = formatter.parse(v);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal;
		} catch(Exception e) {}
		
		return calendarUtil.parseDate(v);
	}
}
