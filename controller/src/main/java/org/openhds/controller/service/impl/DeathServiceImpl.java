package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.beans.DeathRecordGroup;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.DeathService;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.transaction.annotation.Transactional;

public class DeathServiceImpl implements DeathService {
	
	private static final int MILLISECONDS_IN_DAY = 86400000;
	private EntityService entityService;
	private GenericDao genericDao;
	private IndividualService individualService;
	private SitePropertiesService siteProperties;
	
	public DeathServiceImpl(GenericDao genericDao, IndividualService individualService, EntityService entityService, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.individualService = individualService;
		this.entityService = entityService;
		this.siteProperties = siteProperties;
	}

	public Death evaluateDeath(Death entityItem) throws ConstraintViolations {
		
		if (!checkDuplicateIndividual(entityItem.getIndividual())) 
    		throw new ConstraintViolations("The Individual Id specified already exists.");	
		if (!checkHeadOfSocialGroup(entityItem.getIndividual())) 
    		throw new ConstraintViolations("The Individual specified is a Head of a Social Group. If you wish to create a Death event for this Individual, go to the Utility Menu and create a new Death event.");	
		
		return entityItem;
	}
		
	@Transactional(rollbackFor=Exception.class)
	public Death createDeath(Death entityItem) throws ConstraintViolations, SQLException {
		Calendar endDate = entityItem.getDeathDate();
		
		if (entityItem.getIndividual().getCurrentResidency() != null) {
			Residency residency = entityItem.getIndividual().getCurrentResidency();
			residency.setEndDate(endDate);
			residency.setEndType(siteProperties.getDeathCode());
			entityService.save(residency);
		}

		Long ageAtDeath = (endDate.getTimeInMillis() - entityItem.getIndividual().getDob().getTimeInMillis())/MILLISECONDS_IN_DAY;
		entityItem.setAgeAtDeath(ageAtDeath);
		
        //Gets the individual's memberships if any
        // Iterates through memberships and sets endType(DEATH) and endDate
        if (!entityItem.getIndividual().getAllMemberships().isEmpty()) {
            Set<Membership> memberships = (Set<Membership>) entityItem.getIndividual().getAllMemberships();
            for (Membership mem : memberships) {
            	if (mem.getEndType().equals(siteProperties.getNotApplicableCode())) {
	                mem.setEndDate(endDate);
	                mem.setEndType(siteProperties.getDeathCode());
	                entityService.save(mem);
            	}
            }
        }

        //Gets the individual's relationships if any
        // Iterates through the relationships and sets endType(DEATH) and endDate
         if (!entityItem.getIndividual().getAllRelationships1().isEmpty()) {
            Set<Relationship> relationships = (Set<Relationship>) entityItem.getIndividual().getAllRelationships1();
            Iterator<Relationship> it = relationships.iterator();
            while (it.hasNext()) {
                Relationship rel = it.next();
                if (rel.getEndType().equals(siteProperties.getNotApplicableCode())) {
	                rel.setEndDate(endDate);
	                rel.setEndType(siteProperties.getDeathCode());
	                entityService.save(rel);
                }
            }
        }
         
		 if (!entityItem.getIndividual().getAllRelationships2().isEmpty()) {
		     Set<Relationship> relationships = (Set<Relationship>) entityItem.getIndividual().getAllRelationships2();
		     Iterator<Relationship> it = relationships.iterator();
		     while (it.hasNext()) {
		         Relationship rel = it.next();
		         if (rel.getEndType().equals(siteProperties.getNotApplicableCode())) {
		             rel.setEndDate(endDate);
		             rel.setEndType(siteProperties.getDeathCode());
		             entityService.save(rel);
		         }
		     }
		 }
         
         entityService.create(entityItem);

         return entityItem;
	}

