package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.ClassExtension;
import org.openhds.domain.EntityType;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.SocialGroup;
import org.openhds.domain.Visit;

public interface VisitService {

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    Visit evaluateVisit(Visit entityItem) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    Visit generateId(Visit entityItem) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.EDIT_ENTITY })
    Visit checkVisit(Visit persistedItem, Visit entityItem) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<String> getVisitExtIds(String term);

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    Visit findVisitById(String visitId, String msg) throws Exception;

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    Visit initializeExtensions(Visit entityItem);

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    Visit addExtensions(Visit entityItem, EntityType name);

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    List<ClassExtension> getExtensionsByEntityClassAndRoundNumber(EntityType entityType, int roundNum);

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    void validateGeneralVisit(Visit visit) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    void createVisit(Visit visit) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Visit> getAllVisits();

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Visit> getAllVisitsForRoundInRange(int round, int i, int pageSize);

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    long getTotalVisitCountForRound(int roundNumber);
    
    @Authorized({PrivilegeConstants.EDIT_ENTITY})
	void updateVisit(Visit visit) throws ConstraintViolations; 
}
