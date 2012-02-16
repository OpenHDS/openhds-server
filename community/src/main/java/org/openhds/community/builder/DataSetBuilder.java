package org.openhds.community.builder;

import java.util.List;
import org.dhis2.ns.schema.dxf2.DataSetMemberDocument.DataSetMember;
import org.dhis2.ns.schema.dxf2.DataSetMembersDocument.DataSetMembers;
import org.dhis2.ns.schema.dxf2.DataSetsDocument.DataSets;
import org.dhis2.ns.schema.dxf2.DataSetsDocument.DataSets.DataSet;

/**
 * Builds the DataSet section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class DataSetBuilder {
		
	public void buildDataSet(DataSets dataSetDef, String name, String period, int id, List<Integer> elementRefs) {
		
		DataSet dataSet = dataSetDef.addNewDataSet();
		dataSet.setId(id);
		dataSet.setName(name);
		dataSet.setShortName(name);
		dataSet.setCode("");
		dataSet.setPeriodType(period);
	}
	
	public void buildDataSetMembers(DataSetMembers dataSetMembers, int dataSetIndex, int low, int high) {
		
		for (int i = low; i <= high; i++) {
			DataSetMember dataSetMember = dataSetMembers.addNewDataSetMember();
			dataSetMember.setDataSet(dataSetIndex);
			dataSetMember.setDataElement(i);
		}
	}
}
