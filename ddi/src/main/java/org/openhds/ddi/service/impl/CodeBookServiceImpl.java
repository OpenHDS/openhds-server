package org.openhds.ddi.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openhds.ddi.builder.DataDescriptionBuilder;
import org.openhds.ddi.builder.DocumentDescriptionBuilder;
import org.openhds.ddi.builder.FileDescriptionBuilder;
import org.openhds.ddi.builder.StudyDescriptionBuilder;
import org.openhds.ddi.service.CodeBookService;
import edu.umich.icpsr.ddi.CodeBookDocument;
import edu.umich.icpsr.ddi.DataDscrType;
import edu.umich.icpsr.ddi.CodeBookDocument.CodeBook;

public class CodeBookServiceImpl implements CodeBookService {

	DocumentDescriptionBuilder docDscrBuilder;
	StudyDescriptionBuilder studyDscrBuilder; 
	DataDescriptionBuilder dataDscrBuilder;
	FileDescriptionBuilder fileDscrBuilder;
	
	CodeBookDocument doc;
	CodeBook codebook;
	
	Map<?, ?> metadata;
	Properties properties;
	
	public void createCodeBook() {
				
		docDscrBuilder = new DocumentDescriptionBuilder();
		studyDscrBuilder = new StudyDescriptionBuilder();
		dataDscrBuilder = new DataDescriptionBuilder();
		fileDscrBuilder = new FileDescriptionBuilder();
		
		doc = CodeBookDocument.Factory.newInstance();
		codebook = doc.addNewCodeBook();
		codebook.setID(properties.getProperty("studyId").toLowerCase());
		codebook.setVersion("2.1");
	}
	
	public void buildDocumentDescription() {
		
		docDscrBuilder.buildDocumentDescription(codebook, properties.getProperty("studyName"), properties.getProperty("studyId"),
				properties.getProperty("studyStartDate"), properties.getProperty("affiliation"), properties.getProperty("author"), 
				properties.getProperty("email"), properties.getProperty("version"), properties.getProperty("notes"));
		
	}
	
	public void buildStudyDescription() {
		
		// all table names
		Object[] keySet = metadata.keySet().toArray();
		
		studyDscrBuilder.buildStudyDescription(codebook, properties.getProperty("studyName"), properties.getProperty("studyId"), 
				properties.getProperty("affiliation"), properties.getProperty("author"), properties.getProperty("email"), 
				properties.getProperty("nation"),properties.getProperty("studyStartDate"), properties.getProperty("studyEndDate"), 
				Integer.toString(keySet.length), properties.getProperty("notes"));
	
	}
	
	public void buildFileDescription(String tableName, String classDesc, String[] propertyNames, String uri) {
		fileDscrBuilder.buildFileDescription(codebook, tableName, classDesc, propertyNames, uri);
	}
	
	public void buildDataDescription(String tableName,
			String fieldName, String type, String iteration, String desc, List<?> enumList) {
		
		DataDscrType dataDscr = codebook.addNewDataDscr();
		dataDscrBuilder.buildDataDescription(dataDscr, tableName, fieldName, type, iteration, desc, enumList);	
	}
	
	public CodeBookDocument getDoc() {
		return doc;
	}
	
	public Map<?, ?> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<?, ?> metadata) {
		this.metadata = metadata;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
