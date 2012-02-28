package org.openhds.community.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.openhds.community.beans.OrgUnitBean;

public interface DHISService {
	
	void createDxfDocument(); 
	
	void createOrgUnit(OrgUnitBean orgUnit, List<String> levels, Calendar startDate, Calendar endDate);
		
	void createDataElement(String name, String description, String type, String number);
	
	void createDataSet(String name, String period, int id, List<Integer> elementRefs);
	
	void createDataSetMembers(int dataSetId, int low, int high);
	
	void createCategories(Map<String, String> options, String name, String description, int id);
	
	void createDefaultCategories();
	
	void createPeriod(String type, Calendar startDate, Calendar endDate, int id);
	
	void createDataValues(int dataElementIndex, int periodIndex, int sourceIndex, String value);
	
	DxfDocument getDxfDocument(); 

}
