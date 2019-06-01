package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.Individual;
import org.openhds.domain.OutMigration;
import org.openhds.domain.PrivilegeConstants;

public interface OutMigrationService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void evaluateOutMigration(OutMigration outMigration) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<OutMigration> getOutMigrations(Individual individual);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createOutMigration(OutMigration outMigration) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createOutMigrationImg(OutMigration outMigration) throws ConstraintViolations;
}
