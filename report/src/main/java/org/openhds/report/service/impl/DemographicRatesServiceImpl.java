package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.service.DemographicRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemographicRatesServiceImpl implements DemographicRatesService {
	
	GenericDao genericDao;
	SitePropertiesService siteProperties;
	CalendarUtil calendarUtil;
	
	@Autowired
	public DemographicRatesServiceImpl(GenericDao genericDao, SitePropertiesService siteProperties, CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
		this.calendarUtil = calendarUtil;
	}
	
	@RequestMapping(value = { "/inmigration.report", "/population.report", "/mortality.report", "/inmigration.report", 
			"/outmigration.report", "/marital.report", "/childmortalityratios.report", "/fertility.report"})
	public ModelAndView getPopulationRates(HttpServletRequest request) {
		
		String ratesUuid = request.getParameter("ratesUUID");
		DemRates dm = genericDao.findByProperty(DemRates.class, "uuid", ratesUuid);
		
		Calendar startDate = dm.getStartDate();
		Calendar endDate = dm.getEndDate();
		String denomType = dm.getDenominator();
		String event = dm.getEvent();
		
		/*
		 * by default, Jasper Spring looks for the "dataSource" parameter in the
		 * absence of that or where you do not pass in the collection directly
		 * instead of the map, you have to specify reportDataKey in the
		 * views.properties it is not necessary to name the datasource as
		 * 'dataSource' as long as getReportRecords() returns an instance of the
		 * following data type: JRDataSource.class, JRDataSourceProvider.class,
		 * Collection.class, Object[].class
		 */
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("format", "pdf"); // xls, pdf, html, csv
		modelMap.put("event", event);
		modelMap.put("denomType", denomType);
		modelMap.put("firstDate", calendarUtil.formatDate(startDate));
		modelMap.put("lastDate", calendarUtil.formatDate(endDate));
		modelMap.put("locations", "All Locations");
		modelMap.put("individuals", "All Individuals");
				
		//List data = new ArrayList<ReportRecord>(getReportRecords(isMidPoint, firstDate, lastDate, eventType));
		
		
		
		return null;
	}
	

}
