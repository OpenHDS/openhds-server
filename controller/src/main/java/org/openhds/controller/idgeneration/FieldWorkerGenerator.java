package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.FieldWorker;

/**
 * @author Brian
 * 
 * The Generator for FieldWorker Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class FieldWorkerGenerator<T> extends Generator<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public String generateId(T entityItem) throws ConstraintViolations  {
		FieldWorker fieldWorker = (FieldWorker)entityItem;
		StringBuilder sb = new StringBuilder();	
		
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("FieldWorker"));
		IdScheme scheme = resource.getIdScheme().get(index);
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (key.equals(IdGeneratedFields.FIELDWORKER_FNAME.toString())) {
				String fname = fieldWorker.getFirstName();
				
				if (fname.length() >= filter) {
				
					if (filter > 0 && fname.length() >= filter) 
						sb.append(formatProperString(fname, filter));
					else if (filter == 0 || fname.length() < filter) 
						sb.append(formatProperString(fname, fname.length()));
					else
						throw new ConstraintViolations("The provided value of '" + fname + "' " +
					    		" is shorter than the filtered length for the FieldWorker Id template.");
				}
				else
					throw new ConstraintViolations("Unable to generate the id. Make sure the field First Name is of the required length " +
							"specified in the id configuration.");
			}
			else if (key.equals(IdGeneratedFields.FIELDWORKER_LNAME.toString())) {
				String lname = fieldWorker.getLastName();
				
				if (lname.length() >= filter) {
				
					if (filter > 0 && lname.length() >= filter) 			
						sb.append(formatProperString(lname, filter));
					else if (filter == 0 || lname.length() < filter)
						sb.append(formatProperString(lname, lname.length()));
					else
						throw new ConstraintViolations("An error occurred while attempting to generate " +
								"the id on the field specified as '" + lname + "'");
				}
				else
					throw new ConstraintViolations("Unable to generate the id. Make sure the field Last Name is of the required length " +
							"specified in the id configuration.");
			}
		}

		if (scheme.getIncrementBound() == 0)		
			sb.append(buildNumber((Class<T>) FieldWorker.class, sb.toString(), scheme.isCheckDigit()));
		
		if (scheme.isCheckDigit()) 
			sb.append(generateCheckCharacter(sb.toString()));
		
		return sb.toString();
	}
	
	// not applicable for field worker 
	@Override
	public String buildNumberWithBound(T entityItem, IdScheme scheme) throws ConstraintViolations {
		return null;
	}
}
