package org.openhds.service;

import java.sql.SQLException;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.Membership;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.SocialGroup;

public interface SocialGroupService {

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	SocialGroup evaluateSocialGroup(SocialGroup entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	SocialGroup checkSocialGroup(SocialGroup persistedItem, SocialGroup entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	SocialGroup generateId(SocialGroup entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Individual> getAllIndividualsOfSocialGroup(SocialGroup group);
			
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean compareDeathInSocialGroup(SocialGroup persistedItem, SocialGroup entityItem);
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	void deleteSocialGroup(SocialGroup group) throws SQLException;
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<SocialGroup> getAllSocialGroups(Individual individual); 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getSocialGroupExtIds(String term);

	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	void modifySocialGroupHead(SocialGroup group, Individual selectedSuccessor, List<Membership> memberships) throws ConstraintViolations, SQLException, Exception;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	SocialGroup findSocialGroupById(String socialGroupId, String msg) throws Exception; 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	SocialGroup getSocialGroupForIndividualByType(Individual individual, String groupType);

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<SocialGroup> getAllSocialGroups();

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createSocialGroup(SocialGroup socialGroup, Location  location) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    List<SocialGroup> getAllSocialGroupsInRange(int i, int pageSize);

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    long getTotalSocialGroupCount();

	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	void updateSocialGroup(SocialGroup socialGroup) throws ConstraintViolations; 
}
