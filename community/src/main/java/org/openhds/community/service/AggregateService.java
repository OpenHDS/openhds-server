package org.openhds.community.service;

import java.util.List;

import org.openhds.community.beans.RecordGroup;

public interface AggregateService {
	
	void setDeathsForAgeGroupsByLocation(RecordGroup deathGroup, List<String> hierarchyIds);
	
	void setPopulationForAgeGroupsByLocation(List<RecordGroup> populationGroups, List<String> hierarchyIds);
	
	void setPregnancyOutcomesByLocation(RecordGroup pregnancyGroup, List<String> hierarchyIds);

}
