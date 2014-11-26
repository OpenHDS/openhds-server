package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.DeathService;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.dao.service.GenericDao;
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
		
		// added for testing purpose due o possible bugs on the simulator. Normally can not happening on real data collection.
		// if an individual was marked death will not be on the tablet.
		// If a death event is recorded on the tablet the individual disappear from the list and then a death event can not be recorded twice.
		if (!checkDuplicateIndividual(entityItem.getIndividual())) 
    		throw new ConstraintViolations("The Individual Id specified already exists.");	
		
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
        List<Relationship> relationshipsToUpdate = new ArrayList<Relationship>();
        
         if (!entityItem.getIndividual().getAllRelationships1().isEmpty()) {
            Set<Relationship> relationships = (Set<Relationship>) entityItem.getIndividual().getAllRelationships1();
            Iterator<Relationship> it = relationships.iterator();
            while (it.hasNext()) {
                Relationship rel = it.next();
                if (rel.getEndType().equals(siteProperties.getNotApplicableCode())) {
	                rel.setEndDate(endDate);
	                rel.setEndType(siteProperties.getDeathCode());
//	                entityService.save(rel);
	                relationshipsToUpdate.add(rel);
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
//		             entityService.save(rel);
		             relationshipsToUpdate.add(rel);
		         }
		     }
		 }
		 
         for(Relationship rel : relationshipsToUpdate){
        	 entityService.save(rel);
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
    			if (mem.getIndividual().getExtId() != death.getIndividual().getExtId()){
    				mem.setDeleted(true);
    				// TO CHECK IF IS OK. Which value to endType? DHH= Death Head of Household
    				//mem.setEndDate(death.getDeathDate());
    				//mem.setEndType("DHH");
    				//
    				
    				mem.setEndDate(death.getDeathDate());
    				mem.setEndType("DHH");    				
    				entityService.save(mem);
    			}
    			if (mem.getIndividual().getExtId()==mem.getSocialGroup().getGroupHead().getExtId()){
    				Membership memNew = new Membership();
					memNew.setDeleted(false);
    				memNew.setbIsToA("1");
    				memNew.setStartType(mem.getStartType());
    				memNew.setEndDate(null);
    				memNew.setIndividual(mem.getIndividual());
    				memNew.setSocialGroup(mem.getSocialGroup());
    				memNew.setCollectedBy(mem.getCollectedBy());
    				memNew.setEndType(siteProperties.getNotApplicableCode());
    				memNew.setInsertBy(mem.getInsertBy());
    				memNew.setStatus(siteProperties.getDataStatusValidCode());
    				memNew.setStartDate(death.getDeathDate());
    				entityService.create(memNew);
    			}
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
	
	public List<Death> getDeathsByIndividual(Individual individual) {
		return genericDao.findListByProperty(Death.class, "individual", individual, true);
	}
}
