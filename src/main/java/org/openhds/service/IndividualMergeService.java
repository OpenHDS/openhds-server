package org.openhds.service;

import java.sql.SQLException;
import java.util.List;

import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.Individual;
import org.openhds.domain.PrivilegeConstants;

public interface IndividualMergeService {
	
	public enum MergeEvents {
		IN_MIGRATION, MEMBERSHIP
	}

	/**
	 * This method provides the ability to merge the events from one individual to another.
	 * 
	 * @param primary The individual who will receive all merged events
	 * @param toMergeFrom The individual to merge the events from
	 * @return the number of events merged
	 */
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public int mergeIndividuals(Individual primary, Individual toMergeFrom, List<MergeEvents> eventTypesToMerge) throws ConstraintViolations, ConstraintViolations, SQLException, AuthorizationException;
}
