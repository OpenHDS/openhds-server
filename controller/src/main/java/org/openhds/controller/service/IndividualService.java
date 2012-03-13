package org.openhds.controller.service;

import java.util.List;
import org.openhds.domain.annotations.Authorized;
import org.openhds.controller.beans.RecordGroup;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;

public interface IndividualService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Individual evaluateIndividual(Individual entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void validateGeneralIndividual(Individual indiv) throws ConstraintViolations; 
		
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	String generateIdWithBound(Individual entityItem, int count) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.ACCESS_BASELINE, PrivilegeConstants.ACCESS_UPDATE, PrivilegeConstants.CREATE_ENTITY, PrivilegeConstants.EDIT_ENTITY})
	public String getLatestEvent(Individual individual);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	public Individual validateIdLength(Individual entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getIndividualExtIds(String term); 
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Individual generateId(Individual entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    Individual findIndivById(String indivExtId, String msg) throws Exception; 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	Individual findIndivById(String indivExtId);

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Individual createTemporaryIndividualWithExtId(String extId, FieldWorker CollectedBy) throws Exception;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setPopulationForAgeGroupsByLocation(RecordGroup populationGroup, List<String> hierarchyIds);
}

