package org.openhds.controller.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.PrivilegeConstants;

/**
 * Interface that defines the methods for the PregnancyObservation and PregnancyOutcome entity types
 * 
 * @author Dave
 *
 */
public interface PregnancyService {
	
	/**
	 * Verify a pregnancy observation is valid
	 * This should be called before the entity is saved
	 * 
	 * @param entityItem the pregnancy observation to check
	 * @return the pregnancy observation that was passed in as an argument
	 * @throws ConstraintViolations if any integrity constraints are broken for this pregnancy observation
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	PregnancyObservation evaluatePregnancyObservation(PregnancyObservation entityItem) throws ConstraintViolations;

	/**
	 * Determine if a mother already has a duplicate pregnancy observation
	 * In this case, a pregnancy observation is considered duplicate if the mother already has an outstanding
	 * pregnancy observation that has not been closed
	 * 
	 * @param mother the mother to check the pregnancy observation for
	 * @return true if the mother has no outstanding pregnancy observations, false otherwise
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkDuplicatePregnancyObservation(Individual mother);

	/**
	 * Create a pregnancy outcome entity
	 * In addition to creating the pregnancy outcome entity, if the pregnancy outcome contains any children
	 * born, then the following should occur within the same transaction:
	 * The children are created
	 * A residency for the child(ren) is created set to the same location as the mothers current residency
	 * A Membership is created for the child(ren) which is set to the mothers family social group
	 * Close any prior pregnancy observation for the mother
	 * 
	 * @param outcome the pregnancy outcome instance to persist
	 * @throws IllegalArgumentException
	 * @throws ConstraintViolationException if any constraints are violated for this pregnancy outcome
	 * @throws SQLException
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createPregnancyOutcome(PregnancyOutcome outcome) throws IllegalArgumentException, ConstraintViolations, SQLException;
	
	/**
	 * Verify a pregnancy outcome is valid
	 * This should be called before the entity is saved
	 * 
	 * @param entityItem the item to verify is valid
	 * @return the pregnancy outcome passed in as an argument
	 * @throws ConstraintViolations if the pregnancy outcome is not valid
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	PregnancyOutcome evaluatePregnancyOutcome(PregnancyOutcome entityItem) throws ConstraintViolations;

	/**
	 * Return a list of outcomes for a specific individual
	 * 
	 * @param individual the individual to filter results on
	 * @return a listing containing any pregnancy outcomes for the individual
	 */
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<PregnancyOutcome> getPregnancyOutcomesByIndividual(Individual individual);
	
	/**
	 * Return a list of observations for a specific individual
	 * 
	 * @param individual the individual to filter results on
	 * @return a listing contains any observations for the individual
	 */
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<PregnancyObservation> getPregnancyObservationByIndividual(Individual individual);
	
	/**
	 * Close a pregnancy observation for a mother
	 * 
	 * @param mother the individual to close the pregnancy observation for
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void closePregnancyObservation(Individual mother);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void validateGeneralPregnancyObservation(PregnancyObservation entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<PregnancyOutcome> findAllLiveBirthsBetweenInterval(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	int findAllBirthsBetweenIntervalByGender(Calendar startDate, Calendar endDate, int flag);
}