package org.openhds.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Brian
 * 
 * The Generator for Individual Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

@Component("individualIdGenerator")
public class IndividualGenerator extends Generator<Individual> {
	private Location location;
	
	public void setLocation(Location location){
		this.location = location;
	}
		
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

	@Override
	@Autowired
	@Value("${openhds.individualIdUseGenerator}")
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
}
