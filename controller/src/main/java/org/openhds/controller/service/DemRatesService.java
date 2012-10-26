package org.openhds.controller.service;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.PrivilegeConstants;

public interface DemRatesService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	DemRates findDemRateByUuid(String itemId);
}
