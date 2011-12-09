package org.openhds.datageneration.generator;

import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class PregnancyObservationEventGenerator extends AbstractEventGenerator {

	public PregnancyObservationEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}

	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual candidateIndividual) {
		if (candidateIndividual.getGender().equals(siteProperties.getMaleCode())) {
			return false;
		}
		
		PregnancyObservation obs = new PregnancyObservation();
		obs.setCollectedBy(visit.getCollectedBy());
		obs.setMother(candidateIndividual);
		obs.setExpectedDeliveryDate(DataGeneratorUtils.getDateInFuture(100));
		obs.setRecordedDate(DataGeneratorUtils.getDateInPast(10));
		
		return persistEntity(obs, true);
	}
}
