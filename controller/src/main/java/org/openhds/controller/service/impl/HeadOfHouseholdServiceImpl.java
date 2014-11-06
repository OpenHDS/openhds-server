package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.idgeneration.VisitGenerator;
import org.openhds.controller.service.DeathService;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.HeadOfHouseholdService;
import org.openhds.controller.service.MembershipService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.HeadOfHousehold;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.domain.model.wrappers.Individuals;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.sun.javafx.collections.NonIterableChange.GenericAddRemoveChange;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

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
		
		System.out.println("In evaluateHeadOfHousehold()");
		SocialGroup sg = getSocialGroup(entityItem);
		Individual oldHoh = getIndividualByExtId(entityItem.getOldHoh().getExtId());
		
		System.out.println("With old hoh-id: " + entityItem.getOldHoh().getExtId()
				+ " new hoh-id: " + entityItem.getNewHoh().getExtId() + " and sg-id: " + sg.getExtId());
		
		
		if(oldHoh == null){
			System.out.println("No old Individual with this extId found!");
			throw new ConstraintViolations("No old Individual with this extId found!");
		}
				
		String newHohExtId = entityItem.getNewHoh().getExtId();
		
		if(newHohExtId.isEmpty()){
			System.out.println("No new Individual as new HoH specified!");
			throw new ConstraintViolations("No new Individual as new HoH specified!");
		}
		Individual newHoh = getIndividualByExtId(newHohExtId);
		
		if(newHoh == null){
			System.out.println("No new Individual with this extId found!");
			throw new ConstraintViolations("No new Individual with this extId found!");
		}
		
		boolean isValidSocialGroup = determineValidSocialGroup(sg);
		
		if(isValidSocialGroup)
			System.out.println("Socialgroup seems to be valid!");
		else
			System.out.println("Socialgroup DOES NOT SEEM TO BE VALID !!!");
		
		
		if(sg != null){
			if(sg.getGroupHead().getExtId().trim().equalsIgnoreCase(oldHoh.getExtId().trim())){
				System.out.println("OldHoh seems to be groupHead if group with extId " + sg.getExtId());
			}
			else{
				System.out.println("Could not find oldHoh with extId " + oldHoh.getExtId() + " as Head of Household of group with extId " + sg.getExtId());
				throw new ConstraintViolations("Specified old HoH-extId is not the current HoH!");
			}
		}
		else{
			System.out.println("SocialGroup is null !");
			throw new ConstraintViolations("SocialGroup is null !");
		}
		
		return entityItem;
	}
	
	/**
	 * Checks for Membership counts in the Social Group. 
	 * The Social Group must have Memberships in order for modifications to be made.
	 */
	private boolean determineValidSocialGroup(SocialGroup group) {
		
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
	 * Checks to make sure that all Individuals have a corresponding Membership.
	 */
	public void checkCorrespondingMemberships(List<Membership> newMemberships, List<Membership> membershipsOfSocialGroup) throws ConstraintViolations{
				
		if ((newMemberships.size() != membershipsOfSocialGroup.size()) ) 
		{	
			throw new ConstraintViolations("Cannot proceed with modifying the Group Head if not all members of the Social Group have updated their Memberships to the new successor. Remember to create a new Membership for the old Group Head.");
		}
	}	
	
	/**
	 * Algorithm for finalizing the modifications to Modifying the Group Head.
	 * 1. Set the selected successor to the Social Group specified as the new Group Head and remove their Memberships from the Social Group.
	 * 2. Delete all of the old Memberships and create new ones to the newly appointed Group Head.
	 * All of this is done in one transaction.
	 */
	public void modifySocialGroupHead(SocialGroup group, Individual selectedSuccessor, List<Membership> newMemberships) throws ConstraintViolations, SQLException, Exception {
		socialGroupService.modifySocialGroupHead(group, selectedSuccessor, newMemberships);
    }	
	
	public SocialGroup getSocialGroup(HeadOfHousehold entityItem) throws ConstraintViolations{
		SocialGroup sg = null;
		Set<Membership> memberships = entityItem.getMemberships();
		if(memberships.size() > 0){
			Membership membership = memberships.iterator().next();
			String socialGroupExtId = membership.getSocialGroup().getExtId();
			try{
				SocialGroup socialGroup = socialGroupService.findSocialGroupById(socialGroupExtId, "Could not find socialGroup");
				sg = socialGroup;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				throw new ConstraintViolations(e.getMessage());
			}
		}
		else{
			System.out.println("Found no memberships !");
			throw new ConstraintViolations("Found no memberships !");
		}
		return sg;
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
	        else{
	        	System.err.println("Encoutered ConstraintViolations!");
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
		 
		evaluateHeadOfHousehold(entityItem);
		
//		System.out.println("After ConstraintViolation");
		
		Individual oldHoh = entityItem.getOldHoh();
		
//		SocialGroup sg = getSocialGroup(entityItem);
		SocialGroup sg = entityItem.getSocialGroup();
		
		Individual indi = entityItem.getNewHoh();		
		
		List<Membership> oldMemberships = getMembershipsToModify(entityItem);
//		System.out.println("received old memberships: " + oldMemberships.size());
		
		Set<Membership> newMemberships = entityItem.getMemberships();
//		System.out.println("received new memberships: " + newMemberships.size());
		
		checkCorrespondingMemberships(new ArrayList<Membership>(newMemberships), oldMemberships);
				
		FieldWorker fw = entityItem.getCollectedBy();
				
		try{
			for(Membership m: newMemberships){
				Calendar calendar = new GregorianCalendar(2014,10,2);
				m.setStartDate(calendar);
				m.setEndType("NA");
				m.setCollectedBy(fw);
				m.setStartType("ENU"); // Is this correct ?
				
				Individual i = getIndividualByExtId(m.getIndividual().getExtId());
				m.setIndividual(i);				
//				System.out.println("New membership " + " DOB: " + i.getDob() + ", Startdate: " + calendar);
			}
						
			Death death = entityItem.getDeath();			
			
			SocialGroup group = sg; 
			List<SocialGroup> socialGroups = new ArrayList<SocialGroup>();
			socialGroups.add(sg);
			
			List<Individual> successors = new ArrayList<Individual>();
			successors.add(indi);
			
			HashMap<Integer, List<Membership>> memberships = new HashMap<Integer, List<Membership>>();
			memberships.put(new Integer(0), new ArrayList<Membership>(newMemberships));
			
			createDeathAndSetNewHead(death, group, socialGroups, successors, memberships);
			
//			System.out.println("Modifications to HoH completed: " +  (createDeathSuccess?"successfully":"UNsuccessfully") + " !");
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
//			System.out.println(e.getMessage());
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
