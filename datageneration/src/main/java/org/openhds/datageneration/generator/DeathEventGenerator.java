package org.openhds.datageneration.generator;

import org.apache.commons.lang.RandomStringUtils;
import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class DeathEventGenerator extends AbstractEventGenerator {

	public DeathEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}
	
	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual candidateIndividual) {
		Death death = genericDao.findByProperty(Death.class, "individual", candidateIndividual);
		if (death != null) {
			return false; //already a death event for this individual
		}
		
		death = new Death();
		death.setCollectedBy(visit.getCollectedBy());
		death.setDeathCause(RandomStringUtils.randomAlphabetic(12));
		death.setIndividual(candidateIndividual);
		death.setDeathPlace(RandomStringUtils.randomAlphabetic(12));
		death.setVisitDeath(visit);
		death.setDeathDate(DataGeneratorUtils.getDateInPast(1));
		
		return persistEntity(death, true);
	}
}
