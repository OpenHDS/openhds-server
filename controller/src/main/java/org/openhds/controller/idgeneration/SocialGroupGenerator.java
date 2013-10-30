package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.SocialGroup;

/**
 * @author Brian
 * 
 * The Generator for SocialGroup Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class SocialGroupGenerator extends Generator<SocialGroup> {
	private Location location;
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	@Override
	public String generateId(SocialGroup entityItem) throws ConstraintViolations  {
StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (filter != null) {
			
				if (key.equals(IdGeneratedFields.LOCATION_PREFIX.toString())) {
					//String fname = individual.getFirstName();
					String extId = location.getExtId();
					
					if (extId.length() >= filter) {
						
						if (filter > 0 && extId.length() >= filter) 
							sb.append(formatProperString(extId, filter));
						else if (filter == 0 || extId.length() < filter) 
							sb.append(formatProperString(extId, extId.length()));
						else
							throw new ConstraintViolations("An error occurred while attempting to generate " +
									"the id on the field specified as '" + extId + "'");
					}
					else
						throw new ConstraintViolations("Unable to generate the id. Make sure the First Name field is of the required length " +
						"specified in the id configuration.");
				}
				
			}
		}

		extId = sb.toString();
		if (scheme.getIncrementBound() > 0) 
			sb.append(buildNumberWithBound(entityItem, scheme));
		else
			sb.append(buildNumber(SocialGroup.class, sb.toString(), scheme.isCheckDigit()));
				
		validateIdLength(sb.toString(), scheme);
		
		return sb.toString();
	}

	@Override
	public String buildNumberWithBound(SocialGroup entityItem, IdScheme scheme) throws ConstraintViolations {

		SocialGroup tempSocialGroup = new SocialGroup();
		
		Integer size = 1;
		String result = "";
		
		// get length of the incrementBound
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();

		while (tempSocialGroup != null) {
			
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
			
			tempSocialGroup = genericDao.findByProperty(SocialGroup.class, "extId", tempExtId);
			size++;
		}
		return result;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("SocialGroup"));
		return resource.getIdScheme().get(index);
	}
}
