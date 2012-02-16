package org.openhds.community.builder;

import org.apache.xmlbeans.XmlAnySimpleType;
import org.dhis2.ns.schema.dxf2.DataValues;
import org.dhis2.ns.schema.dxf2.DataValueDocument.DataValue;

/**
 * Builds the DataValue section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class DataValueBuilder {

	public void buildDataValue(DataValues dataValues, int dataElementId, int periodId, int sourceId, String value) {
		
		DataValue dataValue = dataValues.addNewDataValue();
		
		dataValue.setDataElement(dataElementId);
		dataValue.setPeriod(periodId);
		dataValue.setSource(sourceId);
		dataValue.setValue(XmlAnySimpleType.Factory.newValue(value));
		dataValue.setStoredBy(null);
		dataValue.setTimeStamp(null);
		dataValue.setComment(null);
		dataValue.setCategoryOptionCombo(1);
	}
}
