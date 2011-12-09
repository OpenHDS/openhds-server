package org.openhds.datageneration.generator;

import java.util.Calendar;
import org.apache.commons.lang.RandomStringUtils;
import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class OutMigrationEventGenerator extends AbstractEventGenerator {

	public OutMigrationEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}

	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual indiv) {
		if (indiv.getCurrentResidency() == null || indiv.getCurrentResidency().getEndDate() != null) {
			return false;
		}
		
		Residency res =indiv.getCurrentResidency();
		res.setEndDate(Calendar.getInstance());
		res.setEndType(siteProperties.getOutmigrationCode());

		OutMigration outMigration = new OutMigration();
		outMigration.setCollectedBy(visit.getCollectedBy());
		outMigration.setDestination(RandomStringUtils.randomAlphabetic(12));
		outMigration.setIndividual(indiv);
		outMigration.setReason(RandomStringUtils.randomAlphabetic(12));
		outMigration.setRecordedDate(DataGeneratorUtils.getDateInPast(10));
		outMigration.setResidency(res);
		outMigration.setVisit(visit);
		
		persistEntity(res, false);
		return persistEntity(outMigration, false);
	}

}
