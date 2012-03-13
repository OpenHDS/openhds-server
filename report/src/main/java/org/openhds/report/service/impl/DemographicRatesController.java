package org.openhds.report.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.OutMigration;
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
	CalculationService calculationService;
	CalendarUtil calendarUtil;
		
	@Autowired
	public DemographicRatesController(GenericDao genericDao, SitePropertiesService siteProperties, 
			CalendarUtil calendarUtil, CalculationService calculationService) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
		this.calendarUtil = calendarUtil;
		this.calculationService = calculationService;
	}
	
	@RequestMapping(value = {"/inmigration.report", "/outmigration.report", "/mortality.report"})
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
		
		calculationService.initializeGroups();
		
		// denominator
		if (denomType.equals("Population at Midpoint")) {
			Calendar midpoint = CalendarUtil.getMidPointDate(startDate, endDate);
			residencies = calculationService.getResidenciesAtMidPoint(midpoint);
			setAgeGroupsForResidenciesAtMidpoint(residencies, midpoint);
		}
		else {
			residencies = calculationService.getResidenciesInBetween(startDate, endDate);
			calculationService.setIntervalsOfResidencies(residencies, startDate, endDate);
			calculationService.setDenominatorTotals();
		}
		
		// numerator
		if (event.equals("InMigration")) {
			List<InMigration> inmigrations = calculationService.getInMigrationsBetweenInterval(startDate, endDate);
			setAgeGroupsForInMigrations(inmigrations);
		}
		else if (event.equals("OutMigration")) {
			List<OutMigration> outmigrations = calculationService.getOutMigrationsBetweenInterval(startDate, endDate);
			setAgeGroupsForOutMigrations(outmigrations);
		}
		else if (event.equals("Mortality")) {
			List<Death> deaths = calculationService.getDeathsBetweenInterval(startDate, endDate);
			setAgeGroupsForDeaths(deaths);
		}
			
		// call this once denominator and numerator totals have been calculated
		calculationService.completeReportRecords(startDate, endDate);
		
		List<ReportRecordBean> data = calculationService.getReportRecords();
		modelMap.put("dataSource", data);
		
		String selectedReport = event.toLowerCase().concat("Report");
		return new ModelAndView(selectedReport, modelMap);
	}
	
	public void setAgeGroupsForResidenciesAtMidpoint(List<Residency> residencies, Calendar midpoint) {	
		for (Residency residency : residencies) {		
			Individual individual = residency.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), midpoint);
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, true);
			calculationService.setDenominatorTotals();
		}
	}
	
	public void setAgeGroupsForInMigrations(List<InMigration> inmigrations) {
		for (InMigration inmigration : inmigrations) {
			Individual individual = inmigration.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), inmigration.getRecordedDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
			calculationService.setNumeratorTotals();
		}
	}
	
	public void setAgeGroupsForOutMigrations(List<OutMigration> outmigrations) {
		for (OutMigration outmigration : outmigrations) {
			Individual individual = outmigration.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), outmigration.getRecordedDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
			calculationService.setNumeratorTotals();
		}
	}
	
	public void setAgeGroupsForDeaths(List<Death> deaths) {
		for (Death death : deaths) {
			Individual individual = death.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), death.getDeathDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
			calculationService.setNumeratorTotals();
		}
	}
}
