package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;

/**
 * @author Brian
 * 
 * The Generator for Individual Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

public class IndividualGenerator extends Generator<Individual> {
		
	@Override
	public String generateId(Individual individual) throws ConstraintViolations  {
		StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (filter != null) {
			
				if (key.equals(IdGeneratedFields.INDIVIDUAL_FNAME.toString())) {
					String fname = individual.getFirstName();
					
					if (fname.length() >= filter) {
						
						if (filter > 0 && fname.length() >= filter) 
							sb.append(formatProperString(fname, filter));
						else if (filter == 0 || fname.length() < filter) 
							sb.append(formatProperString(fname, fname.length()));
						else
							throw new ConstraintViolations("An error occurred while attempting to generate " +
									"the id on the field specified as '" + fname + "'");
					}
					else
						throw new ConstraintViolations("Unable to generate the id. Make sure the First Name field is of the required length " +
						"specified in the id configuration.");
				}
				else if (key.equals(IdGeneratedFields.INDIVIDUAL_MNAME.toString())) {
					String mname = individual.getMiddleName();
					
					if (mname.length() >= filter) {
					
						if (filter > 0 && mname.length() >= filter) 			
							sb.append(formatProperString(mname, filter));
						else if (filter == 0 || mname.length() < filter)
							sb.append(formatProperString(mname, mname.length()));
						else
							throw new ConstraintViolations("An error occurred while attempting to generate " +
									"the id on the field specified as '" + mname + "'");
					}
					else
						throw new ConstraintViolations("Unable to generate the id. Make sure the Middle Name field is of the required length " +
						"specified in the id configuration.");
				}
				else if (key.equals(IdGeneratedFields.INDIVIDUAL_LNAME.toString())) {
					String lname = individual.getLastName();
					
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
						throw new ConstraintViolations("Unable to generate the id. Make sure the Last Name field is of the required length " +
						"specified in the id configuration.");
				}
			}
		}
		
		extId = sb.toString();
		if (scheme.getIncrementBound() > 0) 
			sb.append(buildNumberWithBound(individual, scheme));
		else
			sb.append(buildNumber(Individual.class, sb.toString(), scheme.isCheckDigit()));
				
		validateIdLength(sb.toString(), scheme);
		
		return sb.toString();
	}

	@Override
	public String buildNumberWithBound(Individual entityItem, IdScheme scheme) throws ConstraintViolations {

		Individual tempIndividual = new Individual();
				
		Integer size = 1;
		String result = "";
		
		// get length of the incrementBound
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();
		
		while (tempIndividual != null) {
			
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

			tempIndividual = genericDao.findByProperty(Individual.class, "extId", tempExtId);
			size++;
		}
		return result;
	}
	
	/**
	 * Removes the last piece of the id, which is the bound. Used for custom generating
	 * a new id from an existing piece of an id.
	 */
	public String filterBound(String string) {
		IdScheme scheme = getIdScheme();
		Integer incBound = scheme.getIncrementBound();
		if (string.length() > incBound.toString().length()) 
			return string.substring(0, string.length()-incBound.toString().length());
		return "";
	}
	
	/**
	 * Increments the id by one according to the bound specified.
	 * This is important in custom building multiple id's that are created
	 * in a single transaction. 
	 */
	public String incrementId(String string) {
		IdScheme scheme = getIdScheme();
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();
		String result = "";

		String baseString = string.substring(0, string.length()-incBoundLength);
		String partToIncrement = string.substring(string.length()-incBoundLength, string.length());
		Integer partToIncrementInt = Integer.parseInt(partToIncrement);
		partToIncrementInt++;
		partToIncrement = partToIncrementInt.toString();
		
		while (result.length() < incBoundLength) {
			if (result.length() + partToIncrement.length() < incBoundLength)
				result += "0";
			if (result.length() + partToIncrement.length() == incBoundLength)
				result = result.concat(partToIncrement.toString());
		}
		return baseString.concat(result);
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("Individual"));
		return resource.getIdScheme().get(index);
	}
}
