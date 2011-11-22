package org.openhds.controller.service;

import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.SocialGroup;

public interface RelationshipService {

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Relationship evaluateRelationship(Relationship entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Relationship checkRelationship(Relationship persistedItem, Relationship entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	boolean checkValidRelationship(Relationship entityItem);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Relationship> getAllRelationships(Individual individual);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Relationship> getAllRelationshipsWithinSocialGroup(Individual individual, SocialGroup group);

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void validateGeneralRelationship(Relationship relationship) throws ConstraintViolations;
}
