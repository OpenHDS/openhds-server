package org.openhds.community.builder;

import java.util.Calendar;

import org.dhis2.ns.schema.dxf2.DataElementDocument.DataElement;
import org.dhis2.ns.schema.dxf2.DataElementsDocument.DataElements;

/**
 * Builds the DataElement section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class DataElementBuilder {
	
	public void buildDataElement(DataElements dataElementDef, String name, String description, String type, String number) {
			
		DataElement dataElement = dataElementDef.addNewDataElement();
		
		dataElement.setUuid("");
		dataElement.setName(name);
		dataElement.setAlternativeName(name);
		dataElement.setShortName(name);
		dataElement.setCode("");
		dataElement.setDescription(description);
		dataElement.setActive("true");
		dataElement.setType(type);
		dataElement.setAggregationOperator("sum");
		dataElement.setCategoryCombo(1);
		dataElement.setLastUpdated(Calendar.getInstance());
		dataElement.setId(Integer.parseInt(number));
	}
}
