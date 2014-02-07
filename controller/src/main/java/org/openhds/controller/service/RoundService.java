package org.openhds.controller.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Round;

public interface RoundService {

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    void evaluateRound(Round round) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Round> getAllRounds();
    
    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Round> getLastRound();
    
}