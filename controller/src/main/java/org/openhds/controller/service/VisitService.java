package org.openhds.controller.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.EntityType;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Visit;

public interface VisitService {
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit evaluateVisit(Visit entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit generateId(Visit entityItem) throws ConstraintViolations;
			
	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	Visit checkVisit(Visit persistedItem, Visit entityItem) throws ConstraintViolations; 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getVisitExtIds(String term);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	Visit findVisitById(String visitId, String msg) throws Exception; 
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit initializeExtensions(Visit entityItem); 
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Visit addExtensions(Visit entityItem, EntityType name); 
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	List<ClassExtension> getExtensionsByEntityClassAndRoundNumber(EntityType entityType, int roundNum);

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void validateGeneralVisit(Visit visit) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
    void createVisit(Visit visit) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    List<Visit> getAllVisits(); 
}
