package org.openhds.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.SocialGroup;
import org.openhds.controller.service.DeathService;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.MembershipService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.web.service.WebFlowService;
import org.springframework.binding.message.MessageContext;

public class DeathHOHBean implements Serializable {

	private static final long serialVersionUID = 7098036821407255327L;
	
	// Used to keep track of which Social Group is being modified
	private int current = 0;
	
	// The Social Group being modified
	SocialGroup group;
	
	// The Individual who will replace the Group Head
	Individual selectedSuccessor;
				
	// A list of all successors in all Social Groups
	List<Individual> successors;
	
	// A lit of all Individuals in the Social Group
	List<Individual> indivsOfSocialGroup;
	
	// A list of all memberships to be re-established
	List<Membership> membershipList;
	
	// A mapping of of all the Memberships to the new successor based on the current Social Group being modified
	HashMap<Integer, List<Membership>> memberships;
		
	// A list of all the Social Groups that the Group Head is HOH of
	List<SocialGroup> socialGroups;
		
	GenericDao genericDao;
	EntityService entityService;
	SocialGroupService socialGroupService;
	DeathService deathService;
	MembershipService membershipService;
	WebFlowService webFlowService;
	
	/**
	 * Used to check if modifications to the Social Group can be made.
	 * The Social Group must have memberships in order to create a death event for the Group Head. 
	 * This method is called from the flow for validation purposes.
	 */
	public boolean checkValidHOH(MessageContext messageContext) {
		
		successors = new ArrayList<Individual>();
		indivsOfSocialGroup = new ArrayList<Individual>();
		socialGroups = new ArrayList<SocialGroup>();
		memberships = new HashMap<Integer, List<Membership>>();
		
		ArrayList<Membership> list = new ArrayList<Membership>();
		memberships.put(current, list);
		
		if (determineValidSocialGroups()) {
			webFlowService.createMessage(messageContext, 
			"The Group Head of the Social Group specified has 0 members in at least one of the Social Groups in which they are Head of. The Individual cannot have a Death event since a successor cannot be determined. To create a Death event for the Social Group Head, either appoint a new Head of Social Group or add more Memberships.");
			return false;
		}
				
		return true;	
	}
	
	/**
	 * Reinitialize membership list
	 */
	public void initializeMembershipList() {
		membershipList = new ArrayList<Membership>();
	}
	
	/**
	 * Checks for Membership counts in the Social Group. 
	 * The Social Group must have Memberships in order for modifications to be made.
	 */
	private boolean determineValidSocialGroups() {
		
    	List<Individual> indivsAtSocialGroup = null;

        socialGroups = filterDeleted(genericDao.findListByProperty(SocialGroup.class, "groupHead", group.getGroupHead())); 
              
        if (socialGroups.size() == 0) 
        	return true;
        
        for (SocialGroup item : socialGroups) {
        	 indivsAtSocialGroup = socialGroupService.getAllIndividualsOfSocialGroup(item);
        	 if (indivsAtSocialGroup.size() == 0)
        		 return true;
        }
		return false;
	}
	
