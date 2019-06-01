package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.Round;

public interface RoundService {

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    void evaluateRound(Round round) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Round> getAllRounds();
    
    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Round> getLastRound();

    @Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createRound(Round round) throws ConstraintViolations;
    
}