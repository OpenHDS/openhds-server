package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.Individual;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.Relationship;
import org.openhds.domain.SocialGroup;

public interface RelationshipService {

	@Authorized({ PrivilegeConstants.CREATE_ENTITY })
	Relationship evaluateRelationship(Relationship entityItem) throws ConstraintViolations;

	@Authorized({ PrivilegeConstants.CREATE_ENTITY })
	Relationship checkRelationship(Relationship persistedItem, Relationship entityItem) throws ConstraintViolations;

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	boolean checkValidRelationship(Relationship entityItem);

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	List<Relationship> getAllRelationships(Individual individual);

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	List<Relationship> getAllRelationshipsWithinSocialGroup(Individual individual, SocialGroup group);

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	void validateGeneralRelationship(Relationship relationship) throws ConstraintViolations;

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	List<Relationship> getAllRelationships();

	@Authorized({ PrivilegeConstants.CREATE_ENTITY })
	void createRelationship(Relationship relationship) throws ConstraintViolations;

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
	List<Relationship> getAllRelationshipInRange(int i, int pageSize);

	@Authorized({ PrivilegeConstants.VIEW_ENTITY })
    long getTotalRelationshipCount();
}