	@Transactional(rollbackFor=Exception.class)
	public void deleteDeath(Death entityItem) {
		
		if (entityItem.getIndividual().getCurrentResidency() != null && entityItem.getIndividual().getCurrentResidency().getEndType().equals(siteProperties.getDeathCode())) {
			entityItem.getIndividual().getCurrentResidency().setEndDate(null);
			entityItem.getIndividual().getCurrentResidency().setEndType(siteProperties.getNotApplicableCode());
		}

        if (!entityItem.getIndividual().getAllMemberships().isEmpty()) {
            Set<Membership> memberships = (Set<Membership>) entityItem.getIndividual().getAllMemberships();
            for (Membership mem : memberships) {
            	if (mem.getEndType().equals(siteProperties.getDeathCode())) {
	                mem.setEndDate(null);
	                mem.setEndType(siteProperties.getNotApplicableCode());
            	}
            }
        }

         if (!entityItem.getIndividual().getAllRelationships1().isEmpty()) {
            Set<Relationship> relationships = (Set<Relationship>) entityItem.getIndividual().getAllRelationships1();
            Iterator<Relationship> it = relationships.iterator();
            while (it.hasNext()) {
                Relationship rel = it.next();
                if (!individualService.getLatestEvent(rel.getIndividualB()).equals("Death") && rel.getEndType().equals(siteProperties.getDeathCode())) {
	                rel.setEndDate(null);
	                rel.setEndType(siteProperties.getNotApplicableCode());
                }
            }
        }
         
         if (!entityItem.getIndividual().getAllRelationships2().isEmpty()) {
             Set<Relationship> relationships = (Set<Relationship>) entityItem.getIndividual().getAllRelationships2();
             Iterator<Relationship> it = relationships.iterator();
             while (it.hasNext()) {
                 Relationship rel = it.next();
                 if (!individualService.getLatestEvent(rel.getIndividualA()).equals("Death") && rel.getEndType().equals(siteProperties.getDeathCode())) {
	                 rel.setEndDate(null);
	                 rel.setEndType(siteProperties.getNotApplicableCode());
                 }
             }
         } 
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void createDeathAndSetNewHead(Death death, List<SocialGroup> groups, List<Individual> successors, HashMap<Integer, List<Membership>> memberships) throws ConstraintViolations, SQLException, Exception {
		
		// Create the death event for the Group Head
		entityService.create(createDeath(death));
		
    	// Set the successor as the new Group Head
    	for (int i = 0; i < groups.size(); i++) { 		
    		groups.get(i).setGroupHead(successors.get(i));
    		entityService.save(groups.get(i));	
    	}	
    	
    	// Remove all Memberships from all Social Groups
    	for (SocialGroup item : groups) {
    		
    		Set<Membership> mems = item.getMemberships();  		
    		Iterator<Membership> itr = mems.iterator();
    		
    		while(itr.hasNext()) {
    			Membership mem = itr.next();
    			mem.setDeleted(true);
    			entityService.save(mem);
    		}
    	}
    	
    	// Create new Memberships 
    	for (List<Membership> list : memberships.values()) {	
    		for (Membership mem : list) {
    			entityService.create(mem);
    		}
    	}
    	
    }
	
	/**
	 * Check for duplicate Individuals entered
	 */
	public boolean checkDuplicateIndividual(Individual indiv) {
		
		List<Death> list = genericDao.findListByProperty(Death.class, "individual", indiv);	
		
		for (Death item : list) {		
			if (!item.isDeleted())
				return false;
		}
		return true;		
	}
	
	/**
	 * Checks if the Individual is the head of any Social Group
	 */
	public boolean checkHeadOfSocialGroup(Individual indiv) {
		
		List<SocialGroup> list = genericDao.findListByProperty(SocialGroup.class, "groupHead", indiv);		
		
		if (list.size() == 0)
			return true;
		return false;
	}
	
	// this is used for output to the DHIS
	// iterates through the deaths and categorizes them according to age group
	public void setDeathsForAgeGroupsByLocation(DeathRecordGroup deathGroup, List<String> locations) {
				
		List<Death> deaths = genericDao.findAll(Death.class, true);
		
		for (Death death : deaths) {
			
			if (locations.contains(death.getVisitDeath().getVisitLocation().getExtId())) {

				long ageInDays = death.getAgeAtDeath();
				long ageInYears = (long) (ageInDays / 365.25);
				Calendar deathDate = death.getDeathDate();
				
				if ((deathDate.after(deathGroup.getStart()) || deathDate.equals(deathGroup.getStart())) && 
					(deathDate.before(deathGroup.getEnd()))) {
				
					// 0 to 28 days
					if (ageInDays > 0 && ageInDays <= 28) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(0).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(0).addFemaleCount();
						deathGroup.getDeaths().get(0).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// 29 to 11 months (334.81 days in 11 months)
					else if (ageInDays > 28 && ageInDays <= 334.81) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(1).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(1).addFemaleCount();
						deathGroup.getDeaths().get(1).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// 12 - 59 months 
					else if (ageInDays > 334.81 && ageInDays <= 1826.21) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(2).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(2).addFemaleCount();
						deathGroup.getDeaths().get(2).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// 5 to 9 years
					else if (ageInYears > 5 && ageInYears < 10) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(3).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(3).addFemaleCount();
						deathGroup.getDeaths().get(3).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// 10 to 19 years
					else if (ageInYears >= 10 && ageInYears < 20) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(4).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(4).addFemaleCount();
						deathGroup.getDeaths().get(4).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// 20 to 40 years
					else if (ageInYears >= 20 && ageInYears < 40) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(5).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(5).addFemaleCount();
						deathGroup.getDeaths().get(5).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
					// > 40 years
					else if (ageInYears >= 40) {
						if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
							deathGroup.getDeaths().get(6).addMaleCount();
						else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
							deathGroup.getDeaths().get(6).addFemaleCount();
						deathGroup.getDeaths().get(6).setLocationExtId(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId());
					}
				}
			}
		}
	}
	
	public List<Death> getDeathsByIndividual(Individual individual) {
		return genericDao.findListByProperty(Death.class, "individual", individual, true);
	}
}
