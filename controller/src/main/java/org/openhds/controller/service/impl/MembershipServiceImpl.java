package org.openhds.controller.service.impl;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.MembershipService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;

public class MembershipServiceImpl implements MembershipService {
	
	private IndividualService individualService;
	private GenericDao genericDao;
	private SitePropertiesService siteProperties;

	public MembershipServiceImpl(GenericDao genericDao, IndividualService individualService, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.individualService = individualService;
		this.siteProperties = siteProperties;
	}
	
	public Membership evaluateMembership(Membership entityItem) throws ConstraintViolations {
		
		if (!checkDuplicateMembership(entityItem.getIndividual(), entityItem.getSocialGroup())) 
    		throw new ConstraintViolations("A Membership for the specified Social Group already exists.");	
    	if (individualService.getLatestEvent(entityItem.getIndividual()).equals("Death"))
    		throw new ConstraintViolations("A Membership cannot be created for an Individual who has a Death event.");
        		
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
			if (item.getSocialGroup().getExtId().equals(group.getExtId()) && !item.isDeleted())
				return false;
		}					
		return true;		
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
		
	public List<Membership> getAllMemberships(Individual individual) {
		
		List<Membership> items = genericDao.findListByProperty(Membership.class, "individual", individual, true);
		return items;
	}
}
