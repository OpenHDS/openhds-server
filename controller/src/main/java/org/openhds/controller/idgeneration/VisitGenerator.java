package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Visit;

/**
 * @author Brian
 * 
 * The Generator for Visit Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class VisitGenerator<T> extends Generator<Visit> {

	@Override
	public String generateId(Visit entityItem) throws ConstraintViolations  {
				
		StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (key.equals(IdGeneratedFields.VISIT_LOCID.toString())) {
				String locId = entityItem.getVisitLocation().getExtId();
				
				if (locId.length() >= filter) {
				
					if (filter > 0 && locId.length() >= filter) 
						sb.append(formatProperString(locId, filter));
					else if (filter == 0 || locId.length() < filter) 
						sb.append(formatProperString(locId, locId.length()));
					else
						throw new ConstraintViolations("An error occurred while attempting to generate " +
								"the id on the field specified as '" + locId + "'");
				}
				else
					throw new ConstraintViolations("Unable to generate the id. Make sure the field Location Id is of the required length " +
							"specified in the id configuration.");
			}
			else if (key.equals(IdGeneratedFields.VISIT_ROUND.toString())) {	
				if (filter > 0) {	
					String round = entityItem.getRoundNumber().toString();	
					sb.append(round);
				}
			}
		}
		
		extId = sb.toString();
		if (scheme.getIncrementBound() > 0) 
			sb.append(buildNumberWithBound(entityItem, scheme));
		else
			sb.append(buildNumber(Visit.class, sb.toString(), scheme.isCheckDigit()));
		
		if (scheme.isCheckDigit()) 
			sb.append(generateCheckCharacter(sb.toString()));
		
		validateIdLength(sb.toString(), scheme);
				
		return sb.toString();
	}

	@Override
	public String buildNumberWithBound(Visit entityItem, IdScheme scheme) throws ConstraintViolations {
		Visit visit = new Visit();
		
		Integer size = 1;
		String result = "";
		
		// get length of the incrementBound
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();

		while (visit != null) {
			
			result = "";
			String tempExtId = "";
			
			if (extId != null)
				tempExtId = extId;
			
			while (result.toString().length() < incBoundLength) {
				if (result.toString().length()+ size.toString().length() < incBoundLength)
					result += "0";
				if (result.toString().length() + size.toString().length() == incBoundLength)
					result = result.concat(size.toString());
			}
			tempExtId = tempExtId.concat(result);
			visit = genericDao.findByProperty(Visit.class, "extId", tempExtId);
			size++;
		}
		return result;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("Visit"));
		return resource.getIdScheme().get(index);
	}
}
