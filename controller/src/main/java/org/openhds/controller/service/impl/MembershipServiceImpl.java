package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.MembershipService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.transaction.annotation.Transactional;

public class MembershipServiceImpl implements MembershipService {
	
	private IndividualService individualService;
	private GenericDao genericDao;
	private SitePropertiesService siteProperties;
    private EntityService entityService;

	public MembershipServiceImpl(GenericDao genericDao, EntityService entityService, IndividualService individualService, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.entityService = entityService;
		this.individualService = individualService;
		this.siteProperties = siteProperties;
	}
	
	public Membership evaluateMembership(Membership entityItem) throws ConstraintViolations {
	    if (!checkDuplicateMembership(entityItem.getIndividual(), entityItem.getSocialGroup())) 
    		throw new ConstraintViolations("A Membership for the specified Social Group already exists.");	
    	if (individualService.getLatestEvent(entityItem.getIndividual()).equals("Death"))
    		throw new ConstraintViolations("A Membership cannot be created for an Individual who has a Death event.");
		if(!checkOverlappingMemberships(entityItem))
			throw new ConstraintViolations("A Membership cannot be created because it is overlapping with an already existing membership.");
    	return entityItem;
	}

	public Membership checkMembership(Membership persistedItem, Membership entityItem) throws ConstraintViolations {
		
		if (!compareDeathInMembership(persistedItem, entityItem))
			throw new ConstraintViolations("A Membership cannot be saved because an attempt was made to modify the end event type on an Individual who has a Death event.");	
		if (!checkEndEventTypeForMembershipOnEdit(persistedItem, entityItem))
			throw new ConstraintViolations("A Membership cannot be saved because the end event type of Death cannot apply to Individuals who do not have a Death event.");	
		return entityItem;
	}
	
	/**
	 * Checks if a duplicate Membership already exists
	 */
	public boolean checkDuplicateMembership(Individual indiv, SocialGroup group) {
		
		List<Membership> list = genericDao.findListByProperty(Membership.class, "individual", indiv);
		
		Iterator<Membership> itr = list.iterator();
		
		while(itr.hasNext()) {
			Membership item = itr.next();
			if (item.getSocialGroup().getExtId().equals(group.getExtId()) && !item.isDeleted() && item.getEndDate()==null) 
				return false;
		}					
		return true;		
	}
	
	/**
	 * Checks if an existing Membership for the specific individual overlaps with the potential membership to be inserted
	 */
	public boolean checkOverlappingMemberships(Membership persistedItem) {
		
		List<Membership> list = genericDao.findListByProperty(Membership.class, "individual", persistedItem.getIndividual());
		
		Iterator<Membership> itr = list.iterator();
		
		while(itr.hasNext()) {
			Membership item = itr.next();
			if (!item.isDeleted() && item.getEndDate()==null && 
					item.getStartDate() != null && item.getStartDate().before(persistedItem.getStartDate())) 
				return true;
		}					
		return false;		
	}	
	
	/**
	 * Compares the persisted and (soon to be persisted) Membership items.
	 * If the persisted item does not have an end type of Death and the entityItem does,
	 * the edit can only continue if one of the Individuals has a Death Event.
	 */
	public boolean checkEndEventTypeForMembershipOnEdit(Membership persistedItem, Membership entityItem) {
		
		if (entityItem.getEndType().equals(siteProperties.getDeathCode()) && !individualService.getLatestEvent(persistedItem.getIndividual()).equals("Death") && !individualService.getLatestEvent(persistedItem.getIndividual()).equals("Death")) 
			return false;
		
		return true;
	}
	
	/**
	 * Compares the persisted and (soon to be persisted) Membership items.
	 * If the persisted item and entity item has a mismatch of an end event type
	 * and the persisted item has a end type of death, the edit cannot be saved.
	 */
	public boolean compareDeathInMembership(Membership persistedItem, Membership entityItem) {
		
		if (individualService.getLatestEvent(persistedItem.getIndividual()).equals("Death"))		
			if (persistedItem.getEndType().equals(siteProperties.getDeathCode()) && !entityItem.getEndType().equals(siteProperties.getDeathCode()))
				return false;
		
		return true;
	}
	
    /**
     * Helper method for creating a membership.
     * NOTE: This is only being used by the pregnancy outcome web service method
     * @param startDate
     * @param individual
     * @param sg
     * @param fw
     * @param relationToGroupHead
     * @return
     */
    public Membership createMembershipForPregnancyOutcome(Calendar startDate, Individual individual, SocialGroup sg, FieldWorker fw, String relationToGroupHead) {
        Membership membership = new Membership();
        membership.setStartDate(startDate);
        membership.setIndividual(individual);
        membership.setSocialGroup(sg);
        membership.setCollectedBy(fw);
        membership.setbIsToA(relationToGroupHead);
        membership.setStartType(siteProperties.getBirthCode());
        membership.setEndType(siteProperties.getNotApplicableCode());

        return membership;
    }
    
	public void validateGeneralMembership(Membership membership) throws ConstraintViolations {
		if (individualIsHeadOfSocialGroup(membership.getIndividual(), membership.getSocialGroup())) {
    		throw new ConstraintViolations("A Membership cannot be created for an Individual who is the head of the Social Group");
    	}
	}
			
	public List<Membership> getAllMemberships(Individual individual) {
		List<Membership> items = genericDao.findListByProperty(Membership.class, "individual", individual, true);
		return items;
	}
	
	/**
	 * Determine whether the Individual is the head of the Social Group. 

	 * @param individual
	 * @param socialGroup
	 * 
	 * @return true is the Individual is the head of the Social Group
	 */
	private boolean individualIsHeadOfSocialGroup(Individual individual, SocialGroup socialGroup) {
		return socialGroup.getGroupHead().getExtId().equals(individual.getExtId());
	}

    @Override
    @Transactional
    public void createMembership(Membership item) throws ConstraintViolations {

        // assume a default start type of in migration
        if (item.getStartType() == null) {
            item.setStartType(siteProperties.getInmigrationCode());
        }
        
        if (item.getEndType() == null) {
            item.setEndType(siteProperties.getNotApplicableCode());
        }
        
        evaluateMembership(item);
        //Gets the individual's memberships if any
        // Iterates through memberships and sets endType(OMG) and endDate
        if (!item.getIndividual().getAllMemberships().isEmpty()) {
            Set<Membership> memberships = (Set<Membership>) item.getIndividual().getAllMemberships();
            for (Membership mem : memberships) {
            	if (mem.getEndType().equals(siteProperties.getNotApplicableCode())) {
            		mem.setEndDate(item.getStartDate());
	                mem.setEndType(siteProperties.getOutmigrationCode());
	                try {
						entityService.save(mem);
					} catch (SQLException e) {
						 throw new ConstraintViolations(
				                    "There as a problem updating the database with the membership associated with the out migration");
					}
            	}
            }
        }
        
        
        try {
            entityService.create(item);
        } catch (IllegalArgumentException e) {
        } catch (SQLException e) {
        }
    }
}
