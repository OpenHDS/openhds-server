package org.openhds.community.builder;

import org.dhis2.ns.schema.dxf2.DataElementDefinitionDocument.DataElementDefinition;
import org.dhis2.ns.schema.dxf2.DataElementDocument.DataElement;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;

/**
 * Builds the DataElement section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class DataElementBuilder {
	
	public void buildDataElement(Metadata metadata, String name, String description, String type, String number) {
		
		DataElementDefinition dataElementDef = metadata.addNewDataElementDefinition();			
		DataElement dataElement = dataElementDef.addNewDataElement();
		
		dataElement.setName(name);
		dataElement.setAlternativeName(name);
		dataElement.setCode(name);
		dataElement.setDescription(description);
		dataElement.setType(type);
		dataElement.setId(Integer.parseInt(number));
	}
}
