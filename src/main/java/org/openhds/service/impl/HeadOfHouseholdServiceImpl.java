package org.openhds.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.DeathService;
import org.openhds.service.EntityService;
import org.openhds.service.HeadOfHouseholdService;
import org.openhds.service.MembershipService;
import org.openhds.service.SocialGroupService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.Death;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.HeadOfHousehold;
import org.openhds.domain.Individual;
import org.openhds.domain.Membership;
import org.openhds.domain.SocialGroup;
import org.openhds.service.SitePropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("headOfHouseholdServiceImpl")
public class HeadOfHouseholdServiceImpl implements HeadOfHouseholdService {
	
	@Autowired
    private GenericDao genericDao;
	@Autowired
    private EntityService entityService;
	@Autowired
    private SitePropertiesService siteProperties;	
	@Autowired
	MembershipService membershipService;
	@Autowired
	SocialGroupService socialGroupService;
	@Autowired 
	DeathService deathService;

	@Override
	public HeadOfHousehold evaluateHeadOfHousehold(HeadOfHousehold entityItem)
			throws ConstraintViolations {
		
		if(entityItem == null)
			throw new ConstraintViolations("Please provide a valid entityItem.");	

		SocialGroup sg = entityItem.getSocialGroup(); //getSocialGroup(entityItem);
		Individual oldHoh = getIndividualByExtId(entityItem.getOldHoh().getExtId());
		
		if(oldHoh == null){
			throw new ConstraintViolations("No old Individual with this extId found!");
		}
				
		String newHohExtId = entityItem.getNewHoh().getExtId();
		
		if(newHohExtId.isEmpty()){
			throw new ConstraintViolations("No new Individual as new HoH specified!");
		}
		Individual newHoh = getIndividualByExtId(newHohExtId);
		
		if(newHoh == null){
			throw new ConstraintViolations("No new Individual with this extId found!");
		}
		
		boolean isValidSocialGroup = determineValidSocialGroup(sg);
		
//		if(isValidSocialGroup)
//			System.out.println("Socialgroup seems to be valid!");
//		else
//			System.out.println("Socialgroup DOES NOT SEEM TO BE VALID !!!");
		
		
		if(sg != null){
			if(sg.getGroupHead().getExtId().trim().equalsIgnoreCase(oldHoh.getExtId().trim())){
			}
			else{
				throw new ConstraintViolations("Specified old HoH-extId is not the current HoH!");
			}
		}
		else{
			throw new ConstraintViolations("SocialGroup is null!");
		}
		
		return entityItem;
	}
	
	
	/**
	 * Checks to make sure that all Individuals have a corresponding Membership.
	 */
	public void checkCorrespondingMemberships(List<Membership> newMemberships, List<Membership> membershipsOfSocialGroup) throws ConstraintViolations{
				
		if ((newMemberships.size() != membershipsOfSocialGroup.size()) ) 
		{	
			throw new ConstraintViolations("Cannot proceed with modifying the Group Head if not all members of the Social Group have updated their Memberships to the new successor. Remember to create a new Membership for the old Group Head.");
		}
	}	
	
	/**
	 * Ensures that a successor has been selected.
	 */
	public boolean checkValidSuccessor(Individual selectedSuccessor) throws ConstraintViolations{
		
		try {
			if (selectedSuccessor == null)
				throw new ConstraintViolations();
		} 
		catch (Exception e) {
			throw new ConstraintViolations("The Successor Individual Id must be specified.");
		}
		return true;	
	}	
	
	/**
	 * Checks for Membership counts in the Social Group. 
	 * The Social Group must have Memberships in order for modifications to be made.
	 */
	private boolean determineValidSocialGroup(SocialGroup group) throws ConstraintViolations{
		
    	List<Individual> indivsAtSocialGroup = null;

        SocialGroup socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", group.getExtId(), true);      
        if (socialGroup == null) 
        	throw new  ConstraintViolations("SocialGroup not defined");
        
        indivsAtSocialGroup = socialGroupService.getAllIndividualsOfSocialGroup(socialGroup);
    	if (indivsAtSocialGroup.size() == 0)
    		 return false;
        
		return true;
	}	
	
