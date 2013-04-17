package org.openhds.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.ResidencyService;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.web.service.JsfService;
import org.openhds.web.service.WebFlowService;
import org.springframework.binding.message.MessageContext;

/**
 * A backing bean that holds state and acts as a controller for the Update Form
 * 
 * @author Dave Roberge
 *
 */
public class UpdateBean implements Serializable {

	private static final long serialVersionUID = 2651943889347673856L;

	Visit currentVisit;
	Individual currentIndividual;
	
	ResidencyService residencyService;
	GenericDao genericDao;
	WebFlowService webFlowService;
	JsfService jsfService;
	SitePropertiesService properties;

	// Hold a reference to the selected individual text box
	UIInput selectedIndivInput;
	
	private List<Individual> residentIndividuals;
	
	/**
	 * resets the backing values
	 */
	public void clearAllValues() {
		currentVisit = null;
		residentIndividuals = null;
		clearSelectedIndividual(null);
	}
	
	public void resetResidencyIndividuals() {
		residentIndividuals = null;
		getIndividualsAtLocation();
	}
	
	/**
	 * Clear the {@link org.hibernate.Session} being used by the Update flow
	 * This is some what of a hack and may change in the future. The problem was that
	 * changes made during a sub flow were not being seen by the Update flow. The reason
	 * appears to be because a sub flow uses a different {@link org.hibernate.Session} than the 
	 * Update flow. Since the {@link org.hibernate.Session} being used by the Update flow is "conversational" scope 
	 * (it lasts multiple requests) it was not seeing the changes (most likely because of caching).
	 * This method explicitly calls {@link org.hibernate.Session#clear()} via the 
	 * {@link org.openhds.dao.GenericDao#clear()} method to "refresh" the {@link org.hibernate.Session}.
	 * Doing this allows the session for Update flow to see changes by sub flows
	 */
	public void clearSession() {
		// remember the visit and individual currently being viewed
		// these will be evicted after the call to clear()
		// NOTE: it is NOT always the case that the currentIndividual field refers to an instance
		// (i.e. external in migration) - this is a guard to prevent NPE
		String indivUuid = (currentIndividual == null ? null : currentIndividual.getUuid()); 
		String visitUuid = currentVisit.getUuid();
		genericDao.clear();
		
		clearAllValues();
		
		// reload the instances within the "new" session
		currentVisit = genericDao.read(Visit.class, visitUuid);
		
		// the individual is not always selected (i.e. an external in migration)
		if (indivUuid != null) {
			setCurrentIndividual(indivUuid);
			selectedIndivInput.setSubmittedValue(currentIndividual.getExtId());
		}
	}
	
	/**
	 * @return a list of individuals that have a residence at the location of the current visit
	 */
	public List<Individual> getIndividualsAtLocation() {
		if (currentVisit == null) {
			return new ArrayList<Individual>();
		}
		
		if (residentIndividuals == null) {
			residentIndividuals = residencyService.getIndividualsByLocation(currentVisit.getVisitLocation());
		}
		
		return residentIndividuals;
	}
	
	/**
	 * Determine whether the bean is in a valid state. This is required
	 * so that amendment forms can be pre-populated with values when
	 * the user clicks on an event button.
	 * 
	 * @param messageContext
	 * @return true if in a valid state, false otherwise
	 */
	public boolean checkBean(MessageContext messageContext) {
		if (currentVisit != null && currentIndividual != null)
			return true;
		
		webFlowService.createMessage(messageContext, 
				"Cannot proceed to update without the Visit Id and a selected Individual.");
		return false;
	}
	
	/**
	 * Determine whether the selected is individual is female
	 * This is mostly used when the user clicks on the pregnancy button
	 * 
	 * @param messageContext
	 * @return
	 */
	public boolean isSelectedIndividualFemale(MessageContext messageContext) {
			
		if (currentIndividual != null && currentIndividual.getGender().equals(properties.getFemaleCode()))
			return true;
		
		else {
			if (currentIndividual != null && currentIndividual.getGender().equals(properties.getFemaleCode())) 
				return true;
		}
		webFlowService.createMessage(messageContext, 
				"The individual selected must be a female gender in order to use this operation.");
		return false;
	}
	
