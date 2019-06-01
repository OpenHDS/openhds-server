package org.openhds.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.AuditableCollectedEntity;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.JsfService;
import org.openhds.service.EntityValidationService;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@SuppressWarnings("unchecked")
public class EntityValidationServiceImpl<T> implements EntityValidationService<T> {
	
	SitePropertiesService siteProperties;
	JsfService jsfService;
	
	public EntityValidationServiceImpl(SitePropertiesService siteProperties, JsfService jsfService) {
		this.siteProperties = siteProperties;
		this.jsfService = jsfService;
	}

	public void setStatusPending(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusPendingCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}

	public void setStatusVoided(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusVoidCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}

	public void validateEntity(T entityItem) throws ConstraintViolations {
		List<String> violations = validateType(entityItem);
    	
    	if (violations.size() > 0) {
    		throw new ConstraintViolations(violations.get(0).toString(), violations);
    	}
	}

	public boolean checkConstraints(T entityItem) {
		Validator validator = getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entityItem);
        
        if (constraintViolations.size() > 0) {
        	 Iterator<ConstraintViolation<T>> iter = constraintViolations.iterator();
             while (iter.hasNext()) {
            	 jsfService.addError(iter.next().getMessage());   	 
             }   
        	 return true;
        }
        return false;
	}

	@SuppressWarnings("rawtypes")
	public <S> List validateType(S entity) {
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
}
