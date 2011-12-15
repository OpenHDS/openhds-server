package org.openhds.community.service;

import java.util.Calendar;
import java.util.List;
import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.openhds.community.beans.OrgUnitBean;

public interface DHISService {
	
	void createDxfDocument(); 
	
	void createOrgUnit(OrgUnitBean orgUnit, List<String> levels, Calendar startDate, Calendar endDate);
		
	void createDataElement(String name, String description, String type, String number);
	
	DxfDocument getDxfDocument(); 

}
