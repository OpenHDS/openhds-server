package org.openhds.web.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.Enumerated;
import org.hibernate.metadata.ClassMetadata;
import org.openhds.dao.service.GenericDao;
import org.openhds.ddi.service.CodeBookService;
import org.openhds.ddi.service.impl.CodeBookServiceImpl;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.model.AppSettings;
import org.openhds.web.beans.StudyDocumentBean;

/**
 * References:
 * 
 * XML Schema Tag Library for Ddi 2.1
 * http://www.ddialliance.org/sites/default/files/version2-1-all.html
 * 
 * Tree Structure
 * http://www.ddialliance.org/sites/default/files/ddi-lite.html
 * 
 * XMLBeans 2.4 Documentaton
 * http://xmlbeans.apache.org/docs/2.4.0/reference/index.html
 * 
 * @author Brian
 */

public class DDIController {
	
	GenericDao genericDao;
	AppSettings appSettings;
	StudyDocumentBean studyDocument;

	public DDIController() {	}
		
	public String buildDDIDocument() throws ClassNotFoundException {
			
		// a mapping of table names with their associated ClassMetaData
		Map<?, ?> mapping = genericDao.getClassMetaData();
				
		// all table names
		Object[] keySet = mapping.keySet().toArray();
				
		Properties props = new Properties();
		props.put("studyName", studyDocument.getStudyName());
		props.put("studyId", studyDocument.getStudyId());
		props.put("studyStartDate", studyDocument.getStudyStartDate().toString());
		props.put("affiliation", studyDocument.getAffiliation());
		props.put("author", studyDocument.getAuthor());
		props.put("email", studyDocument.getEmail());
		props.put("version", appSettings.getVersionNumber());
		props.put("notes", studyDocument.getNotes());
		props.put("nation", studyDocument.getNationAbrv());
		props.put("studyEndDate", studyDocument.getStudyEndDate().toString());
		
		CodeBookService cbService = new CodeBookServiceImpl(mapping, props);
		cbService.createCodeBook();
		cbService.buildDocumentDescription();
		cbService.buildStudyDescription();
		
		int count = 0;
		
		// iterate through all tables
		for (int i = 0; i < keySet.length; i++) {
			String tableName = (String) keySet[i];
			
			// using reflection to get the entity 
			Class<?> clazz = Class.forName(tableName);	
			
			String classDesc = getDescription(clazz.getAnnotations());
			
			// hibernate metadata associated with the table
			ClassMetadata data = (ClassMetadata) mapping.get(tableName);
			
			// all column names of the table
			String propertyNames[] = data.getPropertyNames();
			
			// list of all fields for the entity
			ArrayList<Field> fieldsList = buildFieldList(clazz);
	
			// must iterate through all columns
			for (int j = 0; j < propertyNames.length; j++) {
				count++;
				
				// the column name
				String fieldName = propertyNames[j];
				
				// annotations from the fieldName
				Annotation[] annotations = getAnnotationMatch(fieldName, fieldsList);
				
				// enumerated list 
				List<?> enumList = getEnumeratedValues(fieldName, fieldsList);
				
				// description from annotation
				String desc = getDescription(annotations);
				
				String type;
				if (enumList != null)
					type = enumList.get(0).getClass().getName();
				else
					type = data.getPropertyType(fieldName).getName();
					
				String iteration = Integer.toString(count);
				cbService.buildFileDescription(tableName, classDesc, propertyNames, "jdbc:mysql://localhost/openhds");
				cbService.buildDataDescription(tableName, fieldName, type, iteration, desc, enumList);
			}
		}
		return cbService.getDoc().toString();
	}
	
	/**
	 * Returns a list of all enumerated values for the 
	 * specified field name identified by the @Enumerated annotation.
	 */
	private List<?> getEnumeratedValues(String fieldName, ArrayList<Field> fieldsList) {
			
		// must iterate through the fields of the entity class
		// in order to obtain constraint level data
		for (int i = 0; i < fieldsList.size(); i++) {						
		
			// if the field name and column name match then get the annotations
			if (fieldsList.get(i).getName().equals(fieldName)) {			
				Annotation[] annotations = fieldsList.get(i).getDeclaredAnnotations();	
		
				// must iterate through the annotations to filter the ones we need
				for (int j = 0; j < annotations.length; j++) {
					
					if (annotations[j] instanceof Enumerated) {
						Class<?> anoClazz = fieldsList.get(i).getType();
						return Arrays.asList(anoClazz.getEnumConstants());
					}	
				}	
			}
			
		}
		return null;
	}
	
	/**
	 * Returns a list of annotations for the specified field name.
	 */
	private Annotation[] getAnnotationMatch(String fieldName, ArrayList<Field> fieldsList) {
		
		// must iterate through the fields of the entity class
		// in order to obtain the annotations
		for (int i = 0; i < fieldsList.size(); i++) {						
		
			// if the field name and column name match then get the annotations
			if (fieldsList.get(i).getName().equals(fieldName)) {			
				Annotation[] annotations = fieldsList.get(i).getDeclaredAnnotations();	
				return annotations;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of all fields for the given entity class using reflection.
	 */
	private ArrayList<Field> buildFieldList(Class<?> clazz) {
		
		ArrayList<Field> fieldsList = new ArrayList<Field>();
		
		for (Field field : clazz.getDeclaredFields()) {
			fieldsList.add(field);
		}

		if (clazz.getSuperclass() != null) {	
			Class<?> superClazz1 = clazz.getSuperclass();
			Class<?> superClazz2 = superClazz1.getSuperclass();
			for (Field field : superClazz1.getDeclaredFields()) {
				fieldsList.add(field);
			}
			if (superClazz2 != null) {
				for (Field field : superClazz2.getDeclaredFields()) {
					fieldsList.add(field);
				}
			}
		}
		return fieldsList;
	}
	
	/**
	 * Returns the description of a field identified by the
	 * @Description annotation.
	 */
	private String getDescription(Annotation[] annotations) {
		
		// must iterate through the annotations to filter the ones we need
		for (int j = 0; j < annotations.length; j++) {
			
			if (annotations[j] instanceof Description) {
				Description descAno = (Description) annotations[j];
				return descAno.description();
			}	
		}	
		return null;
	}

	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public AppSettings getAppSettings() {
		return appSettings;
	}

	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}
	
	public StudyDocumentBean getStudyDocument() {
		return studyDocument;
	}

	public void setStudyDocument(StudyDocumentBean studyDocument) {
		this.studyDocument = studyDocument;
	}
}
