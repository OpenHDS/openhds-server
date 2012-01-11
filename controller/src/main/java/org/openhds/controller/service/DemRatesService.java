package org.openhds.controller.service;

import java.util.Calendar;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.PrivilegeConstants;

public interface DemRatesService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	DemRates findDemRateByUuid(String itemId);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	Calendar getMidPointDate(Calendar startDate, Calendar endDate);

}
