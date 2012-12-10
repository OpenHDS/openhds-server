package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.RelationshipService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.transaction.annotation.Transactional;

public class RelationshipServiceImpl implements RelationshipService {
	
	private IndividualService individualService;
	private GenericDao genericDao;
	private SitePropertiesService siteProperties;
	private EntityService entityService;

	public RelationshipServiceImpl(GenericDao genericDao, EntityService entityService, IndividualService individualService, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.entityService = entityService;
		this.individualService = individualService;
		this.siteProperties = siteProperties;
	}
	
	public Relationship evaluateRelationship(Relationship entityItem) throws ConstraintViolations {
    	
    	if (!checkValidRelationship(entityItem)) 
    		throw new ConstraintViolations("An Individual cannot have multiple relationships with the same person.");	
    	if (individualService.getLatestEvent(entityItem.getIndividualA()).equals("Death") || individualService.getLatestEvent(entityItem.getIndividualB()).equals("Death") )
    		throw new ConstraintViolations("A Relationship cannot be created for an Individual who has a Death event.");	
    	if (!checkEndEventTypeForDeathOnCreate(entityItem))
			throw new ConstraintViolations("A Relationship cannot be saved because the end event type of Death cannot apply to Individuals who do not have a Death event.");	
    	
    	return entityItem;
	}	
	
	public Relationship checkRelationship(Relationship persistedItem, Relationship entityItem) throws ConstraintViolations {
		
		if (!compareDeathInRelationship(persistedItem, entityItem))
			throw new ConstraintViolations("A Relationship cannot be saved because an attempt was made to modify the end event type on an Individual who has a Death event.");	
		if (!checkEndEventTypeForDeathOnEdit(persistedItem, entityItem))
			throw new ConstraintViolations("A Relationship cannot be saved because the end event type of Death cannot apply to Individuals who do not have a Death event.");	
		
		return entityItem;
	}
	
	public void validateGeneralRelationship(Relationship relationship) throws ConstraintViolations {
		if (!checkValidRelationship(relationship)) 
    		throw new ConstraintViolations("An Individual cannot have multiple relationships with the same person.");	
	}
		
	/**
	 * Checks if a Relationship between Individual A and Individual B already exists.
	 * Also checks if a Relationship between Individual B and A already exists.
	 */
	public boolean checkValidRelationship(Relationship entityItem) {
			
		List<Relationship> itemsA = genericDao.findListByProperty(Relationship.class, "individualA", entityItem.getIndividualA());
		List<Relationship> itemsB = genericDao.findListByProperty(Relationship.class, "individualB", entityItem.getIndividualA());
		
		// compare lists
		for (Relationship relationA : itemsA) {	
			if ((!relationA.equals(entityItem)) && (relationA.getIndividualB().getExtId().equals(entityItem.getIndividualB().getExtId()))) 
				return false;
		}
		for (Relationship relationB : itemsB) {	
			if ((!relationB.equals(entityItem)) && (relationB.getIndividualA().getExtId().equals(entityItem.getIndividualB().getExtId()))) 
				return false;
		}
		return true;
	}
	
	/**
	 * Compares the persisted and (soon to be persisted) Relationship items.
	 * If the persisted item and entity item has a mismatch of an end event type
	 * and the persisted item has a end type of death, the edit cannot be saved.
	 */
	public boolean compareDeathInRelationship(Relationship persistedItem, Relationship entityItem) {
		
		if (individualService.getLatestEvent(persistedItem.getIndividualA()).equals("Death"))		
			if (persistedItem.getEndType().equals(siteProperties.getDeathCode()) && !entityItem.getEndType().equals(siteProperties.getDeathCode()))
				return false;
		
		if (individualService.getLatestEvent(persistedItem.getIndividualB()).equals("Death"))		
			if (persistedItem.getEndType().equals(siteProperties.getDeathCode()) && !entityItem.getEndType().equals(siteProperties.getDeathCode()))
				return false;
		
		return true;
	}
	
