package org.openhds.web.beans;

import java.io.Serializable;
import java.util.List;
import org.openhds.dao.GenericDao;
import org.openhds.domain.Death;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.Membership;
import org.openhds.domain.OutMigration;
import org.openhds.domain.PregnancyObservation;
import org.openhds.domain.PregnancyOutcome;
import org.openhds.domain.Relationship;
import org.openhds.domain.Residency;
import org.openhds.domain.SocialGroup;
import org.openhds.service.DeathService;
import org.openhds.service.InMigrationService;
import org.openhds.service.IndividualService;
import org.openhds.service.MembershipService;
import org.openhds.service.OutMigrationService;
import org.openhds.service.PregnancyService;
import org.openhds.service.RelationshipService;
import org.openhds.service.ResidencyService;
import org.openhds.service.SocialGroupService;
import org.openhds.service.WebFlowService;
import org.springframework.binding.message.MessageContext;

/**
 * 
 * Backing Bean that acts as a controller for viewing the history of an Individual.
 * Future work may involve being able to view, edit, delete a particular record.
 * 
 * @author Brian
 */
public class IndividualHistoryBean implements Serializable {

	private static final long serialVersionUID = -2613156679997307193L;
			
	String individualExtId;
	Individual individual;
	
	MembershipService membershipService;
	IndividualService individualService;
	RelationshipService relationshipService;
	SocialGroupService socialGroupService;
	ResidencyService residencyService;
	InMigrationService inMigrationService;
	OutMigrationService outMigrationService;
	PregnancyService pregnancyService;
	DeathService deathService;
	
	WebFlowService webFlowService;
	GenericDao genericDao;
	
	public void reset() {
		individual = null;
		individualExtId = "";
	}
	

	// The following methods delegate to the Services to retrieve particular
	// events for an Individual. These are used to populate the tables.
	
	public List<Membership> getMemberships() {
		return membershipService.getAllMemberships(individual);
	}
	
	public List<Relationship> getRelationships() {
		return relationshipService.getAllRelationships(individual);
	}
	
	public List<SocialGroup> getSocialGroups() {
		return socialGroupService.getAllSocialGroups(individual);
	}
	
	public List<Residency> getResidencies() {
		return residencyService.getAllResidencies(individual);
	}
	
	public List<InMigration> getInMigrations() {
		return inMigrationService.getInMigrationsByIndividual(individual);
	}
	
	public List<OutMigration> getOutMigrations() {
		return outMigrationService.getOutMigrations(individual);
	}
	
	public List<PregnancyOutcome> getPregOutcomes() {
		return pregnancyService.getPregnancyOutcomesByIndividual(individual);
	}
	
	public List<PregnancyObservation> getPregObservs() {
		return pregnancyService.getPregnancyObservationByIndividual(individual);
	}
	
	public List<Death> getDeaths() {
		return deathService.getDeathsByIndividual(individual);
	}
		
	/**
	 * Determine if the Individual id specified exists.
	 */
	public boolean determineValidIndividual(MessageContext messageContext) throws Exception {
		
		Individual indiv = individualService.findIndivById(individualExtId);
		
		if (indiv == null) {
			webFlowService.createMessage(messageContext, 
			"The specified Individual cannot be found.");
			return false;
		}		
		this.individual = indiv;
		return true;
	}
			
	public String getIndividualExtId() {
		return individualExtId;
	}

	public void setIndividualExtId(String individualExtId) {
		this.individualExtId = individualExtId;
	}

	public Individual getIndividual() {
		return individual;
	}
	
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}
	
	public IndividualService getIndividualService() {
		return individualService;
	}

	public void setIndividualService(IndividualService individualService) {
		this.individualService = individualService;
	}
	
	public RelationshipService getRelationshipService() {
		return relationshipService;
	}

	public void setRelationshipService(RelationshipService relationshipService) {
		this.relationshipService = relationshipService;
	}

	public SocialGroupService getSocialGroupService() {
		return socialGroupService;
	}

	public void setSocialGroupService(SocialGroupService socialGroupService) {
		this.socialGroupService = socialGroupService;
	}

	public ResidencyService getResidencyService() {
		return residencyService;
	}

	public void setResidencyService(ResidencyService residencyService) {
		this.residencyService = residencyService;
	}

	public InMigrationService getInMigrationService() {
		return inMigrationService;
	}

	public void setInMigrationService(InMigrationService inMigrationService) {
		this.inMigrationService = inMigrationService;
	}

	public OutMigrationService getOutMigrationService() {
		return outMigrationService;
	}

	public void setOutMigrationService(OutMigrationService outMigrationService) {
		this.outMigrationService = outMigrationService;
	}

	public PregnancyService getPregnancyService() {
		return pregnancyService;
	}

	public void setPregnancyService(PregnancyService pregnancyService) {
		this.pregnancyService = pregnancyService;
	}

	public DeathService getDeathService() {
		return deathService;
	}

	public void setDeathService(DeathService deathService) {
		this.deathService = deathService;
	}
	
	public WebFlowService getWebFlowService() {
		return webFlowService;
	}

	public void setWebFlowService(WebFlowService webFlowService) {
		this.webFlowService = webFlowService;
	}
	
	public MembershipService getMembershipService() {
		return membershipService;
	}

	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}
	
	public GenericDao getGenericDao() {
		return genericDao;
	}
	
	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
}