	/**
	 * Method for filtering out the deleted Social Groups from the non-deleted ones.
	 */
	private List<SocialGroup> filterDeleted(List<SocialGroup> groups) {
		
		List<SocialGroup> list = new ArrayList<SocialGroup>();
		
		for (SocialGroup item : groups) {
			if (!item.isDeleted())
				list.add(item);
		}
		return list;
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
		
		if (memberships.get(current) == null && indivsOfSocialGroup.size() == 1)
			return true;
		
		if (memberships.size() > 0) {
			if (indivsOfSocialGroup.size() == 1 || memberships.get(current).size() >= indivsOfSocialGroup.size()-1) {
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
		
		Iterator<Individual> itr = indivsOfSocialGroup.iterator();
		
		while(itr.hasNext()) {
			Individual item = itr.next();
		
			if (membership.getIndividual().getExtId().equals(selectedSuccessor.getExtId())) {
				webFlowService.createMessage(messageContext, 
				"A new Membership cannot be created for the selected successor.");
				return false;
			}
			if (item.getExtId().equals(membership.getIndividual().getExtId()))
				return true;
		}
		
		webFlowService.createMessage(messageContext, 
		"A new Membership cannot be created for an Individual who is not a current Member of the Social Group.");
		return false;
	}
	
	/**
	 * Checks to make sure that all Individuals have a corresponding Membership.
	 */
	public boolean checkCorrespondingMemberships(MessageContext messageContext) {
				
		if (memberships.get(current).size() == indivsOfSocialGroup.size()-1) 
			return true;
			
		webFlowService.createMessage(messageContext, 
		"Cannot proceed with creating a Death for the Group Head if not all members of the Social Group have updated their Memberships to the new successor.");
		return false;
		
	}
	
	/**
	 * Ensures that a successor has been selected.
	 * This method is called from the flow for validation purposes.
	 */
	public boolean checkValidSuccessor(MessageContext messageContext) {
		
		try {
			if (successors.get(current) == null)
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
	 * Algorithm for finalizing the modifications to the Death of Group Head.
	 * 1. Create a death event for the Group Head, which means all Memberships, Residences, and Relationships become closed.
	 * 2. Set the selected successors to the Social Groups specified as the new Group Head and remove their Memberships from the Social Group.
	 * 3. Delete all of the old Memberships and create new ones to the newly appointed Group Head.
	 * All of this is done in one transaction.
	 */
	public boolean createDeathAndSetNewHead(MessageContext messageContext, Death death) {
		
		try {
			death.setIndividual(group.getGroupHead());
			deathService.createDeathAndSetNewHead(death, socialGroups, successors, memberships);
			return true;
		} catch (Exception e) {
			webFlowService.createMessage(messageContext, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Clear the bean.
	 */
	public void clear() {
		group = null;
		selectedSuccessor = null;
		successors = null;
		indivsOfSocialGroup = null;
		socialGroups = null;
		memberships = null;
		current = 0;
	}
	
	/**
	 * Get the number of Relationships that are part of the Social Group.
	 */
    public Integer getMembershipListSize() {
        if (getMembershipsOfSocialGroup() != null) {
            return getMembershipsOfSocialGroup().size();
        }
        return 0;
    }
    
    /**
     * Get all Memberships of the current Social Group.
     */
    public List<Membership> getMembershipsOfSocialGroup() {
    	return memberships.get(current);
    }
    
    /**
     * Get the number of Individuals that are part of the Social Group.
     */
    public int getSizeOfIndividualsOfSocialGroup() {
        if (getIndividualsOfSocialGroup() != null) {
            return getIndividualsOfSocialGroup().size();
        }
        return 0;
    }
    
    /**
	 * Get the list of all Individuals in the Social Group.
	 */
    public List<Individual> getIndividualsOfSocialGroup() {
    	
        if (group == null) {
            return null;
        }
        
        socialGroups = filterDeleted(genericDao.findListByProperty(SocialGroup.class, "groupHead", group.getGroupHead()));      
        indivsOfSocialGroup = socialGroupService.getAllIndividualsOfSocialGroup(getSocialGroup());
        Collections.sort(indivsOfSocialGroup, new IndividualComparator());

        return indivsOfSocialGroup;
    }
    
	/**
     * Get the number of Social Groups that the Group Head is Head of.
     */
    public int getNumOfSocialGroups() {
    	return filterDeleted(genericDao.findListByProperty(SocialGroup.class, "groupHead", group.getGroupHead())).size();
    }
    
    /**
     * When the Group Head is Head of multiple Social Groups, this method
     * is used to iterate through each one in order to make modifications.
     */
	public SocialGroup getSocialGroup() {
		Collections.sort(socialGroups, new SocialGroupComparator());
		
		try {
			group = socialGroups.get(current);
		} catch(Exception e) {
			return null;
		}
		
		return group;
	}
	
    /**
     * When the Group Head is Head of multiple Social Groups, this method
     * is used to iterate through each successor in order to make modifications
     * to the Social Group.
     */
	public Individual getSuccessor() {
		try {
			successors.get(current);
		} catch(Exception e) {
			return null;
		}
		return successors.get(current);
	}
		
	/**
	 * Adds a new Membership to the HashMap
	 */
	public void addMembership(Membership mem) {
		membershipList.add(mem);
		memberships.put(current, membershipList);
	}
	
	/**
	 * Method for setting the successor of a Social Group at the current index.
	 * This method is called from the flow whenever a new successor has been selected.
	 * It reads in the uuid of the Individual and fetches it from the database.
	 */
	public void setSuccessor(String individualId) {
		if (individualId != "") {
			this.successors.add(current, genericDao.read(Individual.class, individualId));
			selectedSuccessor = successors.get(current);
		}
		else {
			this.successors.set(current, null);
			selectedSuccessor = null;
		}
	}
	
	/**
	 * Increment the counter and populate the HashMap.
	 */
	public void incCurrent() {
		current++;
		ArrayList<Membership> list = new ArrayList<Membership>();
		memberships.put(current, list);
	}
		
	public int getCurrent() {
		return current+1;
	}
		
	public List<SocialGroup> getSocialGroups() {
		return socialGroups;
	}

	public void setSocialGroups(List<SocialGroup> socialGroups) {
		this.socialGroups = socialGroups;
	}
	
    public List<Individual> getIndivsOfSocialGroup() {
		return indivsOfSocialGroup;
	}

	public void setIndivsOfSocialGroup(List<Individual> indivsOfSocialGroup) {
		this.indivsOfSocialGroup = indivsOfSocialGroup;
	}
	
	public List<Individual> getSuccessors() {
		return successors;
	}
	
	public void setSuccessors(List<Individual> successors) {
		this.successors = successors;
	}

	public HashMap<Integer, List<Membership>> getMemberships() {
		return memberships;
	}

	public void setMemberships(HashMap<Integer, List<Membership>> memberships) {
		this.memberships = memberships;
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
		
	public DeathService getDeathService() {
		return deathService;
	}

	public void setDeathService(DeathService deathService) {
		this.deathService = deathService;
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
	
    private class IndividualComparator implements Comparator<Individual> {

        public int compare(Individual indiv1, Individual indiv2) {
            return indiv1.getExtId().compareTo(indiv2.getExtId());
        }
    }
	
	private class SocialGroupComparator implements Comparator<SocialGroup> {

	    public int compare(SocialGroup group1, SocialGroup group2) {
	        return group1.getExtId().compareTo(group2.getExtId());
	    }
	}
}