	/** Taken from ModifyHOHBean
	 * Algorithm for finalizing the modifications to Modifying the Group Head.
	 * 1. Set the selected successor to the Social Group specified as the new Group Head and remove their Memberships from the Social Group.
	 * 2. Delete all of the old Memberships and create new ones to the newly appointed Group Head.
	 * All of this is done in one transaction.
	 */
//	public void modifySocialGroupHead(SocialGroup group, Individual selectedSuccessor, List<Membership> newMemberships) throws ConstraintViolations, SQLException, Exception {
//		socialGroupService.modifySocialGroupHead(group, selectedSuccessor, newMemberships);
//    }	
	
	public SocialGroup getSocialGroup(HeadOfHousehold entityItem) throws ConstraintViolations{
		SocialGroup sg = null;
		Set<Membership> memberships = entityItem.getMemberships();
		if(memberships.size() > 0){
			Membership membership = memberships.iterator().next();
			String socialGroupExtId = membership.getSocialGroup().getExtId();
			try{
				SocialGroup socialGroup = getSocialGroupFromExtId(socialGroupExtId);
				sg = socialGroup;
			}
			catch(Exception e){
				throw new ConstraintViolations(e.getMessage());
			}
		}
		else{
			String socialGroupExtId = entityItem.getSocialGroup().getExtId();
			SocialGroup socialGroup = getSocialGroupFromExtId(socialGroupExtId);
			Individual deadIndividual = entityItem.getDeath().getIndividual();
			if(socialGroup.getGroupHead().getExtId().equalsIgnoreCase(deadIndividual.getExtId()) && socialGroup.getMemberships().size() == 1){
			}
			else{
				throw new ConstraintViolations("Found no memberships !");
			}
		}
		return sg;
	}
	
	private SocialGroup getSocialGroupFromExtId(String socialGroupExtId){
		SocialGroup socialGroup = null;
		try{
			socialGroup = socialGroupService.findSocialGroupById(socialGroupExtId, "Could not find socialGroup");
		}
		catch(Exception e){}
		return socialGroup;
	}
	
	private boolean isHeadOfHousehold(String individualExtId, Set<Membership> memberships){
		for(Membership membership : memberships){
			SocialGroup socialGroup = membership.getSocialGroup();
			String groupHeadExtId = socialGroup.getGroupHead().getExtId();
			if(individualExtId.trim().equalsIgnoreCase(groupHeadExtId.trim())){
				return true;
			}
		}
		return false;
	} 
	
	private String getHeadOfHouseholdFromMembership(Set<Membership> memberships){
		String newHohExtId = new String();
		for(Membership membership : memberships){
			if(membership.getbIsToA().equals("1")){
				newHohExtId = membership.getIndividual().getExtId();
				break;
			}
		}	
		return newHohExtId;
	}
	
	/*
	 * Update the Socialgroup table: Replace the uuid of the old Head of Household with uuid of new Head of Household
	 */
	public SocialGroup changeSocialGroupHeadOfHousehold(SocialGroup socialGroup, Individual newHeadOfHousehold){
		
		socialGroup.setGroupHead(newHeadOfHousehold);
		
		try {		
			boolean hasConstraintValidationErrors = hasConstraintValidationErrors(socialGroup);
	        
	        if(!hasConstraintValidationErrors){
	        	entityService.save(socialGroup);	
	        }			
		} catch (ConstraintViolations e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return socialGroup;
	}
	
	private boolean hasConstraintValidationErrors(SocialGroup socialGroup){
		boolean errors = false;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SocialGroup>> constraintViolations = validator.validate(socialGroup);
        for (ConstraintViolation cv : constraintViolations) {
            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor().getAnnotation());
            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor());
            System.out.println("ValidatationConstraint: " + cv.getMessageTemplate());
            System.out.println("ValidatationConstraint: " + cv.getInvalidValue());
            System.out.println("ValidatationConstraint: " + cv.getLeafBean());
            System.out.println("ValidatationConstraint: " + cv.getRootBeanClass());
            System.out.println("ValidatationConstraint: " + cv.getPropertyPath().toString());
            System.out.println("ValidatationConstraint: " + cv.getMessage());
        }
        
