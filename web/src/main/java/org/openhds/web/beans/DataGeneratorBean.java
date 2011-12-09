package org.openhds.web.beans;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.openhds.datageneration.service.DataGeneratorService;
import org.openhds.datageneration.utils.DataGeneratorUpdateParams;
import org.openhds.datageneration.utils.DataGeneratorUtils;

public class DataGeneratorBean {
	
	// Baseline Census fields
	private Integer numLocations = 1000, numFieldWorkers = 100;
	private Integer numIndividuals = 20000, minIndividualAge = 0, maxIndividualAge = 80,
					minIndividualPerSocialGroup = 3, maxIndividualPerSocialGroup = 10,
					baselineRelationshipsToCreate = 500;
	private Boolean useIdScheme = false;
	private Boolean enableBaselineCensus = true;
	
	// Update Round fields
	private Integer minNumberOfEventsPerVisit = 1, maxNumberOfEventsPerVisit = 10,
					numberOfInternalInMigrations = 1000, numberOfExternalInMigrations = 1000,
					numberOfOutMigrations = 1000, numberOfDeaths = 1000,
					numberOfPregnancyObservations = 1000, numberOfPregnancyOutcomes = 1000;
	private Boolean runUpdateRound = true;
	
	SelectItem[] updateStrategies = new SelectItem[] { new SelectItem("Brute Force") };
	String updateStragety;
	
	private Date baselineStartDate, baselineEndDate;
	
	private final DataGeneratorService dataGeneratorService;
	
	public DataGeneratorBean(DataGeneratorService dataGeneratorService) {
		this.dataGeneratorService = dataGeneratorService;
	}
	
	public String generateData() {
		if (enableBaselineCensus) {
			dataGeneratorService.purgeEntityTables();
			dataGeneratorService.generateLocationData(numFieldWorkers, numLocations, useIdScheme);
			dataGeneratorService.generateBaselineCensusData(numIndividuals, minIndividualAge, 
					maxIndividualAge, minIndividualPerSocialGroup, maxIndividualPerSocialGroup, baselineRelationshipsToCreate,
					baselineStartDate, baselineEndDate);
		}
		
		if (runUpdateRound) {
			generateUpdateRoundEvents();
		}
		return null;
	}
	
