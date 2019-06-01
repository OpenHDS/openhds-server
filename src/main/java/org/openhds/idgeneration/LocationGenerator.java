package org.openhds.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Brian
 * 
 * The Generator for Location Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

@Component("locationIdGenerator")
public class LocationGenerator extends Generator<Location> {

	@Override
	public String generateId(Location location) throws ConstraintViolations  {
		StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (filter != null) {
			
				if (key.equals(IdGeneratedFields.LOCATION_HIERARCHY_ID.toString())) {
					String locId = location.getLocationLevel().getExtId();
					if (filter > 0 && locId.length() >= filter) 
						sb.append(formatProperString(locId, filter));
					else if (filter == 0 || locId.length() < filter) 
						sb.append(formatProperString(locId, locId.length()));
					else
						throw new ConstraintViolations("An error occurred while attempting to generate " +
								"the id on the field specified as '" + locId + "'");
				}
				else if (key.equals(IdGeneratedFields.LOCATION_NAME.toString())) {
					String locationName = location.getLocationName();
					
					if (locationName.length() >= filter) {
					
						if (filter > 0 && location.getLocationName().length() >= filter) 			
							sb.append(formatProperString(locationName, filter));
						else if (filter == 0 || locationName.length() < filter)
							sb.append(formatProperString(locationName, locationName.length()));
						else
							throw new ConstraintViolations("An error occurred while attempting to generate " +
									"the id on the field specified as '" + locationName + "'");
					}
					else
						throw new ConstraintViolations("Unable to generate the id. Make sure the field Location Name is of the required length " +
								"specified in the id configuration.");
				}
			}
		}
		
		extId = sb.toString();
		if (scheme.getIncrementBound() > 0) 
			sb.append(buildNumberWithBound(location, scheme));
		else
			sb.append(buildNumber(Location.class, sb.toString(), scheme.isCheckDigit()));
					
		validateIdLength(sb.toString(), scheme);
		
		return sb.toString();
	}

	@Override
	public String buildNumberWithBound(Location entityItem, IdScheme scheme) throws ConstraintViolations {
		
		Location loc = new Location();
		
		Integer size = 1;
		String result = "";
		
		// get length of the incrementBound
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();

		while (loc != null) {
			
			result = "";
			String tempExtId = extId;
						
			while (result.toString().length() < incBoundLength) {
				if (result.toString().length()+ size.toString().length() < incBoundLength)
					result += "0";
				if (result.toString().length() + size.toString().length() == incBoundLength)
					result = result.concat(size.toString());
			}
			
			if (extId == null)
				tempExtId = entityItem.getExtId().concat(result);
			else
				tempExtId = tempExtId.concat(result);
			
			if (scheme.isCheckDigit()) {
				String resultChar = generateCheckCharacter(tempExtId).toString();
				result = result.concat(resultChar);
				tempExtId = tempExtId.concat(resultChar);
			}
			
			loc = genericDao.findByProperty(Location.class, "extId", tempExtId);
			size++;
		}
		return result;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("Location"));
		return resource.getIdScheme().get(index);
	}

	@Override
	@Autowired
	@Value("${openhds.locationIdUseGenerator}")
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	public void validateId(Location loc) throws ConstraintViolations {
		IdScheme scheme = getIdScheme();
		
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		if (validateIdLength(loc.getExtId(), scheme)) {
		
			// test for proper prefix
			if (scheme.getPrefix() != "") {
				if (!loc.getExtId().substring(0, scheme.getPrefix().length()).equals(scheme.getPrefix()))	
					throw new ConstraintViolations("The Location Id doesn't contain the correct prefix " +
					"as specified in the IdScheme. The Location Id must start with " + scheme.getPrefix());
			}
			
			while(itr.hasNext()) {
				String key = itr.next();
				Integer filter = fields.get(key);
				
				if (filter != null) {
					
					if (key.equals(IdGeneratedFields.LOCATION_HIERARCHY_ID.toString())) {
						String locId = loc.getLocationLevel().getExtId();
						String sub = "";
						
						if (locId.length() >= filter)
							sub = locId.substring(0, filter);
						
						if (!loc.getExtId().contains(sub))
							throw new ConstraintViolations("The Location Id must contain " + sub);
					}
				}
			}
		}
	}
}
