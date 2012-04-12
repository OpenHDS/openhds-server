package org.openhds.community.beans;

import java.util.Calendar;
import java.util.List;

/**
 * This is used in the DHIS export for subdividing an interval based on the type it is.
 * A RecordGroup consists of a name, which represents the particular group it's for.
 * For example, Death, Pregnancy Outcome, and Population are valid RecordGroup names. 
 * Start and End times represent the time this group is being considered. It contains a 
 * reference to a RecordBean which is helpful for breaking down a particular RecordGroup 
 * into which region in the hierarchy they belong. Do not use this class directly, just
 * supply the proper parameters to the Period and this structure will be constructed 
 * automatically.
 */
public class RecordGroup {
	
	String name;
	RecordBean record;
	Calendar start;
	Calendar end;
	
	public RecordGroup(String name, Calendar start, Calendar end, String[] ages, List<String> hierarchyIds) {
		this.name = name;
		this.start = (Calendar) start.clone();
		this.end = (Calendar) end.clone();
		
		record = new RecordBean(ages, hierarchyIds);
	}
		
	public RecordBean getRecord() {
		return record;
	}

	public void setRecord(RecordBean record) {
		this.record = record;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
