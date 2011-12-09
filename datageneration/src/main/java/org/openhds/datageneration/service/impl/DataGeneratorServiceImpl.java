package org.openhds.datageneration.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.openhds.datageneration.utils.Cursor;
import org.openhds.datageneration.utils.DataGeneratorUpdateParams;
import org.openhds.datageneration.utils.DataGeneratorUpdateParams.UpdateEventGenerationStrategy;
import org.openhds.datageneration.utils.DataGeneratorUtils;
import org.openhds.datageneration.generator.DeathEventGenerator;
import org.openhds.datageneration.generator.ExternalInMigrationEventGenerator;
import org.openhds.datageneration.generator.InternalInMigrationEventGenerator;
import org.openhds.datageneration.generator.OutMigrationEventGenerator;
import org.openhds.datageneration.generator.PregnancyObservationEventGenerator;
import org.openhds.datageneration.generator.PregnancyOutcomeEventGenerator;
import org.openhds.datageneration.service.DataGeneratorDao;
import org.openhds.datageneration.service.DataGeneratorService;
import org.openhds.datageneration.service.EventGenerator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Note;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Round;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.service.SitePropertiesService;

/**
 * Service for accessing functionality related to data generation
 */
public class DataGeneratorServiceImpl implements DataGeneratorService {
	
	private final static Logger logger = Logger.getLogger(DataGeneratorServiceImpl.class);

	private DataGeneratorDao dataGeneratorDao;
	private LocationHierarchyService locationHierarchyService;
	private EntityService entityService;
	private SitePropertiesService siteProperties;
	private final List<EventGenerator> eventGenerators = new ArrayList<EventGenerator>();
	private final List<EventGenerator> completedEventGenerators = new ArrayList<EventGenerator>();

	/**
	 * 
	 * @param numFieldWorkers
	 * @param numLocations
	 * @param useIdScheme
	 */
	public void generateLocationData(int numFieldWorkers, int numLocations, boolean useIdScheme) {
		generateFieldWorkers(numFieldWorkers);
		generateLocations(numLocations, useIdScheme);
	}
	
	private void generateFieldWorkers(int numFieldWorkers) {
		while(numFieldWorkers > 0) {
			FieldWorker fw = DataGeneratorUtils.createRandomFieldWorker();
			
			if (persistEntity(fw)) {
				numFieldWorkers--;
			}
		}
	}

	private void generateLocations(int numLocationsToGenerate, boolean useIdScheme) {
		LocationHierarchyLevel lowestLevel = locationHierarchyService.getLowestLevel();
		List<LocationHierarchy> canidateLocationParents = dataGeneratorDao.findListByProperty(LocationHierarchy.class, "level", lowestLevel);
		List<FieldWorker> canidateLocationFieldWorkers = dataGeneratorDao.findAll(FieldWorker.class, true);
		
		while(numLocationsToGenerate > 0) {
			Location location = DataGeneratorUtils.createRandomLocation(canidateLocationParents, canidateLocationFieldWorkers);
			
			if (useIdScheme) {
				try {
					locationHierarchyService.evaluateLocation(location);
				} catch (ConstraintViolations e) {

				}
			}
			
			if (persistEntity(location)) {
				numLocationsToGenerate--;
			}
		}
	}

	public void generateBaselineCensusData(int numIndividuals, int minAge, int maxAge, int minIndivInSocialGroup, int maxIndivInSocialGroup, 
							int numMaritalRelationships, Date baselineStartDate, Date baselineEndDate) {
		long locationCount = getTotalLocationCount();
		long fieldWorkerCount = getTotalFieldWorkerCount();
					
		while(numIndividuals > 0) {
			SocialGroup sg = null;
			Location location = getRandomRowFromDatabase(Location.class, "locationName", locationCount);
			FieldWorker collectedBy = getRandomRowFromDatabase(FieldWorker.class, "firstName", fieldWorkerCount);
			int numberOfIndividualsInSocialGroup = DataGeneratorUtils.getRandomBetween(minIndivInSocialGroup, maxIndivInSocialGroup);
			Calendar startDate = Calendar.getInstance(), endDate = Calendar.getInstance();
			startDate.setTime(baselineStartDate);
			endDate.setTime(baselineEndDate);
			Calendar insertDate = DataGeneratorUtils.getDateBetween(startDate, endDate);
			
			for(int i = 0; i < numberOfIndividualsInSocialGroup && numIndividuals > 0; i++) {
				if (i == 0) {
					sg = createSocialGroupWithIndividualAsHead(maxAge, location, collectedBy, insertDate);
					if (sg == null) {
						logger.warn("Could not create social group");
					}
				} else if (sg != null) {
					int maxYear = Calendar.getInstance().get(Calendar.YEAR) - sg.getGroupHead().getDob().get(Calendar.YEAR);
					createIndividualWithSocialGroup(sg, location, collectedBy, maxYear, insertDate);
				}
				numIndividuals--;
			}
		}
		
		createMaritalRelationships(numMaritalRelationships);
	}

