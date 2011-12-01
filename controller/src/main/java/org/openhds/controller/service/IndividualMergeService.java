package org.openhds.controller.service;

import java.sql.SQLException;
import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;

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
