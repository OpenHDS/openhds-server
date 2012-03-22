package org.openhds.report.service.impl;

import java.util.ArrayList;
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
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.MortalityRecordBean;
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
	
	Calendar startDate;
	Calendar endDate;
		
	@Autowired
	public DemographicRatesController(GenericDao genericDao, SitePropertiesService siteProperties, 
			CalendarUtil calendarUtil, CalculationService calculationService) {
		this.genericDao = genericDao;
		this.siteProperties = siteProperties;
		this.calendarUtil = calendarUtil;
		this.calculationService = calculationService;
	}
	
	@RequestMapping(value = {"/inmigration.report", "/outmigration.report", "/mortality.report", "/fertility.report", "/population.report"})
	public ModelAndView getPopulationRates(HttpServletRequest request) {
		// this is called because entities are modified during this session, but the changes are
		// not intended to be saved back to the database. Without this call, Hibernate will
		// mark the entities are dirty, and attempt to flush them back to the database when
		// later queries are made. Setting this to true essentially tells hibernate to ignore
		// dirty checking
		genericDao.getSession().setDefaultReadOnly(true);
		
		String ratesUuid = request.getParameter("ratesUUID");
		DemRates dm = genericDao.findByProperty(DemRates.class, "uuid", ratesUuid);
		
		startDate = dm.getStartDate();
		endDate = dm.getEndDate();
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
		Calendar midpoint = null;
		
		calculationService.initializeGroups(request.getServletPath());
		
		// denominator
		if (denomType.equals("Population at Midpoint")) {
			midpoint = CalendarUtil.getMidPointDate(startDate, endDate);
			residencies = calculationService.getResidenciesAtMidPoint(midpoint);
			setAgeGroupsForResidenciesAtMidpoint(residencies, midpoint, true);
		}
		else {
			residencies = calculationService.getResidenciesInBetween(startDate, endDate);
			calculationService.setIntervalsOfResidencies(residencies, startDate, endDate);
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
			List<MortalityRecordBean> mortalityRecords = setAgeGroupsForDeaths(deaths);
			
			MortalityRecordBean neoNatal = mortalityRecords.get(0);
			MortalityRecordBean postNatal = mortalityRecords.get(1);
			MortalityRecordBean infant = mortalityRecords.get(2);
			
			modelMap.put("neoNatalMale", neoNatal.getNeoNatalMale());
			modelMap.put("neoNatalFemale", neoNatal.getNeoNatalFemale());
			modelMap.put("neoNatalTotal", neoNatal.getNeoNatalTotal());
			modelMap.put("neoNatalMaleRatio", neoNatal.getNeoNatalMaleRatio());
			modelMap.put("neoNatalFemaleRatio", neoNatal.getNeoNatalFemaleRatio());
			modelMap.put("neoNatalRatioTotal", neoNatal.getNeoNatalRatioTotal());
			
			modelMap.put("postNatalMale", postNatal.getPostNatalMale());
			modelMap.put("postNatalFemale", postNatal.getPostNatalFemale());
			modelMap.put("postNatalTotal", postNatal.getPostNatalTotal());
			modelMap.put("postNatalMaleRatio", postNatal.getPostNatalMaleRatio());
			modelMap.put("postNatalFemaleRatio", postNatal.getPostNatalFemaleRatio());
			modelMap.put("postNatalRatioTotal", postNatal.getPostNatalRatioTotal());
			
			modelMap.put("infantMale", infant.getInfantMale());
			modelMap.put("infantFemale", infant.getInfantFemale());
			modelMap.put("infantTotal", infant.getInfantTotal());
			modelMap.put("infantMaleRatio", infant.getInfantMaleRatio());
			modelMap.put("infantFemaleRatio", infant.getInfantFemaleRatio());
			modelMap.put("infantRatioTotal", infant.getInfantRatioTotal());
			
			modelMap.put("totalOutcomes", postNatal.getTotalOutcomes());
		}
		else if (event.equals("Fertility")) {
			List<PregnancyOutcome> outcomes = calculationService.getPregnanciesBetweenInterval(startDate, endDate);
			setAgeGroupsForPregnancyOutcomes(outcomes);
			// only want intervals 15-49
			calculationService.getReportRecords().remove(1);
			calculationService.getReportRecords().remove(1);
			calculationService.getReportRecords().remove(1);
		}
		else if (event.equals("Population")) {
			if (denomType.equals("Person Days Observed")) {
				calculationService.setNumeratorsForPopulation();
			} else {
				setAgeGroupsForResidenciesAtMidpoint(residencies, midpoint, false);
			}
		}
			
		// call this once denominator and numerator totals have been calculated
		if (denomType.equals("Population at Midpoint")) 
			calculationService.completeReportRecordsForMidpoint(startDate, endDate);
		else
			calculationService.completeReportRecordsForPdo();
		
		if (event.equals("Fertility")) {
			ReportRecordBean allGroup = calculationService.getReportRecords().get(0);
			modelMap.put("totalFertilityRate", allGroup.getTotalFertilityRate());
			
			modelMap.put("maleBorn", allGroup.getNumeratorMale());
			modelMap.put("femaleBorn", allGroup.getNumeratorFemale());
			modelMap.put("bornTotal", allGroup.getNumeratorTotal());
			modelMap.put("eventRateMale", allGroup.getEventRateMale());
			modelMap.put("eventRateFemale", allGroup.getEventRateFemale());
			modelMap.put("eventRateTotal", allGroup.getEventRateTotal());
		}
		else if (event.equals("Population")) {
			ReportRecordBean allGroup = calculationService.getReportRecords().get(0);
			for (int i = 1; i < calculationService.getReportRecords().size(); i++) {
				ReportRecordBean record = calculationService.getReportRecords().get(i);
				record.setDenominatorMale(allGroup.getPdoMale());
				record.setDenominatorFemale(allGroup.getPdoFemale());
			}
		}
		
		List<ReportRecordBean> data = calculationService.getReportRecords();
		modelMap.put("dataSource", data);
				
		String selectedReport = event.toLowerCase().concat("Report");
		return new ModelAndView(selectedReport, modelMap);
	}
	
	public void setAgeGroupsForResidenciesAtMidpoint(List<Residency> residencies, Calendar midpoint, boolean flag) {	
		for (Residency residency : residencies) {		
			Individual individual = residency.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), midpoint);
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, flag);
		}
	}
	
	public void setAgeGroupsForInMigrations(List<InMigration> inmigrations) {
		for (InMigration inmigration : inmigrations) {
			Individual individual = inmigration.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), inmigration.getRecordedDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
		}
	}
	
	public void setAgeGroupsForOutMigrations(List<OutMigration> outmigrations) {
		for (OutMigration outmigration : outmigrations) {
			Individual individual = outmigration.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), outmigration.getRecordedDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
		}
	}
	
	public List<MortalityRecordBean> setAgeGroupsForDeaths(List<Death> deaths) {
		
		List<MortalityRecordBean> mortalityRecords = new ArrayList<MortalityRecordBean>();
		MortalityRecordBean neoNatalRecord = new MortalityRecordBean("Neo-Natal", 0, 0.079);
		MortalityRecordBean postNatalRecord = new MortalityRecordBean("Post-Natal", 0.079, 0.916);
		MortalityRecordBean infantRecord = new MortalityRecordBean("Infant", 0, 1);
		
		for (Death death : deaths) {
			Individual individual = death.getIndividual();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), death.getDeathDate());		
			double age = ((double)days / 365.25);
			calculationService.setAgeGroups(age, individual, false);
			calculationService.setInfantGroups(age, individual, startDate, endDate, neoNatalRecord, postNatalRecord, infantRecord);
		}
		mortalityRecords.add(neoNatalRecord);
		mortalityRecords.add(postNatalRecord);
		mortalityRecords.add(infantRecord);
		return mortalityRecords;
	}
	
	public void setAgeGroupsForPregnancyOutcomes(List<PregnancyOutcome> outcomes) {
		for (PregnancyOutcome outcome : outcomes) {
			Individual individual = outcome.getMother();
			int days = (int) CalendarUtil.daysBetween(individual.getDob(), outcome.getOutcomeDate());		
			long age = (long) (days / 365.25);
			calculationService.setAgeGroupsForBirths(age, individual, outcome);
		}
	}
}