        if(constraintViolations.size() > 0){
        	errors = true;
        }      
        return errors;
	}

	@Override
	public HeadOfHousehold findHeadOfHouseholdById(String extId)
			throws Exception {
		return null;
	}

    /**
     * Retrieves in Individual by extId's provided. 
     * @param: extId, Id of Individual
     * @return: If individual is unknown, returns null, otherwise Individual object
     */
    public Individual getIndividualByExtId(String extId) {
        Individual individual = genericDao.findByProperty(Individual.class, "extId", extId);
        return individual;
    }
    
   
	@Override
	public HeadOfHousehold createHeadOfHousehold(HeadOfHousehold entityItem) 
			throws ConstraintViolations {
		
		Individual selectedSuccessor = entityItem.getNewHoh();		
		SocialGroup group = entityItem.getSocialGroup();
		List<Membership> oldMemberships = getMembershipsToModify(entityItem);
		Set<Membership> newMemberships = entityItem.getMemberships();

		//Validation
		evaluateHeadOfHousehold(entityItem);
		checkValidSuccessor(selectedSuccessor);
		checkCorrespondingMemberships(new ArrayList<Membership>(newMemberships), oldMemberships);
				
		FieldWorker fw = entityItem.getCollectedBy();
		Death death = entityItem.getDeath();
		
		try{
			for(Membership m: newMemberships){
				m.setStartDate(entityItem.getDate());
				m.setEndType(siteProperties.getNotApplicableCode());
				m.setCollectedBy(fw);
				m.setStartType(siteProperties.getEnumerationCode()); // Is this correct ?
				m.setIndividual(getIndividualByExtId(m.getIndividual().getExtId()));				
			}			
			
			//Package everything and call the existing method which handle the HoH change
			if(death == null){
				List<Membership> membershipList = new ArrayList<Membership>();
				membershipList.addAll(newMemberships);				
				socialGroupService.modifySocialGroupHead(group, selectedSuccessor, membershipList);
			}
			else{
				List<SocialGroup> socialGroups = new ArrayList<SocialGroup>();
				socialGroups.add(group);
				List<Individual> successors = new ArrayList<Individual>();
				successors.add(selectedSuccessor);
				HashMap<Integer, List<Membership>> memberships = new HashMap<Integer, List<Membership>>();
				memberships.put(new Integer(0), new ArrayList<Membership>(newMemberships));
				createDeathAndSetNewHead(death, group, socialGroups, successors, memberships);
			}
		}
		catch(Exception e){
			throw new ConstraintViolations(e.getMessage());
		}
		
		return entityItem; 		
	}
	

	/**
	 * Taken from DeathHOHBean!
	 * Algorithm for finalizing the modifications to the Death of Group Head.
	 * 1. Create a death event for the Group Head, which means all Memberships, Residences, and Relationships become closed.
	 * 2. Set the selected successors to the Social Groups specified as the new Group Head and remove their Memberships from the Social Group.
	 * 3. Delete all of the old Memberships and create new ones to the newly appointed Group Head.
	 * All of this is done in one transaction.
	 */
	public void createDeathAndSetNewHead(Death death, SocialGroup group, List<SocialGroup> socialGroups, List<Individual> successors, HashMap<Integer, List<Membership>> memberships) 
		throws ConstraintViolations{
		try {
			death.setIndividual(group.getGroupHead());
			deathService.createDeathAndSetNewHead(death, socialGroups, successors, memberships);
		} catch (Exception e) {
			throw new ConstraintViolations(e.getMessage());
		}
	}	
	
	
	private List<Membership> getMembershipsToModify(HeadOfHousehold hoh){
		List<Membership> t = new ArrayList<Membership>();		
		Set<Membership> memberships = hoh.getMemberships();
		t.addAll(memberships);
		return t;
		
	}
    
	@Override
	public HeadOfHousehold updateHeadOfHousehold(HeadOfHousehold hoh)
			throws Exception {	
		return null;
	}

}
