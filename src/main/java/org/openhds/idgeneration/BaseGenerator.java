package org.openhds.idgeneration;

import org.openhds.exception.ConstraintViolations;

/**
 * @author Brian
 * 
 * A base template for basic id generation. Since there
 * is no Id Scheme specified, an increment digit can be 
 * calculated and used as the external id instead. 
 */
public class BaseGenerator<T> extends Generator<T> {

	@Override
	public String generateId(T entityItem) throws ConstraintViolations {
		return null;
	}
	
	public String buildNumber(Class<T> classType, String prefix, boolean checkDigit) {
		return super.buildNumber(classType, prefix, checkDigit);
	}

	@Override
	public String buildNumberWithBound(T entityItem, IdScheme scheme) throws ConstraintViolations {
		return null;
	}
}
