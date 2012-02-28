package org.openhds.controller.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeathRecordGroup {
	
	List<DeathRecordBean> deaths;
	Calendar start;
	Calendar end;
	
	public DeathRecordGroup(Calendar start, Calendar end) {
		this.start = (Calendar) start.clone();
		this.end = (Calendar) end.clone();
		
		deaths = new ArrayList<DeathRecordBean>();
		initializeGroup();
	}
	
	private void initializeGroup() {
		deaths.add(new DeathRecordBean("0-28 days"));
		deaths.add(new DeathRecordBean("29-11 months"));
		deaths.add(new DeathRecordBean("12-59 months"));
		deaths.add(new DeathRecordBean("5-9 years"));
		deaths.add(new DeathRecordBean("10-19 years"));
		deaths.add(new DeathRecordBean("20-40 years"));
		deaths.add(new DeathRecordBean(">40 years"));
	}
	
	public List<DeathRecordBean> getDeaths() {
		return deaths;
	}

	public void setDeaths(List<DeathRecordBean> deaths) {
		this.deaths = deaths;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}
		
	public String toString() {
		String output = "";
		
		for (DeathRecordBean record : deaths) {
			if (record.getMaleCount() > 0 || record.getFemaleCount() > 0) {
				output += record.getAgeGroupName() + "\n";
				output += "Male : " + record.getMaleCount() + "\n";
				output += "Female : " + record.getFemaleCount() + "\n";
			}
		}
		
		return output;
	}
}
