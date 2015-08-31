package org.openhds.web.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.SocialGroup;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.MembershipService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.web.service.WebFlowService;
import org.springframework.binding.message.MessageContext;

public class ModifyHOHBean implements Serializable {

	private static final long serialVersionUID = 7407897054774809175L;
	
	// The Social Group being modified
	SocialGroup group;
	
	// The Individual who will replace the Group Head
	Individual selectedSuccessor;
					
	// A lit of all Memberships in the Social Group
	List<Membership> membershipsOfSocialGroup;
	
	// A mapping of of all the Memberships to the new successor based on the current Social Group being modified
	List<Membership> newMemberships;
				
	GenericDao genericDao;
	EntityService entityService;
	SocialGroupService socialGroupService;
	MembershipService membershipService;
	WebFlowService webFlowService;

	/**
	 * Used to check if modifications to the Social Group can be made.
	 * The Social Group must have memberships in order to modify the Group Head.
	 * This method is called from the flow for validation purposes.
	 */
	public boolean checkValidHOH(MessageContext messageContext) {
		
		membershipsOfSocialGroup = new ArrayList<Membership>();
		newMemberships = new ArrayList<Membership>();
		
		if (!determineValidSocialGroup()) {
			webFlowService.createMessage(messageContext, 
			"The Social Group specified has 0 members. The Group Head cannot be modifyed if no successor is able to be a replacement. ");
			return false;
		}
				
		return true;	
	}
	
	/**
	 * Checks for Membership counts in the Social Group. 
	 * The Social Group must have Memberships in order for modifications to be made.
	 */
	private boolean determineValidSocialGroup() {
		
    	List<Individual> indivsAtSocialGroup = null;

        SocialGroup socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", group.getExtId(), true);      
        if (socialGroup == null) 
        	return false;
        
        indivsAtSocialGroup = socialGroupService.getAllIndividualsOfSocialGroup(socialGroup);
    	if (indivsAtSocialGroup.size() == 0)
    		 return false;
        
		return true;
	}
		
