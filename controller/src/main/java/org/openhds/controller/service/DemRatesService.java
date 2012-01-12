package org.openhds.controller.service;

import java.util.Calendar;
import java.util.List;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Residency;

public interface DemRatesService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	DemRates findDemRateByUuid(String itemId);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	Calendar getMidPointDate(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Residency> getResidenciesAtMidPoint(Calendar midpoint);

}
