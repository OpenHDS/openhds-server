package org.openhds.report.service;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;


public interface CalculationService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setAgeGroups(long age, Individual individual); 
}
