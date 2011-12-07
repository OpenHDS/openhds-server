package org.openhds.ddi.builder;

import java.util.List;
import java.util.Vector;
import edu.umich.icpsr.ddi.CatValuType;
import edu.umich.icpsr.ddi.CatgryType;
import edu.umich.icpsr.ddi.DataDscrType;
import edu.umich.icpsr.ddi.LablType;
import edu.umich.icpsr.ddi.TxtType;
import edu.umich.icpsr.ddi.VarFormatType;
import edu.umich.icpsr.ddi.VarType;

/**
 * 
 * Builds the Data Description section of the DDI Schema.
 * 
 * @author Brian
 */

public class DataDescriptionBuilder {
	
	public DataDescriptionBuilder() { }
	
	@SuppressWarnings({"unchecked" })
	public void buildDataDescription(DataDscrType dataDscr, String tableName,
			String fieldName, String type, String iteration, String desc, List<?> enumList) {
		
		VarType variable = dataDscr.addNewVar();
		variable.setID(tableName + iteration);
		variable.setName(tableName + "_V" + iteration);
		Vector files = new Vector();
		files.add(tableName);
		variable.setFiles(files);
		
		TxtType text = variable.addNewTxt();
		text.newCursor().setTextValue(desc);
		
		LablType label = variable.addNewLabl();
		label.newCursor().setTextValue(fieldName);
				
		if (enumList != null) {		
			int count = 0;
			for (Object item : enumList) {	
				CatgryType cat = variable.addNewCatgry();
				CatValuType catValue = cat.addNewCatValu();
				LablType catLabel = cat.addNewLabl();
				
				catValue.newCursor().setTextValue(Integer.toString(count));
				catLabel.newCursor().setTextValue(item.toString());
				count++;
			}
		}
		
		VarFormatType format = variable.addNewVarFormat();
		if (type.toLowerCase().contains("integer") || type.toLowerCase().contains("float") ||
				type.toLowerCase().contains("double") || type.toLowerCase().contains("character"))
			format.setType(VarFormatType.Type.NUMERIC);
		else 
			format.setType(VarFormatType.Type.CHARACTER);
		format.setFormatname(type);		
	}
}
