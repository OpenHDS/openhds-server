package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.idgeneration.IdScheme;
import org.openhds.controller.idgeneration.IdSchemeResource;
import org.openhds.controller.idgeneration.IndividualGenerator;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.transaction.annotation.Transactional;

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
		if(entityItem.getExtId()==null)
			assignId(entityItem);
		if (findIndivById(entityItem.getExtId()) != null)
			throw new ConstraintViolations("The Id specified already exists");
				
		validateIdLength(entityItem);
		
		return entityItem;
	}
	
    @Transactional
    public void createIndividual(Individual individual) throws ConstraintViolations {
        assignId(individual);
        evaluateIndividual(individual);
        try {
            entityService.create(individual);
        } catch (IllegalArgumentException e) {
        } catch (SQLException e) {
        }
    }

    private void assignId(Individual individual) throws ConstraintViolations {
        String id = individual.getExtId() == null ? "" : individual.getExtId();
        if (id.trim().isEmpty() && indivGen.generated) {
            generateId(individual);
        }
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
        List<Individual> list = genericDao.findListByPropertyPrefix(Individual.class, "extId", term, 10, true);
        for (Individual indiv : list) {
            ids.add(indiv.getExtId());
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
    
    private class LastEvent{
    	String eventType;
    	Calendar eventDate;
    	
    	public LastEvent(String eventType,Calendar eventDate){
    		this.eventType=eventType;
    		this.eventDate=eventDate;
    	}
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
		PregnancyOutcome po = genericDao.findUniqueByPropertyWithOrder(PregnancyOutcome.class, "mother", individual, "outcomeDate", false);
		PregnancyObservation pregObs = genericDao.findUniqueByPropertyWithOrder(PregnancyObservation.class, "mother", individual, "recordedDate", false);
		Relationship relationship = genericDao.findUniqueByPropertyWithOrder(Relationship.class, "individualA", individual, "startDate", false);
		Relationship relationship2 = genericDao.findUniqueByPropertyWithOrder(Relationship.class, "individualB", individual, "startDate", false);
		Membership membership = genericDao.findUniqueByPropertyWithOrder(Membership.class, "individual", individual, "startDate", false);
		
		
		List<LastEvent> events= new ArrayList<LastEvent>();
		
		events.add(new LastEvent("Birth. Create membership for this individual ",individual.getDob()));
		String pobsDate=pregObs.getRecordedDate().toString();
		if(om!=null)
			events.add(new LastEvent("Out Migration",om.getRecordedDate()));
		if(in!=null)
			events.add(new LastEvent("In Migration. Create membership for this individual",in.getRecordedDate()));
		if(po!=null)
			events.add(new LastEvent("Pregnancy Outcome",po.getOutcomeDate()));
		if(pregObs!=null)
			events.add(new LastEvent("Pregnancy Observation",pregObs.getRecordedDate()));
		if(relationship!=null)
			events.add(new LastEvent("Relationship",relationship.getStartDate()));
		if(relationship2!=null)
			events.add(new LastEvent("Relationship",relationship2.getStartDate()));
		if(membership!=null)
			events.add(new LastEvent("Membership",membership.getStartDate()));
		
		Collections.sort(events, new Comparator<LastEvent>() {
			  public int compare(LastEvent o1, LastEvent o2) {
			      if (o1.eventDate == null || o2.eventDate == null)
			        return 0;
			      return o1.eventDate.compareTo(o2.eventDate);
			  }
			});
		
		LastEvent le = new LastEvent(null,null);
		LastEvent equallyLastEvent = new LastEvent(null,null);
		if(!events.isEmpty() &&events.size()>1){
			le = events.get(events.size()-1);
			equallyLastEvent = events.get(events.size()-2);
			if(le.eventType=="In Migration. Create membership for this individual" &&
					equallyLastEvent.eventType=="Membership"
					&& le.eventDate==equallyLastEvent.eventDate){
				le = equallyLastEvent;
			}
			
		}else if (events.size()==1)
			le = events.get(0);
		//events.clear();
		
		
		return le.eventType==null ? "" : le.eventType;
		
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

    @Override
    public List<Individual> getAllIndividuals() {
        return genericDao.findAllWithoutProperty(Individual.class, "extId", "UNK");
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public long getTotalIndividualCount() {
        // subtracting 1 for the Unknown Individual
        return genericDao.getTotalCount(Individual.class) - 1;
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public List<Individual> getAllIndividualsInRange(int start, int size) {
        return genericDao.findPaged(Individual.class, "extId", start, size);
    }
}