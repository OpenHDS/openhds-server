package org.openhds.controller.service.impl;

import org.openhds.controller.service.DemRatesService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;

public class DemRatesServiceImpl implements DemRatesService {

	private GenericDao genericDao;
	
	public DemRatesServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public DemRates findDemRateByUuid(String itemId) {
		return genericDao.findByProperty(DemRates.class, "uuid", itemId);
	}
}