	private void generateUpdateRoundEvents() {
		DataGeneratorUpdateParams.Builder builder = new DataGeneratorUpdateParams.Builder();
		builder.minNumberOfEventsPerVisit(minNumberOfEventsPerVisit)
			   .maxNumberOfEventsPerVisit(maxNumberOfEventsPerVisit)
			   .numberOfInternalInMigrations(numberOfInternalInMigrations)
			   .numberOfExternalInMigrations(numberOfExternalInMigrations)
			   .numberOfOutMigrations(numberOfOutMigrations)
			   .numberOfDeaths(numberOfDeaths)
			   .numberOfPregnancyObservations(numberOfPregnancyObservations)
			   .numberOfPregnancyOutcomes(numberOfPregnancyOutcomes);
		
		try {
			dataGeneratorService.generateUpdateRoundEvents(builder.build());
		} catch(Exception e) {
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}	
	
	public Integer getNumLocations() {
		return numLocations;
	}
	
	public void setNumLocations(Integer numLocations) {
		this.numLocations = numLocations;
	}

	public Integer getNumFieldWorkers() {
		return numFieldWorkers;
	}

	public void setNumFieldWorkers(Integer numFieldWorkers) {
		this.numFieldWorkers = numFieldWorkers;
	}

	public Integer getNumIndividuals() {
		return numIndividuals;
	}
	public void setNumIndividuals(Integer numIndividuals) {
		this.numIndividuals = numIndividuals;
	}

	public Boolean getUseIdScheme() {
		return useIdScheme;
	}

	public void setUseIdScheme(Boolean useIdScheme) {
		this.useIdScheme = useIdScheme;
	}

	public Boolean getEnableBaselineCensus() {
		return enableBaselineCensus;
	}

	public void setEnableBaselineCensus(Boolean enableBaselineCensus) {
		this.enableBaselineCensus = enableBaselineCensus;
	}

	public Integer getMinIndividualAge() {
		return minIndividualAge;
	}

	public void setMinIndividualAge(Integer minIndividualAge) {
		this.minIndividualAge = minIndividualAge;
	}

	public Integer getMaxIndividualAge() {
		return maxIndividualAge;
	}

	public void setMaxIndividualAge(Integer maxIndividualAge) {
		this.maxIndividualAge = maxIndividualAge;
	}

	public Integer getMinIndividualPerSocialGroup() {
		return minIndividualPerSocialGroup;
	}

	public void setMinIndividualPerSocialGroup(Integer minIndividualPerSocialGroup) {
		this.minIndividualPerSocialGroup = minIndividualPerSocialGroup;
	}

	public Integer getMaxIndividualPerSocialGroup() {
		return maxIndividualPerSocialGroup;
	}

	public void setMaxIndividualPerSocialGroup(Integer maxIndividualPerSocialGroup) {
		this.maxIndividualPerSocialGroup = maxIndividualPerSocialGroup;
	}

	public Boolean getRunUpdateRound() {
		return runUpdateRound;
	}

	public void setRunUpdateRound(Boolean runUpdateRound) {
		this.runUpdateRound = runUpdateRound;
	}

	public Integer getMinNumberOfEventsPerVisit() {
		return minNumberOfEventsPerVisit;
	}

	public void setMinNumberOfEventsPerVisit(Integer minNumberOfEventsPerVisit) {
		this.minNumberOfEventsPerVisit = minNumberOfEventsPerVisit;
	}

	public Integer getMaxNumberOfEventsPerVisit() {
		return maxNumberOfEventsPerVisit;
	}

	public void setMaxNumberOfEventsPerVisit(Integer maxNumberOfEventsPerVisit) {
		this.maxNumberOfEventsPerVisit = maxNumberOfEventsPerVisit;
	}

	public Integer getNumberOfInternalInMigrations() {
		return numberOfInternalInMigrations;
	}

	public void setNumberOfInternalInMigrations(Integer numberOfInternalInMigrations) {
		this.numberOfInternalInMigrations = numberOfInternalInMigrations;
	}

	public Integer getNumberOfExternalInMigrations() {
		return numberOfExternalInMigrations;
	}

	public void setNumberOfExternalInMigrations(Integer numberOfExternalInMigrations) {
		this.numberOfExternalInMigrations = numberOfExternalInMigrations;
	}

	public Integer getNumberOfOutMigrations() {
		return numberOfOutMigrations;
	}

	public void setNumberOfOutMigrations(Integer numberOfOutMigrations) {
		this.numberOfOutMigrations = numberOfOutMigrations;
	}

	public Integer getNumberOfDeaths() {
		return numberOfDeaths;
	}

	public void setNumberOfDeaths(Integer numberOfDeaths) {
		this.numberOfDeaths = numberOfDeaths;
	}

	public Integer getNumberOfPregnancyObservations() {
		return numberOfPregnancyObservations;
	}

	public void setNumberOfPregnancyObservations(
			Integer numberOfPregnancyObservations) {
		this.numberOfPregnancyObservations = numberOfPregnancyObservations;
	}

	public Integer getNumberOfPregnancyOutcomes() {
		return numberOfPregnancyOutcomes;
	}

	public void setNumberOfPregnancyOutcomes(Integer numberOfPregnancyOutcomes) {
		this.numberOfPregnancyOutcomes = numberOfPregnancyOutcomes;
	}

	public String getUpdateStragety() {
		return updateStragety;
	}

	public void setUpdateStragety(String updateStragety) {
		this.updateStragety = updateStragety;
	}

	public SelectItem[] getUpdateStrategies() {
		return updateStrategies;
	}

	public Date getBaselineStartDate() {
		if (baselineStartDate == null) {
			baselineStartDate = DataGeneratorUtils.getDateInPast(370).getTime();
		}
		return baselineStartDate;
	}

	public void setBaselineStartDate(Date baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}

	public Date getBaselineEndDate() {
		if (baselineEndDate == null) {
			baselineEndDate = DataGeneratorUtils.getDateInPast(365).getTime();
		}
		return baselineEndDate;
	}

	public void setBaselineEndDate(Date baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
	}

	public Integer getBaselineRelationshipsToCreate() {
		return baselineRelationshipsToCreate;
	}

	public void setBaselineRelationshipsToCreate(
			Integer baselineRelationshipsToCreate) {
		this.baselineRelationshipsToCreate = baselineRelationshipsToCreate;
	}
}
