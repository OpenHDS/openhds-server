package org.openhds.service.impl;

import org.openhds.service.DemRatesService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.DemRates;

public class DemRatesServiceImpl implements DemRatesService {

	private GenericDao genericDao;
	
	public DemRatesServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public DemRates findDemRateByUuid(String itemId) {
		return genericDao.findByProperty(DemRates.class, "uuid", itemId);
	}	
}