	/**
	 * Ensures that a successor has been selected before attempting to create a new Membership.
	 */
	public boolean determineValidSuccessorBeforeCreatingMembership(MessageContext messageContext, String param) {
		
		if (param.equals("false")) {
			webFlowService.createMessage(messageContext, 
			"A new Membership cannot be created if the successor hasn't been selected first.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if a Membership can be created. A Membership cannot be created
	 * if there is only 1 current Membership or if all of the corresponding 
	 * Individuals within the Social Group have a corresponding Membership.
	 */
	public boolean determineValidMembershipToCreate(MessageContext messageContext) {
		
		if (newMemberships == null && membershipsOfSocialGroup.size() == 1)
			return true;
		
		if (newMemberships.size() > 0) {
			if (membershipsOfSocialGroup.size() == 1 || newMemberships.size() >= membershipsOfSocialGroup.size()) {
				webFlowService.createMessage(messageContext, 
				"A new Membership cannot be created if the successor is the only member of the Social Group or if all members within the Social Group have been assigned new relations to the Group Head.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * When creating a new Membership, special validation occurs to restrict the Individual
	 * to already have a Membership of the current Social Group. Also, the Individual cannot
	 * be the same as the new successor to the Group.
	 */
	public boolean checkValidMembershipToCreate(Membership membership, MessageContext messageContext) {
		
		Iterator<Membership> itr = membershipsOfSocialGroup.iterator();
		
		while(itr.hasNext()) {
			Membership item = itr.next();
		
			if (membership.getIndividual().getExtId().equals(selectedSuccessor.getExtId())) {
				webFlowService.createMessage(messageContext, 
				"A new Membership cannot be created for the selected successor.");
				return false;
			}
			if (item.getIndividual().getExtId().equals(membership.getIndividual().getExtId()))
				return true;
		}
		
		if (membership.getIndividual().getExtId().equals(group.getGroupHead().getExtId()))
			return true;
		
		webFlowService.createMessage(messageContext, 
		"A new Membership cannot be created for an Individual who is not a current Member of the Social Group.");
		return false;
	}
	
	/**
	 * Checks to make sure that all Individuals have a corresponding Membership.
	 */
	public boolean checkCorrespondingMemberships(MessageContext messageContext) {
				
		if ((newMemberships.size() == membershipsOfSocialGroup.size()) ) 
			return true;
			
		webFlowService.createMessage(messageContext, 
		"Cannot proceed with modifying the Group Head if not all members of the Social Group have updated their Memberships to the new successor. Remember to create a new Membership for the old Group Head.");
		return false;
		
	}
	
	/**
	 * Ensures that a successor has been selected.
	 * This method is called from the flow for validation purposes.
	 */
	public boolean checkValidSuccessor(MessageContext messageContext) {
		
		try {
			if (selectedSuccessor == null)
				throw new Exception();
		} 
		catch (Exception e) {
			webFlowService.createMessage(messageContext, 
			"The Successor Individual Id must be specified.");
			return false;
		}
		return true;	
	}
		
	/**
	 * Algorithm for finalizing the modifications to Modifying the Group Head.
	 * 1. Set the selected successor to the Social Group specified as the new Group Head and remove their Memberships from the Social Group.
	 * 2. Delete all of the old Memberships and create new ones to the newly appointed Group Head.
	 * All of this is done in one transaction.
	 */
	public void modifySocialGroupHead() throws ConstraintViolations, SQLException, Exception {
		socialGroupService.modifySocialGroupHead(group, selectedSuccessor, newMemberships);
    }
	
	/**
	 * Clear the bean.
	 */
	public void clear() {
		group = null;
		selectedSuccessor = null;
		membershipsOfSocialGroup = null;
		newMemberships = null;
	}
	
	/**
	 * Get the number of Memberships that are part of the Social Group.
	 */
    public Integer getMembershipListSize() {
        if (getMembershipsOfSocialGroup() != null) {
            return getMembershipsOfSocialGroup().size();
        }
        return 0;
    }
        
    /**
     * Get the number of Memberships that are part of the Social Group.
     */
    public int getSizeOfMembershipsOfSocialGroup() {
        if (getMembershipsOfSocialGroup() != null) {
            return getMembershipsOfSocialGroup().size();
        }
        return 0;
    }
    
	/**
	 * Get the number of new Memberships that are part of the Social Group being modified.
	 */
    public Integer getNewMembershipListSize() {
        if (getNewMemberships() != null) {
            return getNewMemberships().size();
        }
        return 0;
    }      
    
    /**
	 * Get the list of all Memberships in the Social Group.
	 */
	public List<Membership> getMembershipsOfSocialGroup() {
    	
        if (group == null) {
            return null;
        }

        group = genericDao.findByProperty(SocialGroup.class, "extId", group.getExtId(), true);             
        membershipsOfSocialGroup = genericDao.findListByMultiProperty(Membership.class, getValueProperty("socialGroup", group), 
        																				getValueProperty("deleted", false));
        
        List<Membership> validMemberships = new ArrayList<Membership>();
        for (Membership item : membershipsOfSocialGroup) {
			if (item.getEndDate() == null)
				validMemberships.add(item);
		}
        
        return validMemberships;
    }
		
	/**
	 * Adds a new Membership
	 */
	public void addMembership(Membership mem) {
		newMemberships.add(mem);
	}
	
	/**
	 * Method for setting the successor of a Social Group.
	 * This method is called from the flow whenever a new successor has been selected.
	 * It reads in the uuid of the Membership and fetches it from the database.
	 */
	public void setSuccessor(String membershipId) {
		if (membershipId != "") {
			Membership item = genericDao.read(Membership.class, membershipId);
			selectedSuccessor = genericDao.read(Individual.class, item.getIndividual().getUuid());
		}
		else 
			selectedSuccessor = null;
	}
	
	private GenericDao.ValueProperty getValueProperty(final String propertyName, final Object propertyValue) {
		return new ValueProperty() {

            public String getPropertyName() {
                return propertyName;
            }

            public Object getValue() {
                return propertyValue;
            }
        };		
	}
	
	public void setIndivsOfSocialGroup(List<Membership> membershipsOfSocialGroup) {
		this.membershipsOfSocialGroup = membershipsOfSocialGroup;
	}
						
	public SocialGroup getGroup() {
		return group;
	}

	public void setGroup(SocialGroup group) {
		this.group = group;
	}
			
	public Individual getSelectedSuccessor() {
		return selectedSuccessor;
	}

	public void setSelectedSuccessor(Individual selectedSuccessor) {
		this.selectedSuccessor = selectedSuccessor;
	}
	
	public List<Membership> getMemberships() {
		return newMemberships;
	}

	public void setMemberships(List<Membership> newMemberships) {
		this.newMemberships = newMemberships;
	}
	
	public List<Membership> getNewMemberships() {
		return newMemberships;
	}

	public void setNewMemberships(List<Membership> newMemberships) {
		this.newMemberships = newMemberships;
	}

	public GenericDao getGenericDao() {
		return genericDao;
	}
	
	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public WebFlowService getWebFlowService() {
		return webFlowService;
	}
	
	public void setWebFlowService(WebFlowService webFlowService) {
		this.webFlowService = webFlowService;
	}
		
	public SocialGroupService getSocialGroupService() {
		return socialGroupService;
	}

	public void setSocialGroupService(SocialGroupService socialGroupService) {
		this.socialGroupService = socialGroupService;
	}
	
	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}
	
	public MembershipService getMembershipService() {
		return membershipService;
	}

	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}
}
