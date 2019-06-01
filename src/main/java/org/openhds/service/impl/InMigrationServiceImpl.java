package org.openhds.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.EntityService;
import org.openhds.service.InMigrationService;
import org.openhds.service.IndividualService;
import org.openhds.service.ResidencyService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.MigrationType;
import org.openhds.domain.Residency;
import org.openhds.service.SitePropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the InMigraiton Service
 * 
 * @author Dave
 *
 */
public class InMigrationServiceImpl implements InMigrationService {
	
	private static Logger log = LoggerFactory.getLogger(InMigrationServiceImpl.class);
	
	private ResidencyService residencyService;
	private EntityService entityService;
	private IndividualService individualService;
	private GenericDao genericDao; 
	private SitePropertiesService siteProperties;
	
	public InMigrationServiceImpl(ResidencyService residencyService, EntityService entityService, IndividualService individualService, GenericDao genericDao, SitePropertiesService siteProperties) {
		this.residencyService = residencyService;
		this.entityService = entityService;
		this.individualService = individualService;
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
	}

	public InMigration evaluateInMigration(InMigration inMigration) throws ConstraintViolations {
		checkIfIndividualIsDeceased(inMigration);
		checkIfIndividualHasOpenResidency(inMigration);
		
		return inMigration;
	}

	private void checkIfIndividualIsDeceased(InMigration inMigration) throws ConstraintViolations {
		if (inMigration.getIndividual().getUuid() != null && individualService.getLatestEvent(inMigration.getIndividual()).equals("Death")) {
			log.debug("Tried to create an In Migration for a dead individual");
    		throw new ConstraintViolations("An In Migration cannot be created for an Individual who has a Death event.");	
    	}
	}

	private void checkIfIndividualHasOpenResidency(InMigration inMigration) throws ConstraintViolations {
		if (residencyService.hasOpenResidency(inMigration.getIndividual())) {
			log.debug("Individual with uuid: " + inMigration.getIndividual().getUuid() + " has open residency - cannot create in migration");
			throw new ConstraintViolations("The individual for this in migration has an open residency. Please close the residency before you create an in migration");
		}
	}
	
	public InMigration evaluateInMigrationOnEdit(InMigration inMigration) throws ConstraintViolations, Exception {
		checkIfResidencyIsClosed(inMigration.getResidency());
		setResidencyFieldsFromInMigration(inMigration);
		checkIfResidencyIsValid(inMigration.getResidency());
		
		return inMigration;
	}

	private void checkIfResidencyIsClosed(Residency residency) throws ConstraintViolations {
		if (residency.getEndDate() != null) {
			log.debug("Tried to make changes to an in migration whose residency has closed");
			throw new ConstraintViolations("You cannot make changes to an in migration whose residency has been closed.");
		}
	}

	private void checkIfResidencyIsValid(Residency residency) throws ConstraintViolations {
		residencyService.evaluateResidency(residency);
	}
	
	private boolean checkValidMigrationType(InMigration inMigration) throws ConstraintViolations {
		if(inMigration.getMigType() == null)
			throw new ConstraintViolations("Invalid Migration Type!");
			
		String test = inMigration.getMigType().name();
		
		for (MigrationType mt : MigrationType.values()) {
			if (mt.name().equals(test)) {
				return true;
		    }
		}

		throw new ConstraintViolations("Unknown Migration Type!");
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void createInMigration(InMigration inMigration) throws ConstraintViolations {
		checkValidMigrationType(inMigration);
		setResidencyFieldsFromInMigration(inMigration);
		checkValidIndividual(inMigration);

		residencyService.evaluateResidency(inMigration.getResidency());
		if (inMigration.isUnknownIndividual() || inMigration.getMigType().equals(MigrationType.EXTERNAL_INMIGRATION) || inMigration.getMigType().equals(MigrationType.BASELINE)) {
			try {
                entityService.create(inMigration.getIndividual());
            } catch (IllegalArgumentException e) {
            } catch (SQLException e) {
                throw new ConstraintViolations("There was a problem creating the individual for the in migration in the database");
            }
		}
		
		try {
            entityService.create(inMigration.getResidency());
        } catch (IllegalArgumentException e) {
        } catch (SQLException e) {
            throw new ConstraintViolations("There was a problem creating the residency for the in migration in the database");
        }
		
		if (inMigration.isUnknownIndividual() || inMigration.getMigType().equals(MigrationType.EXTERNAL_INMIGRATION)|| inMigration.getMigType().equals(MigrationType.INTERNAL_INMIGRATION)) {
			try {
	            entityService.create(inMigration);
	        } catch (IllegalArgumentException e) {
	        } catch (SQLException e) {
	            throw new ConstraintViolations("There was a problem creating the in migration in the database");
	        }
	    }
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	public void createInMigrationImg(InMigration inMigration) throws ConstraintViolations {
		//checkValidIndividual(inMigration);

			try {
	            entityService.create(inMigration);
	        } catch (IllegalArgumentException e) {
	        } catch (SQLException e) {
	            throw new ConstraintViolations("There was a problem creating the in migration in the database");
	        }
	   
	}
	
	
	private void checkValidIndividual(InMigration inMigration) throws ConstraintViolations {
		if (inMigration.getMigType().equals(MigrationType.INTERNAL_INMIGRATION)
				&& !inMigration.isUnknownIndividual()) {
			return; // individual is already in database
		}
		
		individualService.evaluateIndividual(inMigration.getIndividual());
	}

	private void setResidencyFieldsFromInMigration(InMigration migration) {
		Residency residency = migration.getResidency();
        residency.setIndividual(migration.getIndividual());
        residency.setStartDate(migration.getRecordedDate());
    	if (migration.getMigType().equals(MigrationType.BASELINE)) {
         residency.setStartType(siteProperties.getEnumerationCode());
    	} else {
    	 residency.setStartType(siteProperties.getInmigrationCode());
    	}
        residency.setCollectedBy(migration.getCollectedBy());
        residency.setEndType(siteProperties.getNotApplicableCode());
        residency.setLocation(migration.getVisit().getVisitLocation());
	}
	
	public List<InMigration> getInMigrationsByIndividual(Individual individual) {
		return genericDao.findListByProperty(InMigration.class, "individual", individual, true);
	}
	
}
