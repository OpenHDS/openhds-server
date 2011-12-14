package org.openhds.community.service.impl;

import java.util.List;

import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.dhis2.ns.schema.dxf2.DxfDocument.Dxf;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;
import org.openhds.community.beans.OrgUnit;
import org.openhds.community.builder.OrgUnitBuilder;
import org.openhds.community.service.DHISService;

public class DHISServiceImpl implements DHISService {
	
	OrgUnitBuilder orgUnitBuilder;
	
	DxfDocument dxfDocument;
	Metadata metadata;
	Dxf dxf;
	
	public void createDxfDocument() {
		
		orgUnitBuilder = new OrgUnitBuilder();
		
		dxfDocument = DxfDocument.Factory.newInstance();
		dxf = dxfDocument.addNewDxf();
		metadata = dxf.addNewMetadata();
	}

	public void createOrgUnit(OrgUnit orgUnit, List<String> levels) {
		orgUnitBuilder.buildOrgUnit(metadata, orgUnit, levels);
	}
	
	public DxfDocument getDxfDocument() {
		return dxfDocument;
	}

}
