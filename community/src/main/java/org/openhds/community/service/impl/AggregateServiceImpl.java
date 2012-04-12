package org.openhds.community.service.impl;

import java.util.Calendar;
import java.util.List;
import org.openhds.community.beans.RecordGroup;
import org.openhds.community.service.AggregateService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;

public class AggregateServiceImpl implements AggregateService {
	
	GenericDao genericDao;
	SitePropertiesService siteProperties;
	
	public AggregateServiceImpl(GenericDao genericDao, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
	}
	
	// iterates through the deaths and categorizes them according to age group
	public void setDeathsForAgeGroupsByLocation(RecordGroup deathGroup, List<String> hierarchyIds) {
				
		List<Death> deaths = genericDao.findAll(Death.class, true);
		
		for (Death death : deaths) {
			
			if (hierarchyIds.contains(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId())) {

				long ageInDays = death.getAgeAtDeath();
				long ageInYears = (long) (ageInDays / 365.25);
				Calendar deathDate = death.getDeathDate();
				
				if ((deathDate.after(deathGroup.getStart()) || deathDate.equals(deathGroup.getStart())) && 
					(deathDate.before(deathGroup.getEnd()))) {
				
					// 0 to 28 days
					if (ageInDays > 0 && ageInDays <= 28) 	
						processGroupForDeath(deathGroup, death, 0);
					// 29 to 11 months (334.81 days in 11 months)
					else if (ageInDays > 28 && ageInDays <= 334.81) 
						processGroupForDeath(deathGroup, death, 1);
					// 12 - 59 months 
					else if (ageInDays > 334.81 && ageInDays <= 1826.21) 
						processGroupForDeath(deathGroup, death, 2);
					// 5 to 9 years
					else if (ageInYears > 5 && ageInYears < 10) 
						processGroupForDeath(deathGroup, death, 3);
					// 10 to 19 years
					else if (ageInYears >= 10 && ageInYears < 20) 
						processGroupForDeath(deathGroup, death, 4);
					// 20 to 40 years
					else if (ageInYears >= 20 && ageInYears < 40) 
						processGroupForDeath(deathGroup, death, 5);
					// > 40 years
					else if (ageInYears >= 40) 
						processGroupForDeath(deathGroup, death, 6);
					
					if (ageInYears >= 0 && ageInYears < 5) 
						processGroupForDeath(deathGroup, death, 7);
					else if (ageInYears >= 10 && ageInYears < 15) 
						processGroupForDeath(deathGroup, death, 8);
					else if (ageInYears >= 15 && ageInYears < 20) 
						processGroupForDeath(deathGroup, death, 9);
					else if (ageInYears >= 20 && ageInYears < 25) 
						processGroupForDeath(deathGroup, death, 10);				
					else if (ageInYears >= 25 && ageInYears < 30) 
						processGroupForDeath(deathGroup, death, 11);			
					else if (ageInYears >= 30 && ageInYears < 35) 
						processGroupForDeath(deathGroup, death, 12);		
					else if (ageInYears >= 35 && ageInYears < 40) 
						processGroupForDeath(deathGroup, death, 13);		
					else if (ageInYears >= 40 && ageInYears < 45) 
						processGroupForDeath(deathGroup, death, 14);		
					else if (ageInYears >= 45 && ageInYears < 50) 
						processGroupForDeath(deathGroup, death, 15);	
					else if (ageInYears >= 50 && ageInYears < 55) 
						processGroupForDeath(deathGroup, death, 16);		
					else if (ageInYears >= 55 && ageInYears < 60) 
						processGroupForDeath(deathGroup, death, 17);		
					else if (ageInYears >= 60 && ageInYears < 65) 
						processGroupForDeath(deathGroup, death, 18);		
					else if (ageInYears >= 65 && ageInYears < 70) 
						processGroupForDeath(deathGroup, death, 19);		
					else if (ageInYears >= 70 && ageInYears < 75) 
						processGroupForDeath(deathGroup, death, 20);			
					else if (ageInYears >= 75 && ageInYears < 80) 
						processGroupForDeath(deathGroup, death, 21);		
					else if (ageInYears >= 80 && ageInYears < 85) 
						processGroupForDeath(deathGroup, death, 22);		
					else if (ageInYears >= 85 && ageInYears < 90) 
						processGroupForDeath(deathGroup, death, 23);		
					else if (ageInYears >= 90 && ageInYears < 95) 
						processGroupForDeath(deathGroup, death, 24);		
					else if (ageInYears >= 95) 
						processGroupForDeath(deathGroup, death, 25);
				}
			}
		}
	}
	
