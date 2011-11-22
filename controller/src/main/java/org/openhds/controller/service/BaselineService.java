package org.openhds.controller.service;

import java.sql.SQLException;
import java.util.Calendar;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.SocialGroup;

public interface BaselineService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyAndMembershipForIndividual(Individual individual, Membership membership, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolationException;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createSocialGroupAndResidencyForIndividual(Individual individual, SocialGroup socialGroup, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolationException;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyForIndividual(Individual individual, Location currentLocation, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolationException;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createResidencyMembershipAndRelationshipForIndividual(Individual individual, Membership membership, Relationship relationship, Location currentLocation, FieldWorker collectedBy, Calendar convertedEntryDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolationException;;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createMembershipForIndividual(Individual individual, Membership membership, SocialGroup socialgroup, FieldWorker collectedBy, Calendar startDate) throws SQLException, ConstraintViolations, IllegalArgumentException, ConstraintViolationException;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createIndividual(Individual individual) throws IllegalArgumentException, ConstraintViolationException, SQLException; 

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createLocation(Location location) throws IllegalArgumentException, ConstraintViolationException, SQLException;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	public void createSocialGroup(SocialGroup group) throws IllegalArgumentException, ConstraintViolationException, SQLException;
}
