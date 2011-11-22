package org.openhds.web.service.impl;

import org.openhds.web.service.EntityClassValidationService;
import org.openhds.web.service.JsfService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class EntityClassValidationServiceImpl<T> implements EntityClassValidationService<T> {
	
    JsfService jsfService;
    
    public EntityClassValidationServiceImpl() { }

	public boolean checkConstraints(T entityItem) {
		
		Validator validator = getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entityItem);
        
        ConstraintViolation<T> cv = null;
        if (constraintViolations.size() > 0) {
        	 Iterator<ConstraintViolation<T>> iter = constraintViolations.iterator();
             while (iter.hasNext()) {
            	 jsfService.addError(iter.next().getMessage());   	 
            	 return true;
             }   	
        }
        return false;
	}
	
	/**
	 * This method does the same thing as checkConstraints but does not use the jsf service
	 * Instead it returns a list of violations
	 * It is being used at the service layer - so trying to avoid any thing jsf specific
	 * 
	 * @param entity The entity to validate constraints on
	 * @return a list of strings which indicate all violating constraints
	 */
	public <S> List<String> validateType(S entity) {
		List<String> violations = new ArrayList<String>();

		Validator validator = getValidator();
		Set<ConstraintViolation<S>> constraintViolations = validator.validate(entity);
		
		for(ConstraintViolation<S> constraint : constraintViolations) {
			violations.add(constraint.getMessage());
		}
		
		return violations;
	}

	private Validator getValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
		return validator;
	}
	
	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
}
