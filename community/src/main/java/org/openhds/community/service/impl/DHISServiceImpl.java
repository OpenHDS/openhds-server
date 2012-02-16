package org.openhds.community.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.dhis2.ns.schema.dxf2.DataSetsDocument.DataSets;
import org.dhis2.ns.schema.dxf2.DataValues;
import org.dhis2.ns.schema.dxf2.DxfDocument;
import org.dhis2.ns.schema.dxf2.DataElementsDocument.DataElements;
import org.dhis2.ns.schema.dxf2.DxfDocument.Dxf;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;
import org.dhis2.ns.schema.dxf2.PeriodsDocument.Periods;
import org.openhds.community.beans.OrgUnitBean;
import org.openhds.community.builder.CategoryBuilder;
import org.openhds.community.builder.DataElementBuilder;
import org.openhds.community.builder.DataSetBuilder;
import org.openhds.community.builder.DataValueBuilder;
import org.openhds.community.builder.OrgUnitBuilder;
import org.openhds.community.builder.PeriodBuilder;
import org.openhds.community.service.DHISService;
import org.dhis2.ns.schema.dxf2.DataSetMembersDocument.DataSetMembers;

public class DHISServiceImpl implements DHISService {
	
	OrgUnitBuilder orgUnitBuilder;
	DataElementBuilder dataElementBuilder;
	CategoryBuilder categoryBuilder;
	PeriodBuilder periodBuilder;
	DataValueBuilder dataValueBuilder;
	DataSetBuilder dataSetBuilder;
	
	DxfDocument dxfDocument;
	Dxf dxf;
	Metadata metadata;
	Periods periods;
	DataValues dataValues;
	DataElements dataElements;
	DataSets dataSet;
	DataSetMembers dataSetMembers;
	
	public void createDxfDocument() {
		
		orgUnitBuilder = new OrgUnitBuilder();
		dataElementBuilder = new DataElementBuilder();
		categoryBuilder = new CategoryBuilder();
		periodBuilder = new PeriodBuilder();
		dataValueBuilder = new DataValueBuilder();
		dataSetBuilder = new DataSetBuilder();
		
		dxfDocument = DxfDocument.Factory.newInstance();
		dxf = dxfDocument.addNewDxf();
		metadata = dxf.addNewMetadata();

		dataElements = metadata.addNewDataElements();			
		dataSet = metadata.addNewDataSets();
		dataSetMembers = metadata.addNewDataSetMembers();
		periods = metadata.addNewPeriods();
		dataValues = dxf.addNewDataValues();
	}

	public void createOrgUnit(OrgUnitBean orgUnit, List<String> levels, Calendar startDate, Calendar endDate) {
		orgUnitBuilder.buildOrgUnit(metadata, orgUnit, levels, startDate, endDate);
	}
	
	public void createDataElement(String name, String description, String type, String number) {
		dataElementBuilder.buildDataElement(dataElements, name, description, type, number);
	}
	
	public void createDataSet(String name, String period, int id, List<Integer> elementRefs) {
		dataSetBuilder.buildDataSet(dataSet, name, period, id, elementRefs);
	}
	
	public void createDataSetMembers(int dataSetId, int low, int high) {
		dataSetBuilder.buildDataSetMembers(dataSetMembers, dataSetId, low, high);
	}
	
	public void createDefaultCategories() {
		categoryBuilder.buildDefaultCategoryCombos(metadata);
		categoryBuilder.buildDefaultCategoryOptionCombos(metadata);
		categoryBuilder.buildDefaultCategoryOptions(metadata);
	}
	
	public void createCategories(Map<String, String> options, String name, String description, int id) {
		categoryBuilder.buildCategory(metadata, options, name, description, id);
	}
	
	public void createPeriod(String type, Calendar startDate, Calendar endDate, int id) {
		periodBuilder.buildPeriod(periods, id, type, startDate, endDate);
	}
	
	public void createDataValues(int dataElementIndex, int periodIndex, int sourceIndex, String value) {
		dataValueBuilder.buildDataValue(dataValues, dataElementIndex, periodIndex, sourceIndex, value);
	}
	
	public DxfDocument getDxfDocument() {
		return dxfDocument;
	}
}
