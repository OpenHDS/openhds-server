package org.openhds.report.service.impl;

import org.openhds.dao.service.GenericDao;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.report.service.DemographicRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DemographicRatesServiceImpl implements DemographicRatesService {
	
	GenericDao genericDao;
	SitePropertiesService siteProperties;
	
	@Autowired
	public DemographicRatesServiceImpl(GenericDao genericDao, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
	}
	

}
