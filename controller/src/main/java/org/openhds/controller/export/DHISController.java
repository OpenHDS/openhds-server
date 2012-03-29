package org.openhds.controller.export;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openhds.domain.annotations.Description;
import org.openhds.community.beans.OrgUnitBean;
import org.openhds.community.builder.OrgUnitBuilder;
import org.openhds.community.service.DHISService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;
import org.openhds.controller.beans.DHISDocumentBean;
import org.openhds.controller.beans.RecordGroup;
import org.openhds.controller.beans.Period;
import org.openhds.controller.beans.RecordItem;
import org.openhds.controller.service.DeathService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.PregnancyService;
import org.openhds.domain.extensions.ValueConstraintService;

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
	DeathService deathService;
	IndividualService individualService;
	PregnancyService pregnancyService;
	ValueConstraintService valueConstraintService;
	
	Period period;
	int periodTotal;
		
	public DHISController(GenericDao genericDao, LocationHierarchyService locationService, DHISService dhisService, 
			DHISDocumentBean dhisDocumentBean, ValueConstraintService valueConstraintService, DeathService deathService,
			IndividualService individualService, PregnancyService pregnancyService) {
		this.genericDao = genericDao;
		this.locationService = locationService;
		this.dhisService = dhisService;
		this.dhisDocumentBean = dhisDocumentBean;
		this.valueConstraintService = valueConstraintService;
		this.deathService = deathService;
		this.individualService = individualService;
		this.pregnancyService = pregnancyService;
	}
	
	public String buildDHISDocument() throws ClassNotFoundException, ParseException {
			
		dhisService.createDxfDocument();
		
		List<String> hierarchyIds = locationService.getValidLocationsInHierarchy(dhisDocumentBean.getHierarchyExtId());
		setupVariables(hierarchyIds);
		
		individualService.setPopulationForAgeGroupsByLocation(period.findGroupsByName("Population"), hierarchyIds);  
		
		for (RecordGroup group : period.findGroupsByName("Death")) 
			deathService.setDeathsForAgeGroupsByLocation(group, hierarchyIds);
		for (RecordGroup group : period.findGroupsByName("PregnancyOutcome")) 
			pregnancyService.setPregnancyOutcomesByLocation(group, hierarchyIds);	
		
		buildOrgUnit();
		buildCategoryCombos();
		buildDataElement();
		buildPeriod();
		buildDataValues();
		
		return dhisService.getDxfDocument().toString();
	}
		
	private void setupVariables(List<String> hierarchyIds) {
		String periodVal = dhisDocumentBean.getPeriod();
		
		Calendar startDate = null;
		Calendar endDate = null;
		
		if (!periodVal.equals("Yearly")) {
			startDate = (Calendar) dhisDocumentBean.getsDate().clone();
			endDate = (Calendar) dhisDocumentBean.geteDate().clone();
		}
		else {
			startDate = Calendar.getInstance();
			startDate.set(Calendar.DATE, 1);
			startDate.set(Calendar.MONTH, 0);
			startDate.set(Calendar.YEAR, Integer.parseInt(dhisDocumentBean.getStartYear()));
			
			endDate = Calendar.getInstance();
			endDate.set(Calendar.DATE, 2);
			endDate.set(Calendar.MONTH, 0);
			endDate.set(Calendar.YEAR, Integer.parseInt(dhisDocumentBean.getEndYear()));
		}
						
		if (startDate.after(endDate)) 
			period = new Period(periodVal, endDate, startDate, hierarchyIds);
		else
			period = new Period(periodVal, startDate, endDate, hierarchyIds);
	}
	
	private void buildCategoryCombos() {
		dhisService.createDefaultCategories();
	}
	
	private void buildOrgUnit() {
		LocationHierarchy root = genericDao.findByProperty(LocationHierarchy.class, "extId", dhisDocumentBean.getHierarchyExtId());

		String id = root.getUuid();
		String level = root.getLevel().getName();
		String code = root.getExtId();
		String name = root.getName();
				
		OrgUnitBean orgUnit = new OrgUnitBean(id, name, code, level, 1);
		buildOrgUnitStructure(root, orgUnit, 2);	
		
		List<String> levels = new ArrayList<String>();
		List<LocationHierarchyLevel> locHLevels = locationService.getAllLevels();
		for (int i = locHLevels.size()-1; i >= 0; i--) {
			levels.add(locHLevels.get(i).getName());
		}
		
		dhisService.createOrgUnit(orgUnit, levels, dhisDocumentBean.getsDate(), dhisDocumentBean.geteDate());
	}
	
	private void buildDataElement() throws ClassNotFoundException {
				
		// just need to get age groups to make data elements
		List<RecordItem> deaths = period.findGroupsByName("Death").get(0).getRecord().getItems().get(dhisDocumentBean.getHierarchyExtId());
		List<RecordItem> populations = period.findGroupsByName("Population").get(0).getRecord().getItems().get(dhisDocumentBean.getHierarchyExtId());
		List<RecordItem> pregOutcomes = period.findGroupsByName("PregnancyOutcome").get(0).getRecord().getItems().get(dhisDocumentBean.getHierarchyExtId());
		List<Integer> elementRefs = new ArrayList<Integer>();
		
		int counter = 1;
		for (RecordItem record : deaths) {
			String maleCount = Integer.toString(counter);
			String femaleCount = Integer.toString(counter+1);
			dhisService.createDataElement("Male Deaths : " + record.getAgeGroupName(), "", "int", maleCount);
			dhisService.createDataElement("Female Deaths : " + record.getAgeGroupName(), "", "int", femaleCount);
			counter += 2;
		}
		dhisService.createDataSet("Mortality Data Set", period.getType(), 1, elementRefs);
		dhisService.createDataSetMembers(1, 1, 52);
		
		for (RecordItem record : populations) {
			String maleCount = Integer.toString(counter);
			String femaleCount = Integer.toString(counter+1);
			dhisService.createDataElement("Male : " + record.getAgeGroupName(), "", "int", maleCount);
			dhisService.createDataElement("Female : " + record.getAgeGroupName(), "", "int", femaleCount);
			counter += 2;
		}
		
		dhisService.createDataSet("Population Data Set", period.getType(), 2, elementRefs);
		dhisService.createDataSetMembers(2, 53, 104);
		
		for (RecordItem record : pregOutcomes) {
			
			if (record.getAgeGroupName().equals("Live Birth")) {
				String maleCount = Integer.toString(counter);
				String femaleCount = Integer.toString(counter+1);
				dhisService.createDataElement("Male : " + record.getAgeGroupName(), "", "int", maleCount);
				dhisService.createDataElement("Female : " + record.getAgeGroupName(), "", "int", femaleCount);
				elementRefs.add(counter);
				elementRefs.add(counter+1);
				counter += 2;
			}
			else {
				dhisService.createDataElement(record.getAgeGroupName(), "", "int", Integer.toString(counter));
				elementRefs.add(counter);
				counter++;
			}
		}
		dhisService.createDataSet("Pregnancy Outcome Data Set", period.getType(), 3, elementRefs);
		dhisService.createDataSetMembers(3, 105, 109);
	}
		
	private void buildPeriod() {
		
		int counter = 1;
				
		counter++;
		List<RecordGroup> groups = period.findGroupsByName("Death");
		for (RecordGroup group : groups) {					
			dhisService.createPeriod(period.getType(), group.getStart(), group.getEnd(), counter);
			counter++;
		}
		periodTotal = counter;
	}
	
	private void buildDataValues() {
		
		int dataElementIndex = 1;
		
		Map<Integer, String> hierarchyMap = OrgUnitBuilder.getHierarchyCodes();
		Set<Integer> set = hierarchyMap.keySet();

		for (Integer sourceIndex : set) {
			List<RecordGroup> groupList = period.findGroupsByName("Death");
			
			int periodIndex = 1;
			for (RecordGroup group : groupList) {
								
				List<RecordItem> items = group.getRecord().getItems().get(hierarchyMap.get(sourceIndex));
				if (items != null) {	
					for (RecordItem item : items) {	
						if (item.getMaleCount() > 0)
							dhisService.createDataValues(dataElementIndex, periodIndex, sourceIndex, Integer.toString(item.getMaleCount()));
						if (item.getFemaleCount() > 0)
							dhisService.createDataValues(dataElementIndex+1, periodIndex, sourceIndex, Integer.toString(item.getFemaleCount()));		
						dataElementIndex += 2;
					}
				}
				periodIndex++;
				dataElementIndex = 1;
			}
		}

		dataElementIndex = 53;
		for (Integer sourceIndex : set) {
			List<RecordGroup> groupList = period.findGroupsByName("Population");
			
			int periodIndex = 1;
			for (RecordGroup group : groupList) {
				
				List<RecordItem> items = group.getRecord().getItems().get(hierarchyMap.get(sourceIndex));
				if (items != null) {	
					for (RecordItem item : items) {
						if (item.getMaleCount() > 0) 
							dhisService.createDataValues(dataElementIndex, periodIndex, sourceIndex, Integer.toString(item.getMaleCount()));
						if (item.getFemaleCount() > 0) 
							dhisService.createDataValues(dataElementIndex+1, periodIndex, sourceIndex, Integer.toString(item.getFemaleCount()));
						dataElementIndex += 2;
					}
				}
				periodIndex++;
				dataElementIndex = 53;
			}
		}
		
		dataElementIndex = 105;
		for (Integer sourceIndex : set) {
			List<RecordGroup> groupList = period.findGroupsByName("PregnancyOutcome");
			
			int periodIndex = 1;
			for (RecordGroup group : groupList) {
				
				List<RecordItem> items = group.getRecord().getItems().get(hierarchyMap.get(sourceIndex));
				if (items != null) {	
					for (RecordItem item : items) {	
						if (item.getAgeGroupName().contains("Live Birth")) {
							if (item.getMaleCount() > 0)
								dhisService.createDataValues(dataElementIndex, periodIndex, sourceIndex, Integer.toString(item.getMaleCount()));
							if (item.getFemaleCount() > 0)
								dhisService.createDataValues(dataElementIndex+1, periodIndex, sourceIndex, Integer.toString(item.getFemaleCount()));		
							dataElementIndex += 2;
						}
						else {
							if (item.getMaleCount() > 0)
								dhisService.createDataValues(dataElementIndex, periodIndex, sourceIndex, Integer.toString(item.getMaleCount()));
							dataElementIndex++;
						}
					}
				}
				periodIndex++;
				dataElementIndex = 105;
			}
		}
	}
	
	/**
	 * A structure is needed in order to build the DHIS OrgUnit element. Since the OpenHDS
	 * location hierarchy only has parent references and the org unit has children references,
	 * it must be converted over.
	 */
    private void buildOrgUnitStructure(LocationHierarchy item, OrgUnitBean unit, int index) {
    	
    	// find children of this locationHierarchy item
    	List<LocationHierarchy> hierarchyList = genericDao.findListByProperty(LocationHierarchy.class, "parent", item);
    	
    	for (LocationHierarchy hierarchyItem : hierarchyList) {
    	
	    	// build OrgUnit
	    	String id = hierarchyItem.getUuid();
			String level = hierarchyItem.getLevel().getName();
			String code = hierarchyItem.getExtId();
			String name = hierarchyItem.getName();

			OrgUnitBean orgUnit = new OrgUnitBean(id, name, code, level, index);
			unit.getChildren().add(orgUnit);
			orgUnit.setParent(unit);
			index++;
			
			buildOrgUnitStructure(hierarchyItem, orgUnit, index);
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
