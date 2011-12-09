package org.openhds.datageneration.generator;

import org.apache.commons.lang.RandomStringUtils;
import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class InternalInMigrationEventGenerator extends AbstractEventGenerator {

	public InternalInMigrationEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}

	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual indiv) {
		if (indiv.getCurrentResidency() != null && indiv.getCurrentResidency().getEndDate() == null) {
			return false;	
		}
		
		InMigration inMigration = new InMigration();
		inMigration.setCollectedBy(visit.getCollectedBy());
		inMigration.setIndividual(indiv);
		inMigration.setMigTypeInternal();
		inMigration.setOrigin(RandomStringUtils.randomAlphabetic(12));
		inMigration.setReason(RandomStringUtils.randomAlphabetic(12));
		inMigration.setVisit(visit);
		inMigration.setResidency(generateResidency(visit.getCollectedBy(), indiv, visit.getVisitLocation()));
		inMigration.setRecordedDate(DataGeneratorUtils.getDateInPast(20));
		
		persistEntity(inMigration.getResidency(), false);
		return persistEntity(inMigration, true);
	}

	private Residency generateResidency(FieldWorker collectedBy, Individual indiv, Location location) {
		Residency res = new Residency();
		res.setCollectedBy(collectedBy);
		res.setIndividual(indiv);
		res.setLocation(location);
		res.setStartDate(DataGeneratorUtils.getDateInPast(25));
		res.setStartType(siteProperties.getInmigrationCode());
		return res;
	}
}
