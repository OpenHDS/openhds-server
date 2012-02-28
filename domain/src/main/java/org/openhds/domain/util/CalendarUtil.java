package org.openhds.domain.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.openhds.domain.service.SitePropertiesService;

public class CalendarUtil {
	
	SitePropertiesService siteProperties;
	
	public Calendar parseDate(String dateStr) throws ParseException {
    	DateFormat formatter = new SimpleDateFormat(siteProperties.getDateFormat());
        Date date = formatter.parse(dateStr);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal;
    }
    
	public Calendar convertDateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
    	DateFormat formatter = new SimpleDateFormat(siteProperties.getDateFormat());
		cal.setTime(date);
		formatter.setCalendar(cal);
		return cal;
	}
	
	public static long daysBetween(Calendar startDate, Calendar endDate) {  
		Calendar date = (Calendar) startDate.clone();  
		long daysBetween = 0;  
		while (date.before(endDate)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}  
		return daysBetween;  
	}  		
	
	public String formatDate(Calendar calendar) {
		SimpleDateFormat format = new SimpleDateFormat(siteProperties.getDateFormat());
		return format.format(calendar.getTime());
	}
	
    public SitePropertiesService getSiteProperties() {
		return siteProperties;
	}

	public void setSiteProperties(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
	}
}