	private long getTotalLocationCount() {
		long locationCount = dataGeneratorDao.getTotalCount(Location.class);
		return locationCount;
	}

	private long getTotalFieldWorkerCount() {
		long fieldWorkerCount = dataGeneratorDao.getTotalCount(FieldWorker.class);
		return fieldWorkerCount;
	}
	
	private <T> T getRandomRowFromDatabase(Class<T> clazz, String orderByProperty, long totalEntityCount) {
		int selectedRow = DataGeneratorUtils.generateRandomIntWithBound((int)totalEntityCount);
		return dataGeneratorDao.findByRowNumber(clazz, selectedRow, orderByProperty, true);
	}	
	
	private SocialGroup createSocialGroupWithIndividualAsHead(int maxAge, Location tagetLocation, FieldWorker collectedBy, Calendar insertDate) {
		Individual head = createIndividual(maxAge, collectedBy, true);
		persistEntity(head);
		head.setInsertDate(insertDate);
		updateEntity(head);
		
		Residency residency = DataGeneratorUtils.createResidency(tagetLocation, collectedBy, head, insertDate, siteProperties.getEnumerationCode());
		persistEntity(residency);
		residency.setInsertDate(insertDate);
		updateEntity(residency);
			
		SocialGroup sg = DataGeneratorUtils.createSocialGroup(collectedBy, head);
		persistEntity(sg);
		sg.setInsertDate(insertDate);
		updateEntity(sg);
		
		return sg;
	}

	private <T> void updateEntity(T entity) {
		try {
			entityService.save(entity);
		} catch (ConstraintViolations e) {
			System.out.println();
		} catch (SQLException e) {
			System.out.println();
		}
	}

	private Individual createIndividual(int maxAge, FieldWorker collectedBy, boolean isMale) {
		Individual head = new Individual();
		head.setFirstName(RandomStringUtils.randomAlphabetic(10));
		head.setLastName(RandomStringUtils.randomAlphabetic(10));
		head.setCollectedBy(collectedBy);
		head.setDob(DataGeneratorUtils.generateRandomDob(maxAge));
		head.setExtId(RandomStringUtils.randomAlphanumeric(10));
		head.setFather(dataGeneratorDao.findByProperty(Individual.class, "extId", siteProperties.getUnknownIdentifier()));
		head.setMother(dataGeneratorDao.findByProperty(Individual.class, "extId", siteProperties.getUnknownIdentifier()));
		if (isMale) {
			head.setGender(siteProperties.getMaleCode());
		} else {
			head.setGender(siteProperties.getFemaleCode());
		}
		return head;
	}

	private void createIndividualWithSocialGroup(SocialGroup socialGroup, Location targetLocation, FieldWorker collectedBy, int maxAge, Calendar insertDate) {
		Individual indiv = createIndividual(maxAge, collectedBy, DataGeneratorUtils.randomlySelectMaleOrFemale());
		persistEntity(indiv);
		indiv.setInsertDate(insertDate);
		updateEntity(indiv);
		
		Residency residency = DataGeneratorUtils.createResidency(targetLocation, collectedBy, indiv, insertDate, siteProperties.getEnumerationCode());
		persistEntity(residency);
		residency.setInsertDate(insertDate);
		updateEntity(residency);
		
		Membership membership = DataGeneratorUtils.createMembership(socialGroup, collectedBy, indiv);
		persistEntity(membership);
		membership.setInsertDate(insertDate);
		updateEntity(membership);
		socialGroup.getMemberships().add(membership);
		updateEntity(socialGroup);
	}

	private <T> boolean persistEntity(T entity) {
		try {
			entityService.create(entity);
			return true;
		} catch (IllegalArgumentException e) {
			logger.warn("Could not create " + entity.getClass().getSimpleName());
		} catch (ConstraintViolations e) {
			logger.warn("Could not create " + entity.getClass().getSimpleName());
		} catch (SQLException e) {
			logger.warn("Could not create " + entity.getClass().getSimpleName());
		}
		
		return false;
	}


