package org.openhds.service;

import java.util.Calendar;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Individual;
import org.openhds.domain.Membership;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.SocialGroup;

public interface MembershipService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Membership evaluateMembership(Membership entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Membership checkMembership(Membership persistedItem, Membership entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkDuplicateMembership(Individual indiv, SocialGroup group);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean compareDeathInMembership(Membership persistedItem, Membership entityItem);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkEndEventTypeForMembershipOnEdit(Membership persistedItem, Membership entityItem);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Membership> getAllMemberships(Individual indiv);
		
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Membership createMembershipForPregnancyOutcome(Calendar startDate, Individual individual, SocialGroup sg, FieldWorker fw, String relationToGroupHead);

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void validateGeneralMembership(Membership membership) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
    void createMembership(Membership item) throws ConstraintViolations; 
}
