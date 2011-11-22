package org.openhds.web.service;

import org.openhds.controller.exception.ConstraintViolationException;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.PrivilegeConstants;

public interface EntityValidationService {
	
	<T> void setStatusPending(T entityItem);
	
	<T> void setStatusVoided(T entityItem);
	
	/**
	 * This method is provided so subclasses can execute some functionality before an entity is persisted
	 * @param entityItem
	 * @throws IllegalArgumentException
	 * @throws ConstraintViolationException
	 */
	<T> void onBeforeCommit(T entityItem) throws IllegalArgumentException, ConstraintViolationException;

	<T> void onBeforeSave(T entityItem) throws IllegalArgumentException, ConstraintViolationException;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	<T> void validateEntity(T entityItem) throws ConstraintViolationException;
}