	private void createMaritalRelationships(int numMaritalRelationships) {
		long socialGroupCount = getTotalSocialGroupCount();
		while (numMaritalRelationships > 0) {
			SocialGroup canidateSocialGroup = getRandomRowFromDatabase(SocialGroup.class, "extId", socialGroupCount);
			Individual canidateIndividualA = null, canidateIndividualB = null;
			for(Membership membership : canidateSocialGroup.getMemberships()) {
				if (canidateIndividualA == null) {
					canidateIndividualA = membership.getIndividual();
					continue;
				} else if (canidateIndividualB == null) {
					canidateIndividualB = membership.getIndividual();
					break;
				}
			}
			if (canidateIndividualA == null || canidateIndividualB == null) {
				continue;
			}
			Relationship relationship = new Relationship();
			relationship.setCollectedBy(canidateIndividualA.getCollectedBy());
			relationship.setaIsToB(getRandomRelationshipType());
			relationship.setEndType(siteProperties.getNotApplicableCode());
			relationship.setIndividualA(canidateIndividualA);
			relationship.setIndividualB(canidateIndividualB);
			relationship.setStartDate(canidateSocialGroup.getInsertDate());
			persistEntity(relationship);
			relationship.setInsertBy(canidateSocialGroup.getInsertBy());
			updateEntity(relationship);
			numMaritalRelationships--;
		}
	}	

	private String getRandomRelationshipType() {
		int relationType = DataGeneratorUtils.generateRandomIntWithBound(4);
		switch (relationType) {
		case 0:
			return "MARRIED";
		case 1:
			return "SEPARATED/DIVORCED";
		case 2:
			return "WIDOWED";
		default:
			return "LIVING TOGETHER";
		}
	}

	private long getTotalSocialGroupCount() {
		return dataGeneratorDao.getTotalCount(SocialGroup.class);
	}
	
	private String getOppositeGender(String gender) {
		if (gender.equals(siteProperties.getMaleCode())) {
			return siteProperties.getFemaleCode();
		} else {
			return siteProperties.getMaleCode();
		}
	}

	public void generateUpdateRoundEvents(DataGeneratorUpdateParams params) throws Exception {
		if (params.getUpdateStrategy().equals(UpdateEventGenerationStrategy.BRUTE_FORCE)) {
			generateUpdateRoundEventsByBruteForce(params);
		}
	}	

	/**
	 * The Brute Force strategy does not concern itself with making the data "realistic." That is,
	 * the events may or may not be correct according to the semantics of the HDS. For example, a 
	 * Death may be registered to an Individual, but the Visit recorded for the Death may or may not
	 * be relevant to the Individual (e.g. the Visit is at a different Location then the Individuals 
	 * current Residency). Additionally, the strategy does not try to uniformly distribute events. The
	 * events are assigned to individuals in sequential order. For example, generate all Death events,
	 * then generate all out migrations, then all in migrations, etc.
	 * 
	 * This strategy can be helpful when you just need data in the database. For example, if you need
	 * 500 Pregnancy Outcomes, and don't care about any other data, then this strategy works well in
	 * that context
	 * 
	 * @param params
	 */
	private void generateUpdateRoundEventsByBruteForce(DataGeneratorUpdateParams params) {
		initEventGenerators(params);
		long locationCount = getTotalLocationCount();
		long fieldWorkerCount = getTotalFieldWorkerCount();
		long individualCount = getIndividualEntityCount();
		Cursor<Individual> individualTableCursor = new Cursor<Individual>(dataGeneratorDao, Individual.class, individualCount);
		Visit visit = generateVisitAtRandomLocation(locationCount, fieldWorkerCount);

		// the algorithm is as follows:
		// iterate over each event generator, and attempt to generate the desired number
		// of events. in order to accomplish this, the entire list of individuals is 
		// iterated over for each event generator. When that list is exhausted, the generator is put in the
		// completed list whether or not it has reached its desired number of events.
		// Otherwise, it would require multiple iterations over the list of individuals
		// for each event generator, which requires more computation time. 
		// Unfortunately, this algorithm does not well for external in migrations because it is independent
		// of the list of individuals
		// Other approaches for handling this are welcomed
		
		EventGenerator extInMig = new ExternalInMigrationEventGenerator(dataGeneratorDao, entityService, params.getNumberOfExternalInMigrations(), siteProperties);
		while(!extInMig.hasReachedGeneratedTarget()) {
			extInMig.generateEvent(visit, null);
		}
		
		completedEventGenerators.add(extInMig);
		
		Iterator<EventGenerator> eventGeneratorIterator = eventGenerators.iterator();
		while(eventGeneratorIterator.hasNext()) {
			EventGenerator eg = eventGeneratorIterator.next();
			individualTableCursor.reset();
			
			while(individualTableCursor.hasNext()) {
				Individual indiv = individualTableCursor.next();
				if (indiv.getExtId().equals("UNK")) {
					continue;
				}

				eg.generateEvent(visit, indiv);
			}
			
			eventGeneratorIterator.remove();
			completedEventGenerators.add(eg);
		}
		
	}

