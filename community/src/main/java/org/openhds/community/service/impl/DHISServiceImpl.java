package org.openhds.community.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.dhis2.ns.schema.dxf2.DxfDocument.Dxf;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;
import org.openhds.community.beans.OrgUnitBean;
import org.openhds.community.builder.CategoryBuilder;
import org.openhds.community.builder.DataElementBuilder;
import org.openhds.community.builder.OrgUnitBuilder;
import org.openhds.community.service.DHISService;

public class DHISServiceImpl implements DHISService {
	
	OrgUnitBuilder orgUnitBuilder;
	DataElementBuilder dataElementBuilder;
	CategoryBuilder categoryBuilder;
	
	DxfDocument dxfDocument;
	Metadata metadata;
	
	public void createDxfDocument() {
		
		orgUnitBuilder = new OrgUnitBuilder();
		dataElementBuilder = new DataElementBuilder();
		categoryBuilder = new CategoryBuilder();
		
		dxfDocument = DxfDocument.Factory.newInstance();
		Dxf dxf = dxfDocument.addNewDxf();
		metadata = dxf.addNewMetadata();
	}

	public void createOrgUnit(OrgUnitBean orgUnit, List<String> levels, Calendar startDate, Calendar endDate) {
		orgUnitBuilder.buildOrgUnit(metadata, orgUnit, levels, startDate, endDate);
	}
	
	public void createDataElement(String name, String description, String type, String number) {
		dataElementBuilder.buildDataElement(metadata, name, description, type, number);
	}
	
	public void createCategories(Map<String, String> options, String name, String description, int id) {
		categoryBuilder.buildCategory(metadata, options, name, description, id);
	}
	
	public DxfDocument getDxfDocument() {
		return dxfDocument;
	}
}