	/**
	 * Compares the persisted and (soon to be persisted) Relationship items.
	 * If the persisted item does not have an end type of Death and the entityItem does,
	 * the edit can only continue if one of the Individuals has a Death Event.
	 */
	public boolean checkEndEventTypeForDeathOnEdit(Relationship persistedItem, Relationship entityItem) {
		
		if (entityItem.getEndType().equals(siteProperties.getDeathCode()) && 
			!individualService.getLatestEvent(persistedItem.getIndividualA()).equals("Death") && 
			!individualService.getLatestEvent(persistedItem.getIndividualB()).equals("Death")) 
			return false;
		
		return true;
	}
	
	/**
	 * If the entityItem has an end event type of Death and the specified Individuals
	 * don't have a Death Event, the Relationship cannot be created.
	 */
	public boolean checkEndEventTypeForDeathOnCreate(Relationship entityItem) {
		
		if (entityItem.getEndType().equals(siteProperties.getDeathCode()) && !individualService.getLatestEvent(entityItem.getIndividualA()).equals("Death") && !individualService.getLatestEvent(entityItem.getIndividualB()).equals("Death")) 
			return false;
		
		return true;
	}
	
	public List<Relationship> getAllRelationships(Individual individual) {
		
		List<Relationship> itemsA = genericDao.findListByProperty(Relationship.class, "individualA", individual, true);
		List<Relationship> itemsB = genericDao.findListByProperty(Relationship.class, "individualB", individual, true);
		
		itemsA.addAll(itemsB);
		return itemsA;
	}
	
	public List<Relationship> getAllRelationshipsWithinSocialGroup(Individual individual, SocialGroup group) {
		
		List<Relationship> itemsA = genericDao.findListByProperty(Relationship.class, "individualA", individual);
		List<Relationship> itemsB = genericDao.findListByProperty(Relationship.class, "individualB", individual);
		
		itemsA.addAll(itemsB);
		
		 for (int i = 0; i < itemsA.size(); i++) {
	        	boolean found = false;
	        	Relationship rel = itemsA.get(i);  
	        	
	        	Individual indivA = rel.getIndividualA();
	        	Individual indivB = rel.getIndividualB();
	        	
	        	if (!group.getGroupHead().getExtId().equals(indivA.getExtId())) {
	        		List<Membership> memsA = genericDao.findListByProperty(Membership.class, "individual", indivA);		
		
	        		for(Membership m : memsA) {
	        			if (m.getSocialGroup().getExtId().equals(group.getExtId())) {
	        				found = true;
	        				continue;
	        			}
	        		}
	        		if (found == false)
	        			itemsA.remove(i);
	        	}
	        	
	        	if (!group.getGroupHead().getExtId().equals(indivB.getExtId())) {
	        		found = false;
	        		List<Membership> memsB = genericDao.findListByProperty(Membership.class, "individual", indivB);		
		
	        		for(Membership m : memsB) {
	        			if (m.getSocialGroup().getExtId().equals(group.getExtId())) {
	        				found = true;
	        				continue;
	        			}
	        		}
	        		if (found == false)
	        			itemsA.remove(i);
	        	}
		 }	
		 return itemsA;
	}

	@Override
	public List<Relationship> getAllRelationships() {
		return genericDao.findAll(Relationship.class, true);
	}

	@Override
	@Transactional
	public void createRelationship(Relationship relationship) throws ConstraintViolations {
		if (relationship.getEndType() == null) {
			relationship.setEndType(siteProperties.getNotApplicableCode());
		}
		
		evaluateRelationship(relationship);
		
		try {
			entityService.create(relationship);
		} catch (IllegalArgumentException e) {
		} catch (SQLException e) {
			throw new ConstraintViolations("There was a problem saving the relationship to the database");
		}
	}

    @Override
    @Authorized("VIEW_ENTITY")
    public List<Relationship> getAllRelationshipInRange(int i, int pageSize) {
        return genericDao.findPaged(Relationship.class, "individualA", i, pageSize);
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public long getTotalRelationshipCount() {
        return genericDao.getTotalCount(Relationship.class);
    }
}
