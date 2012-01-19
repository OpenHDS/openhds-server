package org.openhds.report.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openhds.controller.service.DemRatesService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Residency;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.ReportRecordBean;
import org.openhds.report.service.CalculationService;
import org.openhds.report.service.DemographicRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemographicRatesController implements DemographicRatesService {
	
	GenericDao genericDao;
	SitePropertiesService siteProperties;
	DemRatesService demRatesService;
	CalculationService calculationService;
	CalendarUtil calendarUtil;
		
	@Autowired
	public DemographicRatesController(GenericDao genericDao, SitePropertiesService siteProperties, 
			CalendarUtil calendarUtil, DemRatesService demRatesService, CalculationService calculationService) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
		this.calendarUtil = calendarUtil;
		this.demRatesService = demRatesService;
		this.calculationService = calculationService;
	}
	
	@RequestMapping(value = { "/inmigration.report", "/population.report", "/mortality.report", 
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
		modelMap.put("event", event);
		modelMap.put("denomType", denomType);
		modelMap.put("firstDate", calendarUtil.formatDate(startDate));
		modelMap.put("lastDate", calendarUtil.formatDate(endDate));
		modelMap.put("locations", "All Locations");
		modelMap.put("individuals", "All Individuals");
			
		List<Residency> residencies = null;
		
		// denominator
		if (denomType.equals("Population at Midpoint")) {
			Calendar midpoint = demRatesService.getMidPointDate(startDate, endDate);
			residencies = demRatesService.getResidenciesAtMidPoint(midpoint);
			setAgeGroupsForResidenciesAtMidpoint(residencies, midpoint);
		}
		
		// numerator
		if (event.equals("InMigration")) {
			List<InMigration> inmigrations = demRatesService.getInMigrationsBetweenInterval(startDate, endDate);
			setAgeGroupsForInMigrations(inmigrations);
		}
				
		List<ReportRecordBean> data = calculationService.getReportRecords();
		modelMap.put("dataSource", data);
		
		String selectedReport = event.toLowerCase().concat("Report");
		return new ModelAndView(selectedReport, modelMap);
	}
	
	public void setAgeGroupsForResidenciesAtMidpoint(List<Residency> residencies, Calendar midpoint) {	
		for (Residency residency : residencies) {		
			Individual individual = residency.getIndividual();
			long age = (long) (demRatesService.daysBetween(individual.getDob(), midpoint) / 365.25);
			calculationService.setAgeGroups(age, individual, true);
			calculationService.setDenominatorTotals();
		}
	}
	
	public void setAgeGroupsForInMigrations(List<InMigration> inmigrations) {
		for (InMigration inmigration : inmigrations) {
			Individual individual = inmigration.getIndividual();
			long age = (long) (demRatesService.daysBetween(individual.getDob(), inmigration.getRecordedDate()) / 365.25);
			calculationService.setAgeGroups(age, individual, false);
			calculationService.setNumeratorTotals();
		}
	}
}
