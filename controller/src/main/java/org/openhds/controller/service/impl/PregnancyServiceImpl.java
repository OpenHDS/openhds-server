package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.PregnancyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the pregnancy service interface
 * 
 * @author Dave
 *
 */
public class PregnancyServiceImpl implements PregnancyService {
	
	private EntityService entityService;
	private IndividualService individualService;
	private GenericDao genericDao;
	private SitePropertiesService siteProperties;
	
	public PregnancyServiceImpl(EntityService entityService, IndividualService individualService, GenericDao genericDao, SitePropertiesService siteProperties) {
		this.entityService = entityService;
		this.individualService = individualService;
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
	}
	
	public PregnancyObservation evaluatePregnancyObservation(PregnancyObservation entityItem) throws ConstraintViolations {
    	
		int age = (int) (CalendarUtil.daysBetween(entityItem.getMother().getDob(), entityItem.getRecordedDate()) / 365.25);
		if (age < siteProperties.getMinimumAgeOfPregnancy())
			throw new ConstraintViolations("The Mother specified is younger than the minimum age required to have a Pregnancy Observation.");	
		if (!checkDuplicatePregnancyObservation(entityItem.getMother())) 
    		throw new ConstraintViolations("The Mother specified already has a pending Pregnancy Observation.");	
    	if (individualService.getLatestEvent(entityItem.getMother()).equals("Death"))
    		throw new ConstraintViolations("A Pregnancy Observation cannot be created for a Mother who has a Death event.");	
    	
    	return entityItem;
	}
	
