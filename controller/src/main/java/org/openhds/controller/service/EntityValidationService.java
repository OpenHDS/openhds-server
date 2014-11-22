package org.openhds.controller.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;

public interface EntityValidationService<T> {
	
	boolean checkConstraints(T entityItem);
		
	<S> List<String> validateType(S entity);
	
	void validateEntity(T entityItem) throws ConstraintViolations; 
	
	void setStatusPending(T entityItem);
	
	void setStatusVoided(T entityItem);
}
