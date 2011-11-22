package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Visit;

/**
 * @author Brian
 * 
 * The Generator for Visit Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class VisitGenerator<T> extends Generator<T> {

	@Override
	public String generateId(T entityItem) throws ConstraintViolations  {
				
		Visit visit = (Visit)entityItem;
		StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (key.equals(IdGeneratedFields.VISIT_LOCID.toString())) {
				String locId = visit.getVisitLocation().getExtId();
				
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
					String round = visit.getRoundNumber().toString();	
					sb.append(round);
				}
			}
		}
		
		String locId = visit.getVisitLocation().getExtId();
		HashMap<String, Integer> map = scheme.getFields();
		int length = map.get(IdGeneratedFields.VISIT_LOCID.toString());
		String bound = locId.substring(length, locId.length());
		sb.append(bound);

		if (scheme.isCheckDigit()) 
			sb.append(generateCheckCharacter(sb.toString()));
		
		validateIdLength(sb.toString(), scheme);
				
		return sb.toString();
	}

	// not applicable for visit
	@Override
	public String buildNumberWithBound(T entityItem, IdScheme scheme) throws ConstraintViolations {
		return null;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("Visit"));
		return resource.getIdScheme().get(index);
	}
}