	/**
	 * 
	 * @param entityItem
	 * @return
	 */
	public boolean checkRelFieldsA(Relationship entityItem) {	
		if (entityItem.getIndividualA().getExtId().equals(currentIndividual.getExtId()))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param entityItem
	 * @return
	 */
	public boolean checkRelFieldsB(Relationship entityItem) {	
		
		if (entityItem.getIndividualB() == null)
			return false;
		
		else if (entityItem.getIndividualB().getExtId().equals(currentIndividual.getExtId()) &&
				!entityItem.getIndividualA().getExtId().equals(currentIndividual.getExtId()))
			return true;
		return false;
	}
	
	/**
	 * Called when a Select link is clicked for an Individual
	 * This method will change the value that is displayed in the
	 * selected individual text box
	 * 
	 * @param event
	 */
	public void changeIndividual(ActionEvent event) {
		String id = jsfService.getReqParam("indivExtId");
		
		selectedIndivInput.setSubmittedValue(id);
	}
	
	/**
	 * Listener called when the Select button is clicked
	 * Its job is to grab the value in the textbox which will
	 * be an individual ext id
	 * 
	 * @param event
	 */
	public void selectBtnListener(ActionEvent event) {
		String indivUuid = selectedIndivInput.getSubmittedValue().toString().trim();
		
		setCurrentIndividualByExtId(indivUuid);
		
		if (currentIndividual == null) {
			// no individual was found with that ext id
			selectedIndivInput.resetValue();
		}
	}
	
	public void clearSelectedIndividual(ActionEvent event) {
		if (selectedIndivInput != null) {
			selectedIndivInput.resetValue();
		}
		
		currentIndividual = null;
	}
				
	/**
	 * Sets the current individual on the backing bean
	 * This will be called from the flow when the user has clicked
	 * on the Select link next to an Individual
	 * 
	 * @param individualId the uuid of the individual to lookup
	 */
	public void setCurrentIndividual(String individualId) {
		for(Individual individual : getIndividualsAtLocation()) {
			if (individual.getUuid().equals(individualId)) {
				currentIndividual = individual;
				break;
			}
		}
	}
	
	/**
	 * Sets the current individual on the backing bean
	 * using the extId rather than uuid. This is called when the
	 * user types a value in the Select Individual text box
	 * 
	 * @param extId the extid of the individual to look up
	 */
	private void setCurrentIndividualByExtId(String extId) {
        currentIndividual = null;
		for(Individual individual : getIndividualsAtLocation()) {
			if (individual.getExtId().equals(extId)) {
				currentIndividual = individual;				
				break;
			}
		}
		
	}
	
	public String getLocationName() {
		if (currentVisit == null) {
			return "";
		} else {
			return currentVisit.getVisitLocation().getLocationName();
		}
	}

	public Visit getCurrentVisit() {
		return currentVisit;
	}
	
	public void setCurrentVisit(Visit currentVisit) {
		this.currentVisit = currentVisit;
	}
	
	public Individual getCurrentIndividual() {
		return currentIndividual;
	}

	public void setCurrentIndividual(Individual currentIndividual) {
		this.currentIndividual = currentIndividual;
	}	
	
	public ResidencyService getResidencyService() {
		return residencyService;
	}
	
	public void setResidencyService(ResidencyService residencyService) {
		this.residencyService = residencyService;
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

	public UIInput getSelectedIndivInput() {
		return selectedIndivInput;
	}

	public void setSelectedIndivInput(UIInput selectedIndivInput) {
		this.selectedIndivInput = selectedIndivInput;
	}

	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
	
	public SitePropertiesService getProperties() {
		return properties;
	}

	public void setProperties(SitePropertiesService properties) {
		this.properties = properties;
	}
}
