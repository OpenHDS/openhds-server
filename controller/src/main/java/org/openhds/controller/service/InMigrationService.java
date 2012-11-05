package org.openhds.controller.service;

import java.sql.SQLException;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;

/**
 * A service class to be used when dealing with the InMigration class
 * This service provides various methods to verify the integrity of an InMigration in the system
 * 
 * @author Dave
 *
 */
public interface InMigrationService {

	/**
	 * Evaluate an in migration for integrity constraints
	 * 
	 * @param inMigration
	 * @return the in migration object passed in as an argument
	 * @throws ConstraintViolations if this in migration violates any integrity constraints
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	InMigration evaluateInMigration(InMigration inMigration) throws ConstraintViolations;
	
	/**
	 * Evaluate an in migration for integrity constraints on edit
	 * This differs slightly from the above method because editing an existing in migration
	 * introduces different constraints that must be checked
	 * 
	 * @param inMigration
	 * @return the in migration object passed in as an argument
	 * @throws ConstraintViolations if this edited in migration violates any integrit constraints
	 */
	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	InMigration evaluateInMigrationOnEdit(InMigration inMigration) throws ConstraintViolations, Exception;

	/**
	 * Return a listing of in migration for a specific individual
	 * 
	 * @param entityItem the individual to get a list of in migrations for
	 * @return a listing of in migrations for a specific individual
	 */
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<InMigration> getInMigrationsByIndividual(Individual entityItem);
	
	/**
	 * Create an InMigration entity within OpenHDS
	 * An Individual entity will also be created by this method if the type of
	 * InMigration is external, OR the InMigration contains an individual with
	 * an unknown Id. This method will implicitly call {{@link #evaluateInMigration(InMigration)}
	 * before attempting to create the in migration
	 * 
	 * @param inMigration The InMigration entity to create in the system
	 * @throws ConstraintViolations if an integrity constraint fails
	 * @throws SQLException if a database exception occurs
	 * @throws Exception any other type of exception that might occur
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createInMigration(InMigration inMigration) throws ConstraintViolations;
}
