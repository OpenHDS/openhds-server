package org.openhds.idgeneration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.SiteConfigService;
import org.openhds.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Brian
 * 
 * An abstract class that's used as a template for generating id's. 
 * The idea is that generators for Location, Visit, Individual, etc
 * will extend this class in order to provide more specific 
 * implementations for generating the id.
 * 
 * generateId(T entityItem) will build the external id for the entityItem provided.
 * 
 */
@Component("idGenerator")
public abstract class Generator<T> extends LuhnValidator {
	
	@Autowired
	protected IdSchemeResource resource;
	@Autowired
	protected GenericDao genericDao;
	@Autowired
	protected SiteConfigService siteconfigService;
	
	// temp variable for storing parts of the id
	protected String extId;
	
	// if set to true, the system will automatically generate the id based on the id scheme
	// if set to false, the system will not manage id generation and the user will be responsible
	public boolean generated;
	
	public abstract String generateId(T entityItem) throws ConstraintViolations;
	
	public abstract String buildNumberWithBound(T entityItem, IdScheme scheme) throws ConstraintViolations;
		
	/**
	 * Builds the number portion of the id and ensures that it's unique.
	 * @throws ConstraintViolations 
	 */
	public String buildNumber(Class<T> classType, String prefix, boolean checkDigit) {
					
		Integer suffixInt = 0;
		String numberGen;
		List<T> list = null;  
		do {  		      	  
			suffixInt++;
			numberGen = suffixInt.toString();
			String temp = prefix.toString() + numberGen;
			
			if (checkDigit) {
				String resultChar = generateCheckCharacter(temp).toString();
				temp = temp.concat(resultChar);
				numberGen = numberGen.concat(resultChar);
			}

			list = genericDao.findListByProperty(classType, "extId", temp);	
		} while (list.size() > 0);
		
	    return numberGen;	
	}
		
	/**
	 * Formats the id and ensures that it's valid.
	 */
	final String formatProperString(String string, Integer filter) throws ConstraintViolations {
		
		String substring = string.substring(0, filter);
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");   
	    Matcher matcher = pattern.matcher(substring);
	  
		if (!matcher.matches())
			throw new ConstraintViolations(substring + " contains invalid characters that " +
					"cannot be specified as part of the Id.");  
				
		return substring.trim().toUpperCase();
	}
	
	/**
	 * Validate the id length against the idScheme.
	 * This should be used especially if the id is being entered manually.
	 */
	public final boolean validateIdLength(String id, IdScheme scheme) throws ConstraintViolations {	
		if (id.length() != scheme.length)
			throw new ConstraintViolations(id + " is not of the required length as specified in the IdScheme. " +
					"It must be " + scheme.length + " characters long.");  
		return true;
	}
		
	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}

	public IdSchemeResource getResource() {
		return resource;
	}

	public void setResource(IdSchemeResource resource) {
		this.resource = resource;
	}
	
	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
}
