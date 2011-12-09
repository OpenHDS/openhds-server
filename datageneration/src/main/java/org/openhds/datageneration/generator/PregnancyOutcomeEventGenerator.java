package org.openhds.datageneration.generator;

import java.util.Iterator;
import org.apache.commons.lang.RandomStringUtils;
import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class PregnancyOutcomeEventGenerator extends AbstractEventGenerator {

	public PregnancyOutcomeEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}

	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual mother) {
		if (mother.getGender().equals(siteProperties.getMaleCode())) {
			return false;
		}
		
		Iterator<Membership> memberships = mother.getAllMemberships().iterator();
		
		if (!memberships.hasNext()) {
			return false;
		}
		
		SocialGroup mothersSocialGroup = memberships.next().getSocialGroup();
		
		Membership membership = createMembershipFromMother(mother, visit.getCollectedBy(), mothersSocialGroup);
		Individual child = createChildWithMother(mother, visit.getCollectedBy());
		Residency res = DataGeneratorUtils.createResidency(mother.getCurrentResidency().getLocation(), visit.getCollectedBy(), child, child.getDob(), "LBR");
		Outcome outcome = createChild(child, membership);
		PregnancyOutcome pregOutcome = createPregnancyOutcome(visit, mother, outcome);
		
		persistEntity(outcome.getChild(), false);
		persistEntity(outcome.getChildMembership(), false);
		persistEntity(res, false);
		
		return persistEntity(pregOutcome, true);
	}

	private Outcome createChild(Individual child, Membership membership) {
		Outcome outcome = new Outcome();
		outcome.setType(siteProperties.getLiveBirthCode());
		outcome.setChild(child);
		outcome.setChildMembership(membership);
		return outcome;
	}

	private PregnancyOutcome createPregnancyOutcome(Visit visit,
			Individual indiv, Outcome outcome) {
		PregnancyOutcome pregOutcome = new PregnancyOutcome();
		pregOutcome.setChildEverBorn(1);
		pregOutcome.setCollectedBy(visit.getCollectedBy());
		pregOutcome.setFather(genericDao.findByProperty(Individual.class, "extId", "UNK"));
		pregOutcome.setMother(indiv);
		pregOutcome.setNumberOfLiveBirths(1);
		pregOutcome.setOutcomeDate(DataGeneratorUtils.getDateInPast(5));
		pregOutcome.addLiveBirthOutcome(outcome);
		return pregOutcome;
	}

	private Individual createChildWithMother(Individual mother, FieldWorker collectedBy) {
		Individual indiv = new Individual();
		indiv.setCollectedBy(collectedBy);
		indiv.setDob(DataGeneratorUtils.getDateInPast(5));
		indiv.setExtId(RandomStringUtils.randomAlphabetic(10));
		indiv.setFirstName(RandomStringUtils.randomAlphabetic(10));
		indiv.setLastName(RandomStringUtils.randomAlphabetic(10));
		indiv.setFather(genericDao.findByProperty(Individual.class, "extId", "UNK"));
		indiv.setMother(mother);
		indiv.setGender(DataGeneratorUtils.randomlySelectMaleOrFemale() ? siteProperties.getMaleCode() : siteProperties.getFemaleCode());
		return indiv;
	}
	
	private Membership createMembershipFromMother(Individual indiv, FieldWorker collectedBy, SocialGroup socialGroup) {
		Membership membership = new Membership();
		membership.setbIsToA(indiv.getGender().equals(siteProperties.getMaleCode()) ? "SON" : "DAUGHTER");
		membership.setCollectedBy(collectedBy);
		membership.setIndividual(indiv);
		membership.setStartType(siteProperties.getBirthCode());
		membership.setStartDate(indiv.getDob());
		membership.setSocialGroup(socialGroup);
		membership.setEndType(siteProperties.getNotApplicableCode());
	
		return membership;
	}	
}