	// iterates through the deaths and categorizes them according to age group
    public void setPopulationForAgeGroupsByLocation(List<RecordGroup> populationGroups, List<String> hierarchyIds) {
    	
		List<Individual> indivs = genericDao.findAll(Individual.class, true);
		
		for (RecordGroup populationGroup : populationGroups) {   
		
			for (Individual indiv : indivs) {
				
				if (indiv.getCurrentResidency() == null) 		
					continue;	
				
				if (hierarchyIds.contains(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId())) {
					
					Calendar midpoint = CalendarUtil.getMidPointDate(populationGroup.getStart(), populationGroup.getEnd());
					long ageInDays = CalendarUtil.daysBetween(indiv.getDob(), midpoint);
					long ageInYears = (long) (ageInDays / 365.25);
									
					if (ageInYears >= 0 && ageInYears < 5) 
						processGroupForIndividual(populationGroup, indiv, 0);
					else if (ageInYears >= 5 && ageInYears < 10) 
						processGroupForIndividual(populationGroup, indiv, 1);
					else if (ageInYears >= 10 && ageInYears < 15) 
						processGroupForIndividual(populationGroup, indiv, 2);
					else if (ageInYears >= 15 && ageInYears < 20) 
						processGroupForIndividual(populationGroup, indiv, 3);
					else if (ageInYears >= 20 && ageInYears < 25) 
						processGroupForIndividual(populationGroup, indiv, 4);				
					else if (ageInYears >= 25 && ageInYears < 30) 
						processGroupForIndividual(populationGroup, indiv, 5);			
					else if (ageInYears >= 30 && ageInYears < 35) 
						processGroupForIndividual(populationGroup, indiv, 6);		
					else if (ageInYears >= 35 && ageInYears < 40) 
						processGroupForIndividual(populationGroup, indiv, 7);		
					else if (ageInYears >= 40 && ageInYears < 45) 
						processGroupForIndividual(populationGroup, indiv, 8);		
					else if (ageInYears >= 45 && ageInYears < 50) 
						processGroupForIndividual(populationGroup, indiv, 9);		
					else if (ageInYears >= 50 && ageInYears < 55) 
						processGroupForIndividual(populationGroup, indiv, 10);		
					else if (ageInYears >= 55 && ageInYears < 60) 
						processGroupForIndividual(populationGroup, indiv, 11);		
					else if (ageInYears >= 60 && ageInYears < 65) 
						processGroupForIndividual(populationGroup, indiv, 12);		
					else if (ageInYears >= 65 && ageInYears < 70) 
						processGroupForIndividual(populationGroup, indiv, 13);		
					else if (ageInYears >= 70 && ageInYears < 75) 
						processGroupForIndividual(populationGroup, indiv, 14);			
					else if (ageInYears >= 75 && ageInYears < 80) 
						processGroupForIndividual(populationGroup, indiv, 15);		
					else if (ageInYears >= 80 && ageInYears < 85) 
						processGroupForIndividual(populationGroup, indiv, 16);		
					else if (ageInYears >= 85 && ageInYears < 90) 
						processGroupForIndividual(populationGroup, indiv, 17);		
					else if (ageInYears >= 90 && ageInYears < 95) 
						processGroupForIndividual(populationGroup, indiv, 18);		
					else if (ageInYears >= 95) 
						processGroupForIndividual(populationGroup, indiv, 19);
					
					// 0 to 28 days
					if (ageInDays > 0 && ageInDays <= 28) 	
						processGroupForIndividual(populationGroup, indiv, 20);
					// 29 to 11 months (334.81 days in 11 months)
					else if (ageInDays > 28 && ageInDays <= 334.81) 
						processGroupForIndividual(populationGroup, indiv, 21);
					// 12 - 59 months 
					else if (ageInDays > 334.81 && ageInDays <= 1826.21) 
						processGroupForIndividual(populationGroup, indiv, 22);
					// 10 to 19 years
					else if (ageInYears >= 10 && ageInYears < 20) 
						processGroupForIndividual(populationGroup, indiv, 23);
					// 20 to 40 years
					else if (ageInYears >= 20 && ageInYears < 40) 
						processGroupForIndividual(populationGroup, indiv, 24);
					// > 40 years
					else if (ageInYears >= 40) 
						processGroupForIndividual(populationGroup, indiv, 25);
				}
			}
		}
    }
    
