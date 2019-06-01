package org.openhds.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.idgeneration.VisitGenerator;
import org.openhds.service.EntityService;
import org.openhds.service.VisitService;
import org.openhds.dao.GenericDao;
import org.openhds.dao.GenericDao.ValueProperty;
import org.openhds.annotations.Authorized;
import org.openhds.domain.ClassExtension;
import org.openhds.domain.EntityType;
import org.openhds.domain.Extension;
import org.openhds.domain.Round;
import org.openhds.domain.Visit;
import org.openhds.service.SitePropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("visitServiceImpl")
public class VisitServiceImpl implements VisitService {

	@Autowired
    private GenericDao genericDao;
	@Autowired
    private VisitGenerator generator;
	@Autowired
    private EntityService entityService;
	@Autowired
    private SitePropertiesService siteProperties;

    public Visit evaluateVisit(Visit entityItem) throws ConstraintViolations {
        if (!checkValidRoundNumber(entityItem.getRoundNumber())){
        	entityItem.setExtId(null);
            throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");
        }

        generator.validateIdLength(entityItem.getExtId(), generator.getIdScheme());
        
        //Check if key already exists
        Visit v = null;
        try{
        	v = findVisitById(entityItem.getExtId(), "");
        }catch(Exception e){}
        
    	if(v != null){
    		throw new ConstraintViolations("Duplicate entry for visit extId!");
    	}

        return entityItem;
    }

    public Visit generateId(Visit entityItem) throws ConstraintViolations {
        entityItem.setExtId(generator.generateId(entityItem));
        return entityItem;
    }

    public Visit checkVisit(Visit persistedItem, Visit entityItem) throws ConstraintViolations {

        if (!checkValidRoundNumber(entityItem.getRoundNumber()))
            throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");
        if (!(entityItem.getRealVisit().equalsIgnoreCase("0") || entityItem.getRealVisit().equalsIgnoreCase("1"))) 
        	 throw new ConstraintViolations("The parameter real Visit has not a valid value.");
 
        return entityItem;
    }

    public void validateGeneralVisit(Visit visit) throws ConstraintViolations {
        if (!checkValidRoundNumber(visit.getRoundNumber()))
            throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");
    }

    /**
     * Checks if the provided round number exists
     */
    public boolean checkValidRoundNumber(Integer roundNumber) {

        Round round = genericDao.findByProperty(Round.class, "roundNumber", roundNumber);
        if (round != null)
            return true;
        return false;
    }

    /**
     * Retrieves all Visit extId's that contain the term provided. Used in performing autocomplete.
     */
    public List<String> getVisitExtIds(String term) {
        List<String> ids = new ArrayList<String>();
        List<Visit> list = genericDao.findListByPropertyPrefix(Visit.class, "extId", term, 10, true);
        for (Visit visit : list) {
            ids.add(visit.getExtId());
        }

        return ids;
    }

    public Visit findVisitById(String visitId, String msg) throws Exception {
        Visit visit = genericDao.findByProperty(Visit.class, "extId", visitId);
        if (visit == null) {
            throw new Exception(msg);
        }
        return visit;
    }

    /**
     * The following extension methods are called from the Crud in retrieving the associated extensions, grouped by
     * entity
     */
    public Visit initializeExtensions(Visit entityItem) {
        List<ClassExtension> list = genericDao.findListByProperty(ClassExtension.class, "roundNumber",
                entityItem.getRoundNumber());

        for (ClassExtension ce : list) {
            Extension extension = new Extension();
            extension.setEntity(entityItem);
            extension.setClassExtension(ce);
            entityItem.getExtensions().add(extension);
        }
        return entityItem;
    }

    public Visit addExtensions(Visit entityItem, EntityType name) {

        List<ClassExtension> list = getExtensionsByEntityClassAndRoundNumber(name, entityItem.getRoundNumber());

        for (ClassExtension ce : list) {
            Extension extension = new Extension();
            extension.setEntity(entityItem);
            extension.setClassExtension(ce);
            entityItem.getExtensions().add(extension);
        }
        return entityItem;
    }

    public List<ClassExtension> getExtensionsByEntityClassAndRoundNumber(EntityType entityType, int roundNum) {

        final EntityType entityName = entityType;
        final int visitRoundNum = roundNum;

        ValueProperty roundNumber = new ValueProperty() {
            public String getPropertyName() {
                return "roundNumber";
            }

            public Object getValue() {
                return visitRoundNum;
            }
        };

        ValueProperty indivType = new ValueProperty() {
            public String getPropertyName() {
                return "entityClass";
            }

            public Object getValue() {
                return entityName;
            }
        };

        return genericDao.findListByMultiProperty(ClassExtension.class, roundNumber, indivType);
    }

    @Transactional
    public void createVisit(Visit visit) throws ConstraintViolations {
        assignId(visit);
        evaluateVisit(visit);
        visit.setStatus(siteProperties.getDataStatusValidCode());

        try {
            entityService.create(visit);
        } catch (IllegalArgumentException e) {
            // should never happen
        } catch (SQLException e) {
            throw new ConstraintViolations("There was a problem saving the visit to the database");
        }
    }

    private void assignId(Visit visit) throws ConstraintViolations {
        String id = visit.getExtId() == null ? "" : visit.getExtId();
        if (id.trim().isEmpty() && generator.isGenerated()) {
            generateId(visit); 
        }
    }

    @Override
    public List<Visit> getAllVisits() {
        List<Visit> visits = genericDao.findAll(Visit.class, true);
        return visits;
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public List<Visit> getAllVisitsForRoundInRange(int round, int i, int pageSize) {
        return genericDao.findPagedFilteredgt(Visit.class, "extId", "roundNumber", round, i, pageSize);
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public long getTotalVisitCountForRound(int roundNumber) {
        return genericDao.getTotalCountWithFilter(Visit.class, "roundNumber", roundNumber);
    }

	@Override
	public void updateVisit(Visit visit) throws ConstraintViolations {
		// TODO Auto-generated method stub
		
	}
}
