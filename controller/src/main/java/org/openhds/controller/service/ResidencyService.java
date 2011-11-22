package org.openhds.controller.service;

import java.util.Calendar;
import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.AuditableCollectedEntity;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Residency;

/**
 * A service class to be used when dealing with the Residency class
 * This service provides various methods to verify the integrity of a Residency in the system
 * 
 * @author Dave
 *
 */
public interface ResidencyService {

	/**
	 * Determine if the a residency is valid for an Individual.
	 * Specifically, look at fields related to starting a residency (start date, start type, etc.)
	 * and verify they are valid compared to previous residencies for the Individual
	 * 
	 * @param residency the residency to evaluate
	 * @return the same residency object passed in as an argument
	 * @throws ConstraintViolations if any integrity constraints are violated
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Residency evaluateResidency(Residency residency) throws ConstraintViolations;
	
	/**
	 * Create a residency.
	 * 
	 * @param individual the individual for the residency
	 * @param location the location of the residency
	 * @param startDate the start date of the residency
	 * @param startType the start type of the residency
	 * @param collectedBy The field worker who collected the residency information
	 * @return A newly created residency
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Residency createResidency(Individual individual, Location location, Calendar startDate, String startType, FieldWorker collectedBy);
	
	/**
	 * Retrieve a list of individual who have their last known residency (current residency) for a given location
	 * 
	 * @param location The location to look for residencies at
	 * @return a list of individuals who have a current residency at the location
	 */
	@Authorized({PrivilegeConstants.VIEW_ENTITY, PrivilegeConstants.ACCESS_UPDATE, PrivilegeConstants.ACCESS_BASELINE})
	List<Individual> getIndividualsByLocation(Location location);
	
	/**
	 * Return the associated events for a given residency
	 * (e.g. Pregnancy outcome, in migration, out migration, death).
	 * 
	 * @return the associated events for this residency
	 */
	
	List<AuditableCollectedEntity> getResidencyAssociatedEvents(Residency residency);
	
	/**
	 * Determine if an Individual has a current open residency. An open residency is defined as a residency
	 * that has no end date
	 * 
	 * @param individual the individual to check for an open residency
	 * @return true if the individual has atleast 1 open residency, false otherwise
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean hasOpenResidency(Individual individual);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    List<Residency> getAllResidencies(Individual individual);
}
