package org.openhds.community.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openhds.domain.util.CalendarUtil;

/**
 * This is used in the DHIS Export for specifying an Interval for counting aggregate data.
 * A Period represents the entire Interval. Example, an Interval can be from 01-01-2011 to
 * 01-01-2012 and be set for either Yearly, Monthly, or Daily. A Period also contains a
 * reference to all hierarchy id's that are within the region of focus. A Period contains
 * many groups, which in this example will be 12 groups for the interval 01-01-2011 to 
 * 01-01-2012. The groups will automatically be calculated based on what type of interval is
 * chosen. The groups will have their own start and end dates, for example 01-01-2011 to
 * 02-01-2011 will be the first group, etc. There will be as many groups as there are 
 * categories. There are many different categories of interest for population, death, and
 * pregnancy outcome. This makes sense because you will want to count the population size
 * for individuals between 0-4 years within a particular interval, in this case every month
 * for the last 12 months. 
 */
public class Period {
	
	String type;
	Calendar startDate;
	Calendar endDate;
	List<String> hierarchyIds;
		
	String[] populationAges = {"0-4 years", "5-9 years", "10-14 years", 
			"15-19 years", "20-24 years", "25-29 years", "30-34 years", "35-39 years", 
			"40-44 years", "45-49 years", "50-54 years", "55-59 years", "60-64 years", 
			"65-69 years", "70-74 years", "75-79 years", "80-84 years", "85-89 years", 
			"90-94 years", ">95 years", "0-28 days", "29-11 months", "12-59 months", 
			"10-19 years", "20-40 years", ">40 years"};
	String[] deathAges = {"0-28 days", "29-11 months", "12-59 months",
			"5-9 years", "10-19 years", "20-40 years", ">40 years", 
			"0-4 years", "10-14 years", "15-19 years", "20-24 years", 
			"25-29 years", "30-34 years", "35-39 years", "40-44 years", 
			"45-49 years", "50-54 years", "55-59 years", "60-64 years", 
			"65-69 years", "70-74 years", "75-79 years", "80-84 years", 
			"85-89 years", "90-94 years", ">95 years"};
	
	// cross river is only tracking live births
	String[] pregnancyOutcomes = {"Live Birth", "StillBirth", "Miscarriage", "Abortion"};
	
	List<RecordGroup> groups;
	
	public Period(String type, Calendar startDate, Calendar endDate, List<String> hierarchyIds) {
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hierarchyIds = hierarchyIds;
			
		groups = new ArrayList<RecordGroup>();
		initializeRecords("Population");
		initializeRecords("Death");
		initializeRecords("PregnancyOutcome");
	}
	
	public void initializeRecords(String name) {
		if (type.equals("Daily")) {
			int days = (int) CalendarUtil.daysBetween(startDate, endDate);
			
			Calendar start = (Calendar) startDate.clone();
			Calendar end = (Calendar) startDate.clone();
						
			for (int i = 0; i < days; i++) {
				end.add(Calendar.DAY_OF_MONTH, 1);
				if (name.equals("Death"))
					groups.add(new RecordGroup(name, start, end, deathAges, hierarchyIds));
				if (name.equals("PregnancyOutcome"))
					groups.add(new RecordGroup(name, start, end, pregnancyOutcomes, hierarchyIds));
				if (name.equals("Population"))
					groups.add(new RecordGroup(name, start, end, populationAges, hierarchyIds));
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
				if (name.equals("Death"))
					groups.add(new RecordGroup(name, startIntervalCal, endIntervalCal, deathAges, hierarchyIds));
				if (name.equals("PregnancyOutcome"))
					groups.add(new RecordGroup(name, startIntervalCal, endIntervalCal, pregnancyOutcomes, hierarchyIds));
				if (name.equals("Population"))
					groups.add(new RecordGroup(name, startIntervalCal, endIntervalCal, populationAges, hierarchyIds));
				startIntervalCal.add(Calendar.MONTH, 1);
			}
		}
		else if (type.equals("Yearly")) {
			int years = (int)(CalendarUtil.daysBetween(startDate, endDate) / 365.25);
				
			Calendar start = (Calendar) startDate.clone();
			Calendar end = (Calendar) startDate.clone();
						
			for (int i = 0; i < years; i++) {
				end.add(Calendar.YEAR, 1);
				if (name.equals("Death"))
					groups.add(new RecordGroup(name, start, end, deathAges, hierarchyIds));
				if (name.equals("PregnancyOutcome"))
					groups.add(new RecordGroup(name, start, end, pregnancyOutcomes, hierarchyIds));
				if (name.equals("Population"))
					groups.add(new RecordGroup(name, start, end, populationAges, hierarchyIds));
				start.add(Calendar.YEAR, 1);
			}
		}
	}
	
	public List<RecordGroup> findGroupsByName(String name) {
		List<RecordGroup> groupsList = new ArrayList<RecordGroup>();
		for (RecordGroup group : groups) {
			if (group.getName().equals(name))
				groupsList.add(group);
		}
		return groupsList;
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
	
	public List<RecordGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<RecordGroup> groups) {
		this.groups = groups;
	}
}
