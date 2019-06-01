package org.openhds.service;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.HeadOfHousehold;
import org.openhds.domain.Individual;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.SocialGroup;

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
