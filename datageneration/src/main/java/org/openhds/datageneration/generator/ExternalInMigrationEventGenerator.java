package org.openhds.datageneration.generator;

import org.apache.commons.lang.RandomStringUtils;
import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public class ExternalInMigrationEventGenerator extends AbstractEventGenerator {

	public ExternalInMigrationEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		super(genericDao, entityService, numberToGenerate, siteProperties);
	}

	@Override
	protected boolean generateEventWithIndividual(Visit visit, Individual ignoredIndividual) {
		Individual indiv = createIndividual(visit.getCollectedBy());
		Residency res = DataGeneratorUtils.createResidency(visit.getVisitLocation(), visit.getCollectedBy(), indiv, 
				DataGeneratorUtils.getDateInPast(1), siteProperties.getInmigrationCode());
		
		
		InMigration inMigration = new InMigration();
		inMigration.setCollectedBy(visit.getCollectedBy());
		inMigration.setIndividual(indiv);
		inMigration.setMigTypeExternal();
		inMigration.setOrigin(RandomStringUtils.randomAlphabetic(12));
		inMigration.setReason(RandomStringUtils.randomAlphabetic(12));
		inMigration.setVisit(visit);
		inMigration.setResidency(res);
		inMigration.setRecordedDate(DataGeneratorUtils.getDateInPast(20));
		
		return persistEntity(inMigration, true);
	}

	private Individual createIndividual(FieldWorker collectedBy) {
		Individual individual = new Individual();
		individual.setFirstName(RandomStringUtils.randomAlphabetic(10));
		individual.setLastName(RandomStringUtils.randomAlphabetic(10));
		individual.setCollectedBy(collectedBy);
		individual.setDob(DataGeneratorUtils.generateRandomDob(50));
		individual.setExtId(RandomStringUtils.randomAlphanumeric(5));
		individual.setFather(genericDao.findByProperty(Individual.class, "extId", siteProperties.getUnknownIdentifier()));
		individual.setMother(genericDao.findByProperty(Individual.class, "extId", siteProperties.getUnknownIdentifier()));
		if (DataGeneratorUtils.randomlySelectMaleOrFemale()) {
			individual.setGender(siteProperties.getMaleCode());
		} else {
			individual.setGender(siteProperties.getFemaleCode());
		}
		return individual;
	}

}
