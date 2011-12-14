package org.openhds.community.service;

import java.util.List;
import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.openhds.community.beans.OrgUnit;

public interface DHISService {
	
	void createDxfDocument(); 
	
	void createOrgUnit(OrgUnit orgUnit, List<String> levels);
	
	DxfDocument getDxfDocument(); 

}
