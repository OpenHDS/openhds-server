package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.beans.RecordGroup;
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
	public void setDeathsForAgeGroupsByLocation(RecordGroup deathGroup, List<String> hierarchyIds) {
				
		List<Death> deaths = genericDao.findAll(Death.class, true);
		
		for (Death death : deaths) {
			
			if (hierarchyIds.contains(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId())) {

				long ageInDays = death.getAgeAtDeath();
				long ageInYears = (long) (ageInDays / 365.25);
				Calendar deathDate = death.getDeathDate();
				
				if ((deathDate.after(deathGroup.getStart()) || deathDate.equals(deathGroup.getStart())) && 
					(deathDate.before(deathGroup.getEnd()))) {
				
					// 0 to 28 days
					if (ageInDays > 0 && ageInDays <= 28) 	
						processGroup(deathGroup, death, 0);
					// 29 to 11 months (334.81 days in 11 months)
					else if (ageInDays > 28 && ageInDays <= 334.81) 
						processGroup(deathGroup, death, 1);
					// 12 - 59 months 
					else if (ageInDays > 334.81 && ageInDays <= 1826.21) 
						processGroup(deathGroup, death, 2);
					// 5 to 9 years
					else if (ageInYears > 5 && ageInYears < 10) 
						processGroup(deathGroup, death, 3);
					// 10 to 19 years
					else if (ageInYears >= 10 && ageInYears < 20) 
						processGroup(deathGroup, death, 4);
					// 20 to 40 years
					else if (ageInYears >= 20 && ageInYears < 40) 
						processGroup(deathGroup, death, 5);
					// > 40 years
					else if (ageInYears >= 40) 
						processGroup(deathGroup, death, 6);
					
					if (ageInYears >= 0 && ageInYears < 5) 
						processGroup(deathGroup, death, 7);
					else if (ageInYears >= 10 && ageInYears < 15) 
						processGroup(deathGroup, death, 8);
					else if (ageInYears >= 15 && ageInYears < 20) 
						processGroup(deathGroup, death, 9);
					else if (ageInYears >= 20 && ageInYears < 25) 
						processGroup(deathGroup, death, 10);				
					else if (ageInYears >= 25 && ageInYears < 30) 
						processGroup(deathGroup, death, 11);			
					else if (ageInYears >= 30 && ageInYears < 35) 
						processGroup(deathGroup, death, 12);		
					else if (ageInYears >= 35 && ageInYears < 40) 
						processGroup(deathGroup, death, 13);		
					else if (ageInYears >= 40 && ageInYears < 45) 
						processGroup(deathGroup, death, 14);		
					else if (ageInYears >= 45 && ageInYears < 50) 
						processGroup(deathGroup, death, 15);	
					else if (ageInYears >= 50 && ageInYears < 55) 
						processGroup(deathGroup, death, 16);		
					else if (ageInYears >= 55 && ageInYears < 60) 
						processGroup(deathGroup, death, 17);		
					else if (ageInYears >= 60 && ageInYears < 65) 
						processGroup(deathGroup, death, 18);		
					else if (ageInYears >= 65 && ageInYears < 70) 
						processGroup(deathGroup, death, 19);		
					else if (ageInYears >= 70 && ageInYears < 75) 
						processGroup(deathGroup, death, 20);			
					else if (ageInYears >= 75 && ageInYears < 80) 
						processGroup(deathGroup, death, 21);		
					else if (ageInYears >= 80 && ageInYears < 85) 
						processGroup(deathGroup, death, 22);		
					else if (ageInYears >= 85 && ageInYears < 90) 
						processGroup(deathGroup, death, 23);		
					else if (ageInYears >= 90 && ageInYears < 95) 
						processGroup(deathGroup, death, 24);		
					else if (ageInYears >= 95) 
						processGroup(deathGroup, death, 25);
				}
			}
		}
	}
	
	private void processGroup(RecordGroup group, Death death, int index) {
		if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
			group.getRecord().addMaleCountForLocationAndAgeGroup(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId(), index);
		else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
			group.getRecord().addFemaleCountForLocationAndAgeGroup(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId(), index);
	}
	
	public List<Death> getDeathsByIndividual(Individual individual) {
		return genericDao.findListByProperty(Death.class, "individual", individual, true);
	}
}
