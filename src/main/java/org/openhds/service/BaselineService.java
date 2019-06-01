package org.openhds.service;

import java.sql.SQLException;
import java.util.Calendar;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.Membership;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.Relationship;
import org.openhds.domain.SocialGroup;

public interface BaselineService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyAndMembershipForIndividual(Individual individual, Membership membership, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createSocialGroupAndResidencyForIndividual(Individual individual, SocialGroup socialGroup, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyForIndividual(Individual individual, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyMembershipAndRelationshipForIndividual(Individual individual, Membership membership, Relationship relationship, Location currentLocation, FieldWorker collectedBy, Calendar convertedEntryDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createMembershipForIndividual(Individual individual, Membership membership, SocialGroup socialgroup, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createIndividual(Individual individual) throws IllegalArgumentException, ConstraintViolations, SQLException; 

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createLocation(Location location) throws IllegalArgumentException, ConstraintViolations, SQLException;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createSocialGroup(SocialGroup group) throws IllegalArgumentException, ConstraintViolations, SQLException;
}