	public void validateGeneralPregnancyObservation(PregnancyObservation entityItem) throws ConstraintViolations {
		List<PregnancyObservation> list = genericDao.findListByMultiProperty(PregnancyObservation.class, getValueProperty("mother", entityItem.getMother()),																   									 getValueProperty("status", siteProperties.getDataStatusPendingCode()));
		if (list.size() > 1) 
    		throw new ConstraintViolations("The Mother specified already has a pending Pregnancy Observation.");	
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void closePregnancyObservation(Individual mother) {
		List<PregnancyObservation> obs = genericDao.findListByProperty(PregnancyObservation.class, "mother", mother);
		
		for(PregnancyObservation ob : obs) {
			if (ob.getStatus().equals(siteProperties.getDataStatusPendingCode())) {
				// found the corresponding pregnancy observation
				// now close it
				ob.setStatus(siteProperties.getDataStatusClosedCode());
				genericDao.update(ob);
				break;
			}
		}
	}
	
	public boolean checkDuplicatePregnancyObservation(Individual mother) {
		List<PregnancyObservation> list = genericDao.findListByProperty(PregnancyObservation.class, "mother", mother);		
		for (PregnancyObservation item : list) {
			if (item.getStatus().equals(siteProperties.getDataStatusPendingCode()))
				return false;
		}
		return true;		
	}

	public PregnancyOutcome evaluatePregnancyOutcome(PregnancyOutcome entityItem) throws ConstraintViolations {
		
		int age = (int) (CalendarUtil.daysBetween(entityItem.getMother().getDob(), entityItem.getOutcomeDate()) / 365.25);
		if (age < siteProperties.getMinimumAgeOfPregnancy())
			throw new ConstraintViolations("The Mother specified is younger than the minimum age required to have a Pregnancy Outcome.");	
    	if (individualService.getLatestEvent(entityItem.getMother()).equals("Death"))
    		throw new ConstraintViolations("A Pregnancy Outcome cannot be created for a Mother who has a Death event.");	
    	if (entityItem.getOutcomes().size() == 0) 
    		throw new ConstraintViolations("A Pregnancy Outcome cannot be created unless it has at least 1 outcome.");
		if (entityItem.getMother().getCurrentResidency() == null) 
			throw new ConstraintViolations("A Pregnancy Outcome cannot be created because a Residency record cannot be found for the mother.");

		return entityItem;
	}
	
	public List<PregnancyOutcome> getPregnancyOutcomesByIndividual(Individual individual) {
		return genericDao.findListByProperty(PregnancyOutcome.class, "mother", individual, true);
	}

	public List<PregnancyObservation> getPregnancyObservationByIndividual(Individual individual) {
		return genericDao.findListByProperty(PregnancyObservation.class, "mother", individual, true);
	}

	@Transactional(rollbackFor=Exception.class)
	public void createPregnancyOutcome(PregnancyOutcome pregOutcome) throws IllegalArgumentException, ConstraintViolations, SQLException {
		// the algorithm for completing a pregnancy outcome is as follows:
		// (Please note, this may change in the future)
		// If pregnancy outcome has live births
		// for each child:
		// - create the new child
		// - create new residency for child and set the location to the mothers current residency location
		// - create a membership to the mothers social group for which the social group is of type family 
		
		Location motherLocation = pregOutcome.getMother().getCurrentResidency().getLocation();
		
		List<PregnancyOutcome> persistedPOList = genericDao.findListByMultiProperty(PregnancyOutcome.class, 
				getValueProperty("mother", pregOutcome.getMother()),
				getValueProperty("outcomeDate", pregOutcome.getOutcomeDate()));
		
		PregnancyOutcome persistedPO = null;
		if (!persistedPOList.isEmpty())
			persistedPO = persistedPOList.get(0);

		for(Outcome outcome : pregOutcome.getOutcomes()) {
			if (persistedPO != null)
				persistedPO.addOutcome(outcome);
			
			if (!outcome.getType().equals(siteProperties.getLiveBirthCode())) {
				// not a live birth so individual, residency and membership not needed
				continue;
			}
			
			// create individual
			entityService.create(outcome.getChild());
			
			// use mothers location for the residency
			Residency residency = new Residency();
			residency.setStartDate( pregOutcome.getOutcomeDate() );
			residency.setIndividual(outcome.getChild());
			residency.setStartType(siteProperties.getBirthCode());
			residency.setLocation( motherLocation );
			residency.setCollectedBy(pregOutcome.getCollectedBy());
			residency.setEndType(siteProperties.getNotApplicableCode());
			entityService.create(residency);
			
			// create membership
			entityService.create(outcome.getChildMembership());
		}
		
		// close any pregnancy observation
		closePregnancyObservation(pregOutcome.getMother());
		
		if (persistedPO != null)
			entityService.save(persistedPO);
		// finally create the pregnancy outcome
		else
			entityService.create(pregOutcome);		
	}
		
	public List<PregnancyOutcome> findAllLiveBirthsBetweenInterval(Calendar startDate, Calendar endDate) {
		
		List<PregnancyOutcome> output = new ArrayList<PregnancyOutcome>();
		List<PregnancyOutcome> outcomes = genericDao.findAll(PregnancyOutcome.class, true);
		
		for (PregnancyOutcome outcome : outcomes) {			
			Calendar outcomeDate = outcome.getOutcomeDate();
			if ((outcomeDate.after(startDate) || outcomeDate.equals(startDate)) && 
					(outcomeDate.before(endDate))) {
				
				List<Outcome> allOutcomes = outcome.getOutcomes();
				for (Outcome o : allOutcomes) 
					if (o.getType().equals(siteProperties.getLiveBirthCode())) {
						output.add(outcome);
				}
			}
		}
		return output;
	}
	
	public int findAllBirthsBetweenIntervalByGender(Calendar startDate, Calendar endDate, int flag) {
		
		int count = 0;
		List<PregnancyOutcome> outcomes = genericDao.findAll(PregnancyOutcome.class, true);
		
		for (PregnancyOutcome outcome : outcomes) {			
			Calendar outcomeDate = outcome.getOutcomeDate();
			if ((outcomeDate.after(startDate) || outcomeDate.equals(startDate)) && 
					(outcomeDate.before(endDate))) {
				
				List<Outcome> allOutcomes = outcome.getOutcomes();
				for (Outcome o : allOutcomes) {		
					if (o.getType().equals(siteProperties.getLiveBirthCode())) {
						// male
						if (flag == 0) {
							if (o.getChild().getGender().equals(siteProperties.getMaleCode())) {
								if (o.getType().equals(siteProperties.getLiveBirthCode())) {
									count++;
								}
							}
						}
						// female
						else {
							if (o.getChild().getGender().equals(siteProperties.getFemaleCode())) {
								if (o.getType().equals(siteProperties.getLiveBirthCode())) {
									count++;
								}
							}
						}
					}
				}
			}
		}
		return count;
	}
		
	private ValueProperty getValueProperty(final String propertyName, final Object propertyValue) {
		return new ValueProperty() {

            public String getPropertyName() {
                return propertyName;
            }

            public Object getValue() {
                return propertyValue;
            }
        };		
	}
}