package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Individual;
import org.openhds.domain.Membership;
import org.openhds.domain.Outcome;
import org.openhds.domain.PregnancyOutcome;
import org.openhds.domain.SocialGroup;
import org.openhds.service.PregnancyService;
import org.openhds.service.SocialGroupService;

/**
 * The implementation of the crud for the Pregnancy Outcome form
 * This form varies from other crud classes because it using spring web flow
 * 
 * @author Dave
 *
 */
public class PregnancyOutcomeCrudImpl extends EntityCrudImpl<PregnancyOutcome, String> {

    PregnancyService service;
    SocialGroupService sgService;    

	//flag checking whether a pregoutcome has been evaluated
    boolean evaluated;
    
	// this is binded to the outcome type select field within the jsf page
    // its value will be the current outcome selected by the user
    String selectedType;
    
    // represents the current outcome the user is working on
    // this is only used when the user is adding a live birth to the pregnancy outcome
    // they must first create an individual, than a membership which means the
    // outcome must last thru several form transitions
    // the outcome is only added to this pregnancy outcome after the user
    // has completed the individual and membership create forms
	private Outcome currentOutcome;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date recordedDate;
    
    public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

    public PregnancyOutcomeCrudImpl(Class<PregnancyOutcome> entityClass) {
        super(entityClass);
        entityFilter = new PregnancyOutcomeFilter();
    }

    @Override
    public String create() {
        try {
            // verify integrity constraints
        	if(!evaluated)
        		service.evaluatePregnancyOutcome(entityItem);
            // create the pregnancy outcome
            // NOTE: this crud never explicity calls the super.create
            // because the service class will persist the pregnancy outcome
            service.createPregnancyOutcome(entityItem);

            return listSetup();
        } catch (ConstraintViolations e) {
            jsfService.addError(e.getMessage());
        } catch (AuthorizationException e) {
        	jsfService.addError(e.getMessage());
        }
        return null;
    }

    /**
     * Helper method called by the pregnancy flow when the user selects the Add Outcome button
     * If the user has selected any outcome other than live birth, add it to the list of outcomes
     * for this pregnancy. Otherwise prepare an outcome for live birth - user must create individual and membership
     *
     * @return the string liveBirth if user has selected live birth, otherwise the string other
     */
    public String addOutcome() {

        if (selectedType.equals(properties.getLiveBirthCode())) {
        	currentOutcome = new Outcome();
        	currentOutcome.setType(properties.getLiveBirthCode());
            return "liveBirth";
        }

        entityItem.addOutcome(selectedType, null);

        // reset the selected item in drop down list
        selectedType = properties.getLiveBirthCode();

        return "other";
    }

    /**
     * Helper method used by the pregnancy flow to set the child for the current outcome
     * Called after the user hits the create button on the individual form
     * @param individual
     */
    public void addChild(Individual individual) {
        currentOutcome.setChild(individual);
        entityItem.setOutcomeDate(individual.getDob());
    }
    
    /**
     * Helper method to retrieve the social group from the mother of the current individual
     * @return
     */
    public SocialGroup getMothersSocialGroupForCurrentIndividual() {
    	return sgService.getSocialGroupForIndividualByType(entityItem.getMother(), "FAM");
    }
    
    /**
     * Helper method used by the pregnancy flow to set the membership for the child of the current outcome
     * Called after the user hits the create button on the membership form
     * @param membership
     */
    public void setMembershipOnCurrentOutcome(Membership membership) {
    	membership.setStartType(properties.getBirthCode());
    	currentOutcome.setChildMembership(membership);
    }
    
    /**
     * Adds the currentOutcome to the list of outcomes for this pregnancy
     * Called after the user hits the create button on the membership form
     */
    public void addCurrentOutcome() {
    	entityItem.addLiveBirthOutcome(currentOutcome);
    	currentOutcome = null;
    }
    
    public Date getRecordedDate() {
    	
    	if (entityItem.getOutcomeDate() == null)
    		return new Date();
    	
    	return entityItem.getOutcomeDate().getTime();
	}

	public void setRecordedDate(Date recordedDate) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(recordedDate);
//		entityItem.setOutcomeDate(cal);
	}

    public PregnancyService getService() {
        return service;
    }

    public void setService(PregnancyService service) {
        this.service = service;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    private class PregnancyOutcomeFilter implements EntityFilter<PregnancyOutcome> {

        @Override
        public List<PregnancyOutcome> getFilteredEntityList(PregnancyOutcome entityItem) {
            if(entityItem!=null){
                return service.getPregnancyOutcomesByIndividual(entityItem.getMother());
            }
            return new ArrayList<PregnancyOutcome>();
        }
    }

	public SocialGroupService getSgService() {
		return sgService;
	}

	public void setSgService(SocialGroupService sgService) {
		this.sgService = sgService;
	}
}
