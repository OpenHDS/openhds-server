package org.openhds.controller.export;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openhds.community.beans.DHISDocumentBean;
import org.openhds.community.beans.OrgUnitBean;
import org.openhds.community.service.DHISService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.extensions.ExtensionConstraint;
import org.openhds.domain.extensions.ValueConstraintService;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;

/**
 * References:
 * 
 * DHIS2 Reference Guide:
 * http://dhis2.org/doc/snapshot/en/user/html/
 * 
 * @author Brian
 */

public class DHISController {
	
	GenericDao genericDao;
	LocationHierarchyService locationService;
	DHISService dhisService;
	DHISDocumentBean dhisDocumentBean;
	ValueConstraintService valueConstraintService;
	
	public DHISController(GenericDao genericDao, LocationHierarchyService locationService, DHISService dhisService, 
			DHISDocumentBean dhisDocumentBean, ValueConstraintService valueConstraintService) {
		this.genericDao = genericDao;
		this.locationService = locationService;
		this.dhisService = dhisService;
		this.dhisDocumentBean = dhisDocumentBean;
		this.valueConstraintService = valueConstraintService;
	}
	
	public String buildDHISDocument() throws ClassNotFoundException, ParseException {
		
		dhisService.createDxfDocument();
		
		buildOrgUnit();
		buildDataElement();
		
		return dhisService.getDxfDocument().toString();
	}
		
	private void buildOrgUnit() {
		
		LocationHierarchy root = locationService.getHierarchyItemHighestLevel();

		String id = root.getUuid();
		String level = root.getLevel().getName();
		String code = root.getExtId();
		String name = root.getName();
		
		OrgUnitBean orgUnit = new OrgUnitBean(id, name, code, level);
		buildOrgUnitStructure(root, orgUnit);	
		
		List<String> levels = new ArrayList<String>();
		List<LocationHierarchyLevel> locHLevels = locationService.getAllLevels();
		for (int i = locHLevels.size()-1; i >= 0; i--) {
			levels.add(locHLevels.get(i).getName());
		}
		
		dhisService.createOrgUnit(orgUnit, levels, dhisDocumentBean.getsDate(), dhisDocumentBean.geteDate());
	}
	
	private void buildDataElement() throws ClassNotFoundException {
		
		// a mapping of table names with their associated ClassMetaData
		Map<?, ?> mapping = genericDao.getClassMetaData();
				
		// all table names
		Object[] keySet = mapping.keySet().toArray();
		
		int count = 0;
		
		// iterate through all tables
		for (int i = 0; i < keySet.length; i++) {
			String tableName = (String) keySet[i];
			
			// using reflection to get the entity 
			Class<?> clazz = Class.forName(tableName);	
										
			// list of all fields for the entity
			ArrayList<Field> fieldsList = buildFieldList(clazz);
	
			// must iterate through all columns
			for (int j = 0; j < fieldsList.size(); j++) {
							
				// the column name
				String fieldName = fieldsList.get(j).getName();
				
				// annotations from the fieldName
				Annotation[] annotations = getAnnotationMatch(fieldName, fieldsList);
				
				Map<String, String> valueConstraintMap = null;
				String constraintName = "";
				for (Annotation a : annotations) {
					if (a instanceof ExtensionConstraint) {
						ExtensionConstraint ext = (ExtensionConstraint)a;
						constraintName = ext.constraint();
						valueConstraintMap = valueConstraintService.getMapForConstraint(constraintName);	
					}
				}

				// description from annotation
				String desc = getDescription(annotations);

				String type = fieldsList.get(j).getType().getName();
									
				if (valueConstraintMap != null) {
					count++;
					String iteration = Integer.toString(count);
					dhisService.createCategories(valueConstraintMap, fieldName, desc, count);
					dhisService.createDataElement(fieldName, desc, type, iteration);
				}				
			}
		}
	}
	
	/**
	 * A structure is needed in order to build the DHIS OrgUnit element. Since the OpenHDS
	 * location hierarchy only has parent references and the org unit has children references,
	 * it must be converted over.
	 */
    private void buildOrgUnitStructure(LocationHierarchy item, OrgUnitBean unit) {
    	
    	// find children of this locationHierarchy item
    	List<LocationHierarchy> hierarchyList = genericDao.findListByProperty(LocationHierarchy.class, "parent", item);
    	
    	for (LocationHierarchy hierarchyItem : hierarchyList) {
    	
	    	// build OrgUnit
	    	String id = hierarchyItem.getUuid();
			String level = hierarchyItem.getLevel().getName();
			String code = hierarchyItem.getExtId();
			String name = hierarchyItem.getName();
			
			OrgUnitBean orgUnit = new OrgUnitBean(id, name, code, level);
			unit.getChildren().add(orgUnit);
			
			buildOrgUnitStructure(hierarchyItem, orgUnit);
    	}
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
}
