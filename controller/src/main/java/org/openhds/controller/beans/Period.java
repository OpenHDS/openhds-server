package org.openhds.controller.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openhds.domain.util.CalendarUtil;

public class Period {
	
	String type;
	Calendar startDate;
	Calendar endDate;
		
	List<DeathRecordGroup> deathGroup;
	
	public Period(String type, Calendar startDate, Calendar endDate) {
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
			
		deathGroup = new ArrayList<DeathRecordGroup>();
		initializeRecords();
	}
	
	public void initializeRecords() {
		if (type.equals("Daily")) {
			int days = (int) CalendarUtil.daysBetween(startDate, endDate);
			
			Calendar start = (Calendar) startDate.clone();
			Calendar end = (Calendar) startDate.clone();
			for (int i = 0; i < days; i++) {
				end.add(Calendar.DAY_OF_MONTH, 1);
				deathGroup.add(new DeathRecordGroup(start, end));
				start.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		else if (type.equals("Monthly")) {	
			endDate.add(Calendar.DAY_OF_MONTH, 1);	
			int months = (int)(CalendarUtil.daysBetween(startDate, endDate) / 30.44);	
	
			Calendar startIntervalCal = (Calendar) startDate.clone();
			Calendar endIntervalCal =  (Calendar) startDate.clone();
			for (int i = 0; i < months; i++) {
				endIntervalCal.add(Calendar.MONTH, 1);				
				deathGroup.add(new DeathRecordGroup(startIntervalCal, endIntervalCal));
				startIntervalCal.add(Calendar.MONTH, 1);
			}
		}
		else if (type.equals("Yearly")) {
			int years = (int)(CalendarUtil.daysBetween(startDate, endDate) / 365.25);
				
			Calendar start = (Calendar) startDate.clone();
			Calendar end = (Calendar) startDate.clone();
			for (int i = 0; i < years; i++) {
				end.add(Calendar.YEAR, 1);
				deathGroup.add(new DeathRecordGroup(start, end));
				start.add(Calendar.YEAR, 1);
			}
		}
	}
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	public List<DeathRecordGroup> getDeathGroup() {
		return deathGroup;
	}

	public void setDeathGroup(List<DeathRecordGroup> deathGroup) {
		this.deathGroup = deathGroup;
	}
}
