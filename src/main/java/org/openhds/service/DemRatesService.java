package org.openhds.service;

import org.openhds.annotations.Authorized;
import org.openhds.domain.DemRates;
import org.openhds.domain.PrivilegeConstants;

public interface DemRatesService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	DemRates findDemRateByUuid(String itemId);
}
