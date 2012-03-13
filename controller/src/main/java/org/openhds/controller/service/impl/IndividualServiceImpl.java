package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.beans.RecordGroup;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.idgeneration.IdScheme;
import org.openhds.controller.idgeneration.IdSchemeResource;
import org.openhds.controller.idgeneration.IndividualGenerator;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public class IndividualServiceImpl implements IndividualService {
	
	private GenericDao genericDao;
	private SitePropertiesService properties;
	private IndividualGenerator indivGen;
	private EntityService entityService;
	private IdSchemeResource resource;

	public IndividualServiceImpl(GenericDao genericDao, IndividualGenerator generator, SitePropertiesService properties, EntityService entityService, IdSchemeResource resource) {
		this.genericDao = genericDao;
		this.indivGen = generator;
		this.properties = properties;
		this.entityService = entityService;
		this.resource = resource;
	}
	
	public Individual evaluateIndividual(Individual entityItem) throws ConstraintViolations {	
		if (indivGen.generated)	
			return generateId(entityItem);
		
		if (findIndivById(entityItem.getExtId()) != null)
			throw new ConstraintViolations("The Id specified already exists");
				
		validateIdLength(entityItem);
		
		return entityItem;
	}
	
	public Individual validateIdLength(Individual entityItem) throws ConstraintViolations {
		indivGen.validateIdLength(entityItem.getExtId(), indivGen.getIdScheme());
		return entityItem;
	}
	
	public void validateGeneralIndividual(Individual indiv) throws ConstraintViolations {
		if (indiv.getGender().equals(properties.getUnknownIdentifier()))
			throw new ConstraintViolations("The Individual is partially completed");
	}
	
	public Individual generateId(Individual entityItem) throws ConstraintViolations {
		entityItem.setExtId(indivGen.generateId(entityItem));
		return entityItem;
	}
		
	/**
	 * Used to create a custom id based off a counter. The custom id generated
	 * will be incremented the number of times of the counter specified. This
	 * routine is used to create new individual ids from pregnancy outcome.
	 */
	public String generateIdWithBound(Individual entityItem, int count) throws ConstraintViolations {
		IdScheme scheme = resource.getIdSchemeByName("Individual");
		String result = entityItem.getExtId().concat(indivGen.buildNumberWithBound(entityItem, scheme));
		String output = result;
		int index = 1;
		while (index < count) {
			output = indivGen.incrementId(result);
			result = output;
			index++;
		}
		return output;
	}
		
    /**
     * Retrieves all Individual extId's that contain the term provided.
     * Used in performing autocomplete.
     */
    public List<String> getIndividualExtIds(String term) {
    	List<String> ids = new ArrayList<String>();
    	List<Individual> list = genericDao.findAll(Individual.class, true);
    	Iterator<Individual> itr = list.iterator();
    	while(itr.hasNext()) {
    		Individual item = itr.next();
    		if (item.getExtId().toLowerCase().contains(term.toLowerCase())) 
    			ids.add(item.getExtId());
    	}
    	return ids;
    }
    
    public Individual findIndivById(String indivExtId, String msg) throws Exception {
        Individual indiv = genericDao.findByProperty(Individual.class, "extId", indivExtId);
        if (indiv == null) {
            throw new Exception(msg);
        }
        return indiv;
    }   
    
    @Transactional(readOnly=true)
    public Individual findIndivById(String indivExtId) {
        Individual indiv = genericDao.findByProperty(Individual.class, "extId", indivExtId);
        return indiv;
    }    
	
    @Transactional(readOnly=true)
	public String getLatestEvent(Individual individual) {
		// it's possible the individual passed in hasn't actually been persisted
		// yet. This is a guard against throwing a Transient Object exception
		if (findIndivById(individual.getExtId()) == null) {
			return "";
		}
		
		// determine the latest event
		// a Death event should in theory be the last of any events
		Death death = genericDao.findByProperty(Death.class, "individual", individual, true);
		
		if (death != null) {
			return "Death";
		}
		// otherwise determine latest event
		OutMigration om = genericDao.findUniqueByPropertyWithOrder(OutMigration.class, "individual", individual, "recordedDate", false);
		InMigration in = genericDao.findUniqueByPropertyWithOrder(InMigration.class, "individual", individual, "recordedDate", false);
		
		if (om == null && in == null) {
			return "";
		}
		
		if (om != null && in == null) {
			return "Out Migration";
		}
		
		if (in != null && om == null) {
			return "In Migration";
		}
		
		if (in.getRecordedDate().compareTo(om.getRecordedDate()) >= 0) {
			return "In Migration";
		}
		
		return "Out Migration";
	}

	@Transactional(rollbackFor=Exception.class)
	public Individual createTemporaryIndividualWithExtId(String extId, FieldWorker collectedBy) throws Exception {
		Individual head = new Individual();
		head.setFirstName("Temporary Individual");
		head.setLastName("Temporary Individual");
		head.setExtId(extId);
		head.setMother(findIndivById(properties.getUnknownIdentifier()));
		head.setFather(findIndivById(properties.getUnknownIdentifier()));
		head.setGender(properties.getUnknownIdentifier());
		head.setCollectedBy(collectedBy);
		
		entityService.create(head);
		return head;
	}
	
	// this is used for output to the DHIS
	// iterates through the deaths and categorizes them according to age group
    public void setPopulationForAgeGroupsByLocation(RecordGroup populationGroup, List<String> hierarchyIds) {
    	
		List<Individual> indivs = genericDao.findAll(Individual.class, true);
		
		for (Individual indiv : indivs) {
			
			if (indiv.getCurrentResidency() == null) 		
				continue;	
			
			if (hierarchyIds.contains(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId())) {
				
				Calendar midpoint = CalendarUtil.getMidPointDate(populationGroup.getStart(), populationGroup.getEnd());
				long ageInDays = CalendarUtil.daysBetween(indiv.getDob(), midpoint);
				long ageInYears = (long) (ageInDays / 365.25);
								
				if (ageInYears >= 0 && ageInYears < 5) 
					processGroup(populationGroup, indiv, 0);
				else if (ageInYears >= 5 && ageInYears < 10) 
					processGroup(populationGroup, indiv, 1);
				else if (ageInYears >= 10 && ageInYears < 15) 
					processGroup(populationGroup, indiv, 2);
				else if (ageInYears >= 15 && ageInYears < 20) 
					processGroup(populationGroup, indiv, 3);
				else if (ageInYears >= 20 && ageInYears < 25) 
					processGroup(populationGroup, indiv, 4);				
				else if (ageInYears >= 25 && ageInYears < 30) 
					processGroup(populationGroup, indiv, 5);			
				else if (ageInYears >= 30 && ageInYears < 35) 
					processGroup(populationGroup, indiv, 6);		
				else if (ageInYears >= 35 && ageInYears < 40) 
					processGroup(populationGroup, indiv, 7);		
				else if (ageInYears >= 40 && ageInYears < 45) 
					processGroup(populationGroup, indiv, 8);		
				else if (ageInYears >= 45 && ageInYears < 50) 
					processGroup(populationGroup, indiv, 9);		
				else if (ageInYears >= 50 && ageInYears < 55) 
					processGroup(populationGroup, indiv, 10);		
				else if (ageInYears >= 55 && ageInYears < 60) 
					processGroup(populationGroup, indiv, 11);		
				else if (ageInYears >= 60 && ageInYears < 65) 
					processGroup(populationGroup, indiv, 12);		
				else if (ageInYears >= 65 && ageInYears < 70) 
					processGroup(populationGroup, indiv, 13);		
				else if (ageInYears >= 70 && ageInYears < 75) 
					processGroup(populationGroup, indiv, 14);			
				else if (ageInYears >= 75 && ageInYears < 80) 
					processGroup(populationGroup, indiv, 15);		
				else if (ageInYears >= 80 && ageInYears < 85) 
					processGroup(populationGroup, indiv, 16);		
				else if (ageInYears >= 85 && ageInYears < 90) 
					processGroup(populationGroup, indiv, 17);		
				else if (ageInYears >= 90 && ageInYears < 95) 
					processGroup(populationGroup, indiv, 18);		
				else if (ageInYears >= 95) 
					processGroup(populationGroup, indiv, 19);
				
				// 0 to 28 days
				if (ageInDays > 0 && ageInDays <= 28) 	
					processGroup(populationGroup, indiv, 20);
				// 29 to 11 months (334.81 days in 11 months)
				else if (ageInDays > 28 && ageInDays <= 334.81) 
					processGroup(populationGroup, indiv, 21);
				// 12 - 59 months 
				else if (ageInDays > 334.81 && ageInDays <= 1826.21) 
					processGroup(populationGroup, indiv, 22);
				// 10 to 19 years
				else if (ageInYears >= 10 && ageInYears < 20) 
					processGroup(populationGroup, indiv, 23);
				// 20 to 40 years
				else if (ageInYears >= 20 && ageInYears < 40) 
					processGroup(populationGroup, indiv, 24);
				// > 40 years
				else if (ageInYears >= 40) 
					processGroup(populationGroup, indiv, 25);
			}
		}
    }
    
    private void processGroup(RecordGroup group, Individual indiv, int index) {
    	if (indiv.getGender().equals(properties.getMaleCode()))
    		group.getRecord().addMaleCountForLocationAndAgeGroup(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId(), index);
		else if (indiv.getGender().equals(properties.getFemaleCode()))
			group.getRecord().addFemaleCountForLocationAndAgeGroup(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId(), index);
    }
}