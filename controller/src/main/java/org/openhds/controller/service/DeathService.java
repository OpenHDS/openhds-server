package org.openhds.controller.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.SocialGroup;

public interface DeathService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Death evaluateDeath(Death entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Death createDeath(Death entityItem) throws ConstraintViolations, SQLException; 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Death> getDeathsByIndividual(Individual individual);
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	void deleteDeath(Death entityItem); 
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	void createDeathAndSetNewHead(Death death, List<SocialGroup> groups, List<Individual> successors, HashMap<Integer, List<Membership>> memberships) throws ConstraintViolations, SQLException, Exception;
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	boolean checkDuplicateIndividual(Individual indiv);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	boolean checkHeadOfSocialGroup(Individual indiv);
}
