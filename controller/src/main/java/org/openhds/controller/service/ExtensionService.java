package org.openhds.controller.service;

import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Visit;

public interface ExtensionService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit evaluateExtensions(Visit entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	ClassExtension evaluateClassExtension(ClassExtension entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	ClassExtension deleteClassExtension(ClassExtension entityItem) throws ConstraintViolations;
}
