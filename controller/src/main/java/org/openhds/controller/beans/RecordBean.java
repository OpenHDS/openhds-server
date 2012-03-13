package org.openhds.controller.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is used in the DHIS Export for further subdividing a RecordGroup into the
 * designated regions in the location hierarchy. It contains a map of all the 
 * location hierarchy ids, with a list of RecordItems as values. These RecordItems
 * contain the particular category that was defined in the Period. For example,
 * a map may contain the ids, [AKP: 0-28 days Male, 0-28 days Female, 29-11 months Male, ...]
 * Do not use this class directly, just supply the proper parameters to the Period 
 * and this structure will be constructed automatically.
 */
public class RecordBean {
	
	Map<String, List<RecordItem>> items;
	
	RecordBean(String[] ages, List<String> hierarchyIds) {
		items = new TreeMap<String, List<RecordItem>>();
		buildStructure(ages, hierarchyIds);
	}
	
	public void buildStructure(String[] ages, List<String> hierarchyIds) {
		
		for (String hierarchy : hierarchyIds) {
			
			List<RecordItem> list = new ArrayList<RecordItem>();
			for (String age : ages) {
				RecordItem recordItem = new RecordItem(age);
				list.add(recordItem);
			}
			items.put(hierarchy, list);
		}	
	}
		
	public void addMaleCountForLocationAndAgeGroup(String hierarchyId, int groupIndex) {
		items.get(hierarchyId).get(groupIndex).addMaleCount();
	}
	
	public void addFemaleCountForLocationAndAgeGroup(String hierarchyId, int groupIndex) {
		items.get(hierarchyId).get(groupIndex).addFemaleCount();
	}
		
	public Map<String, List<RecordItem>> getItems() {
		return items;
	}
}
