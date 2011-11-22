package org.openhds.web.service.impl;

import java.util.List;
import org.openhds.controller.exception.ConstraintViolationException;
import org.openhds.domain.model.AuditableCollectedEntity;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.web.service.EntityClassValidationService;
import org.openhds.web.service.EntityValidationService;

@SuppressWarnings("unchecked")
public class EntityValidationServiceImpl implements EntityValidationService {
	
	SitePropertiesService siteProperties;
	EntityClassValidationService classValidator;
	
	EntityValidationServiceImpl(SitePropertiesService siteProperties, EntityClassValidationService classValidator) {
		this.siteProperties = siteProperties;
		this.classValidator = classValidator;
	}

	public <T> void onBeforeCommit(T entityItem) throws IllegalArgumentException, ConstraintViolationException {
		validateEntity(entityItem);
	}

	public <T> void onBeforeSave(T entityItem) throws IllegalArgumentException, ConstraintViolationException {
		validateEntity(entityItem);
	}

	public <T> void setStatusPending(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusPendingCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}

	public <T> void setStatusVoided(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusVoidCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}

	public <T> void validateEntity(T entityItem) throws ConstraintViolationException {
		List<String> violations = classValidator.validateType(entityItem);
    	
    	if (violations.size() > 0) {
    		throw new ConstraintViolationException(violations.get(0).toString(), violations);
    	}
	}
}
