package org.openhds.controller.service;

import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Round;

public interface RoundService {

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void evaluateRound(Round round) throws ConstraintViolations;
}