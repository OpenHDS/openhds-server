package org.openhds.datageneration.service;

import java.util.Date;

import org.openhds.datageneration.utils.DataGeneratorUpdateParams;

public interface DataGeneratorService {

	 void generateLocationData(int numFieldWorkers, int numLocations, boolean useIdScheme);
	 
	 void generateBaselineCensusData(int numIndividuals, int minAge, int maxAge, int minIndivInSocialGroup, int maxIndivInSocialGroup, 
				int numMaritalRelationships, Date baselineStartDate, Date baselineEndDate);
	 
	 void generateUpdateRoundEvents(DataGeneratorUpdateParams params) throws Exception;
	 
	 void purgeEntityTables();
}