	private long getIndividualEntityCount() {
		return dataGeneratorDao.getTotalCount(Individual.class);
	}

	private void initEventGenerators(DataGeneratorUpdateParams params) {
		eventGenerators.clear();
		completedEventGenerators.clear(); 
		
		eventGenerators.add(new PregnancyObservationEventGenerator(dataGeneratorDao, entityService, params.getNumberOfPregnancyObservations(), siteProperties));
		eventGenerators.add(new PregnancyOutcomeEventGenerator(dataGeneratorDao, entityService, params.getNumberOfPregnancyOutcomes(), siteProperties));
		eventGenerators.add(new OutMigrationEventGenerator(dataGeneratorDao, entityService, params.getNumberOfOutMigrations(), siteProperties));
		eventGenerators.add(new InternalInMigrationEventGenerator(dataGeneratorDao, entityService, params.getNumberOfInternalInMigrations(), siteProperties));
		eventGenerators.add(new DeathEventGenerator(dataGeneratorDao, entityService, params.getNumberOfDeaths(), siteProperties));
	}

	private Visit generateVisitAtRandomLocation(long locationTotalCount, long fieldWorkerTotalCount) {
		Location location = getRandomRowFromDatabase(Location.class, "locationName", locationTotalCount);
		FieldWorker fw = getRandomRowFromDatabase(FieldWorker.class, "extId", fieldWorkerTotalCount);
		
		Round round = findNextAvailableRoundForVisit(location);
		
		Visit visit = new Visit();
		visit.setCollectedBy(fw);
		visit.setExtId(RandomStringUtils.randomAlphabetic(8));
		visit.setVisitLocation(location);
		visit.setRoundNumber(round.getRoundNumber());
		visit.setVisitDate(DataGeneratorUtils.getDateBetween(round.getStartDate(), round.getEndDate()));
		
		if (!persistEntity(visit)) {
			return null;
		}
		
		return visit;
	}
	
	private Round findNextAvailableRoundForVisit(Location location) {
		Visit lastVisit = dataGeneratorDao.findUniqueByPropertyWithOrder(Visit.class, "visitLocation", location, "roundNumber", false);
		int candidateRound;
		
		if (lastVisit == null) {
			candidateRound = 1;
		} else {
			candidateRound = lastVisit.getRoundNumber() + 1;
		}
		
		Round round = dataGeneratorDao.findByProperty(Round.class, "roundNumber", candidateRound);
		
		if (round == null) {
			return createNewRound();
		} else {
			return round;
		}
	}

	private Round createNewRound() {
		Round lastRound = dataGeneratorDao.findByRowNumber(Round.class, 0, "roundNumber", false);
		Round newRound = new Round();
		newRound.setRoundNumber(lastRound.getRoundNumber() + 1);
		newRound.setStartDate(DataGeneratorUtils.getDateIncrementedBy(lastRound.getEndDate(), 1));
		newRound.setEndDate(DataGeneratorUtils.getDateIncrementedBy(newRound.getStartDate(), 10));
		
		persistEntity(newRound);
		
		return newRound;
	}

	/**
	 * Drop all rows from the entity tables in database
	 */
	public void purgeEntityTables() {
		dataGeneratorDao.purgeTable(InMigration.class);
		dataGeneratorDao.purgeTable(OutMigration.class);
		dataGeneratorDao.purgeTable(Residency.class);
		dataGeneratorDao.purgeTable(Death.class);
		dataGeneratorDao.purgeTable(Visit.class);
		dataGeneratorDao.purgeTable(Location.class);
		dataGeneratorDao.purgeTable(Relationship.class);
		dataGeneratorDao.purgeTable(Membership.class);
		dataGeneratorDao.purgeTable(SocialGroup.class);
		dataGeneratorDao.purgeTable(PregnancyObservation.class);
		dataGeneratorDao.purgeTable(Individual.class);
		dataGeneratorDao.purgeTable(Note.class);
		dataGeneratorDao.purgeTable(FieldWorker.class);
	}

	public void setDataGeneratorDao(DataGeneratorDao dataGeneratorDao) {
		this.dataGeneratorDao = dataGeneratorDao;
	}

	public void setLocationHierarchyService(
			LocationHierarchyService locationHierarchyService) {
		this.locationHierarchyService = locationHierarchyService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}	

	public void setSiteProperties(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
	}
}
