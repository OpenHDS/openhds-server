package org.openhds.controller.service;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.HeadOfHousehold;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;

public interface HeadOfHouseholdService {
	
    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    HeadOfHousehold evaluateHeadOfHousehold(HeadOfHousehold entityItem) throws ConstraintViolations;
    
    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    HeadOfHousehold findHeadOfHouseholdById(String extId) throws Exception;

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    HeadOfHousehold createHeadOfHousehold(HeadOfHousehold entityItem) throws ConstraintViolations;
    
    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    HeadOfHousehold updateHeadOfHousehold(HeadOfHousehold hoh) throws Exception;
    
    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    Individual getIndividualByExtId(String extId) throws Exception;
    
    @Authorized({ PrivilegeConstants.EDIT_ENTITY })
    SocialGroup changeSocialGroupHeadOfHousehold(SocialGroup socialGroup, Individual newHeadOfHousehold) throws Exception;
}