	// iterates through the outcomes and categorizes the live births according to gender
	public void setPregnancyOutcomesByLocation(RecordGroup pregnancyGroup, List<String> hierarchyIds) {

		List<PregnancyOutcome> outcomes = genericDao.findAll(PregnancyOutcome.class, true);
		
		for (PregnancyOutcome outcome : outcomes) {
			
			if (hierarchyIds.contains(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId())) {
				
				Calendar outcomeDate = outcome.getOutcomeDate();
				if ((outcomeDate.after(pregnancyGroup.getStart()) || outcomeDate.equals(pregnancyGroup.getStart())) && 
						(outcomeDate.before(pregnancyGroup.getEnd()))) {
					
					List<Outcome> allOutcomes = outcome.getOutcomes();
					
					for (Outcome o : allOutcomes) {
						
						if (o.getType().equals(siteProperties.getLiveBirthCode())) {
							if (o.getChild().getGender().equals(siteProperties.getMaleCode())) 
								pregnancyGroup.getRecord().addMaleCountForLocationAndAgeGroup(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId(), 0);
							if (o.getChild().getGender().equals(siteProperties.getFemaleCode()))
								pregnancyGroup.getRecord().addFemaleCountForLocationAndAgeGroup(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId(), 0);
						}
						else if (o.getType().equals(siteProperties.getStillBirthCode())) {
							pregnancyGroup.getRecord().addMaleCountForLocationAndAgeGroup(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId(), 1);
						}
						else if (o.getType().equals(siteProperties.getMiscarriageCode())) {
							pregnancyGroup.getRecord().addMaleCountForLocationAndAgeGroup(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId(), 2);
						}
						else if (o.getType().equals(siteProperties.getAbortionCode())) {
							pregnancyGroup.getRecord().addMaleCountForLocationAndAgeGroup(outcome.getVisit().getVisitLocation().getLocationLevel().getExtId(), 3);
						}
					}
				}
			}
		}
	}
	
	private void processGroupForDeath(RecordGroup group, Death death, int index) {
		if (death.getIndividual().getGender().equals(siteProperties.getMaleCode()))
			group.getRecord().addMaleCountForLocationAndAgeGroup(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId(), index);
		else if (death.getIndividual().getGender().equals(siteProperties.getFemaleCode()))
			group.getRecord().addFemaleCountForLocationAndAgeGroup(death.getVisitDeath().getVisitLocation().getLocationLevel().getExtId(), index);
	}
	
    private void processGroupForIndividual(RecordGroup group, Individual indiv, int index) {
    	if (indiv.getGender().equals(siteProperties.getMaleCode()))
    		group.getRecord().addMaleCountForLocationAndAgeGroup(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId(), index);
		else if (indiv.getGender().equals(siteProperties.getFemaleCode()))
			group.getRecord().addFemaleCountForLocationAndAgeGroup(indiv.getCurrentResidency().getLocation().getLocationLevel().getExtId(), index);
    }
}
