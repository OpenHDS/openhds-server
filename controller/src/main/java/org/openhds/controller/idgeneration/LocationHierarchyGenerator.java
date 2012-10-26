package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.LocationHierarchy;

/**
 * @author Brian
 * 
 * The Generator for Location Hierarchy Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class LocationHierarchyGenerator extends Generator<LocationHierarchy> {

	@Override
	public String generateId(LocationHierarchy location) throws ConstraintViolations  {
		StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
				
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (key.equals(IdGeneratedFields.CHILD_LOC_NAME.toString())) {
				String name = location.getName();
				
				if (name.length() >= filter) {
				
					String locNameFirstKey = itr.next();
					Integer locNameFirstFilter = fields.get(locNameFirstKey);
					
					String locNameLastKey = itr.next();
					Integer locNameLastFilter = fields.get(locNameLastKey);
					
					try {
					
						if (filter > 0 && name.length() >= filter) {			
								
							// break down the location name into its parts
							String[] parts = name.split(" ");
							
							// form id using first and last filters
							if (parts.length > 1) {
								
								String locNameFirst = parts[0];
								String locNameLast = parts[1];
								
								sb.append(formatProperString(locNameFirst, locNameFirstFilter));
								sb.append(formatProperString(locNameLast, locNameLastFilter));
							}
							else
								sb.append(formatProperString(name, filter));
						}
						else if (filter == 0 || name.length() < filter)
							sb.append(formatProperString(name, name.length()));
					} catch (Exception e) {
						throw new ConstraintViolations("An error occurred while attempting to generate " +
								"the id on the field specified as '" + name + "'");
					}
				}
				else
					throw new ConstraintViolations("Unable to generate the id. Make sure the Child Location Name field is of the required length " +
							"specified in the id configuration.");
			}
		}
	
		if (scheme.isCheckDigit()) 
			sb.append(generateCheckCharacter(sb.toString()));
		
		if (!checkValidId(sb.toString()))
			throw new ConstraintViolations("An id was generated that already exists in the Location Hierarchy.");
		
		validateIdLength(sb.toString(), scheme);
		
		return sb.toString();
	}
	
	private boolean checkValidId(String extId) {
		LocationHierarchy item = genericDao.findByProperty(LocationHierarchy.class, "extId", extId);
		if (item != null)
			return false;
		return true;
	}

	// not applicable for location hierarchy
	@Override
	public String buildNumberWithBound(LocationHierarchy entityItem, IdScheme scheme) throws ConstraintViolations {
		return null;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("LocationHierarchy"));
		return resource.getIdScheme().get(index);
	}
}
