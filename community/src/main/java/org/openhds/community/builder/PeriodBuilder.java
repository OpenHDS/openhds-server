package org.openhds.community.builder;

import java.util.Calendar;
import org.dhis2.ns.schema.dxf2.PeriodDocument.Period;
import org.dhis2.ns.schema.dxf2.PeriodsDocument.Periods;

/**
 * Builds the Period section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class PeriodBuilder {
	
	public void buildPeriod(Periods periods, int id, String type, Calendar startDate, Calendar endDate) {
		
		Period period = periods.addNewPeriod();
	
		period.setId(id);
		period.setPeriodType(type);
		
		// this parsing is necessary because the Calendar toString
		// returns an invalid format of yyyy-mm-dd-tt-tttt
		// we need to have just yyyy-mm-dd. 
		// dhis will not accept if it's not
		String start = startDate.get(Calendar.YEAR) + "-" + getFormattedStartDateString(startDate);
		String end = endDate.get(Calendar.YEAR) + "-" + getFormattedEndDateString(endDate);

		period.setStartDate(start);
		period.setEndDate(end);
	}
	
	private String getFormattedStartDateString(Calendar startDate) {
		String startStringMonth = "";
		int sMonth = startDate.get(Calendar.MONTH) + 1;
		if (sMonth < 10) {
			startStringMonth += "0";
			startStringMonth = startStringMonth.concat(Integer.toString(sMonth));
		}
		else 
			startStringMonth = Integer.toString(sMonth);
		
		String startStringDay = "";
		int sDay = startDate.get(Calendar.DATE);
		if (sDay < 10) {
			startStringDay += "0";
			startStringDay = startStringDay.concat(Integer.toString(sDay));
		}
		else
			startStringDay = Integer.toString(sDay);
		
		return startStringMonth + "-" + startStringDay;
	}
	
	private String getFormattedEndDateString(Calendar endDate) {
		String endStringMonth = "";
		int eMonth = endDate.get(Calendar.MONTH) + 1;
		if (eMonth < 10) {
			endStringMonth += "0";
			endStringMonth = endStringMonth.concat(Integer.toString(eMonth));
		}
		else 
			endStringMonth = Integer.toString(eMonth);
		
		String endStringDay = "";
		int eDay = endDate.get(Calendar.DATE);
		if (eDay < 10) {
			endStringDay += "0";
			endStringDay = endStringDay.concat(Integer.toString(eDay));
		}
		else
			endStringDay = Integer.toString(eDay);
		
		return endStringMonth + "-" + endStringDay;
	}

}
