package org.openhds.ddi.builder;

import java.util.ArrayList;
import edu.umich.icpsr.ddi.DimensnsType;
import edu.umich.icpsr.ddi.FileDscrType;
import edu.umich.icpsr.ddi.FileNameType;
import edu.umich.icpsr.ddi.FileTxtType;
import edu.umich.icpsr.ddi.NotesType;
import edu.umich.icpsr.ddi.VarQntyType;
import edu.umich.icpsr.ddi.CodeBookDocument.CodeBook;

/**
 * 
 * Builds the File Description section of the DDI Schema.
 * 
 * @author Brian
 */

public class FileDescriptionBuilder {
	
	ArrayList<String> tablesList;
	
	public FileDescriptionBuilder() {
		tablesList = new ArrayList<String>();
	}
	
	public void buildFileDescription(CodeBook codebook, String tableName, String classDesc, String[] propertyNames, String uri) {
			
		if (!tablesList.contains(tableName)) {
			
			FileDscrType fileDscr = codebook.addNewFileDscr();
			fileDscr.setID(tableName);
			fileDscr.setURI(uri);
			
			FileTxtType fileTxt = fileDscr.addNewFileTxt();
			FileNameType fileName = fileTxt.addNewFileName();
			fileName.newCursor().setTextValue(tableName);
			
			DimensnsType dimensions = fileTxt.addNewDimensns();
			VarQntyType varQuantity = dimensions.addNewVarQnty();
			varQuantity.newCursor().setTextValue(Integer.toString(propertyNames.length));
			
			NotesType notes = fileDscr.addNewNotes();
			notes.newCursor().setTextValue(classDesc);
			
			tablesList.add(tableName);
		}
	}
}
