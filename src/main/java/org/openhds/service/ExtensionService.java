package org.openhds.service;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.ClassExtension;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.Visit;

public interface ExtensionService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit evaluateExtensions(Visit entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	ClassExtension evaluateClassExtension(ClassExtension entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	ClassExtension deleteClassExtension(ClassExtension entityItem) throws ConstraintViolations;
}
