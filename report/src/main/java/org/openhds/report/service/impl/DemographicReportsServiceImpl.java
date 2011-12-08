package org.openhds.report.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.openhds.controller.util.DateUtil;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Residency;
import org.openhds.report.beans.ReportRecordBean;
import org.openhds.report.service.DemographicReportsService;
import org.openhds.report.util.ReportMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemographicReportsServiceImpl implements DemographicReportsService {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	GenericDao genericDao;
	
	// report parameters put into the map
	String reportFormat = "xls";  // xls, pdf, html, csv
	Boolean isMidPoint; // false means the entire duration (person days) is used
	Calendar firstDate;
	Calendar lastDate;
	String denomTypeString;
	String eventType;
	
	@Autowired
	public DemographicReportsServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	// main handler method
	@RequestMapping(value="/demographic.report") //TODO: change to rates
	public ModelAndView getPopulationRates(HttpServletRequest request) {

		String ratesUuid = request.getParameter("ratesUUID");
		// Get the DemRates object
		DemRates dm = genericDao.findByProperty(DemRates.class, "uuid", ratesUuid);
		// Populate report with firstdate and lastdate from dm DemRates object 
		firstDate = dm.getFirstDate();
		lastDate = dm.getLastDate();
		denomTypeString = dm.getSelectedDenomType();
		isMidPoint = new Boolean(dm.isMidPointType());
		eventType = dm.getSelectedEventRate();
		
		/*  by default, Jasper Spring looks for the "dataSource" parameter
 			in the absence of that or where you do not pass in the collection directly instead
			of the map, you have to specify reportDataKey in the views.properties
			it is not necessary to name the datasource as 'dataSource' as long as getReportRecords() 
			returns an instance of the following data type:
			JRDataSource.class, JRDataSourceProvider.class, Collection.class, Object[].class
		 */	
		
		Map<String,Object> modelMap = new HashMap<String,Object>();
		modelMap.put("populationReportRecords", getReportRecords());
		modelMap.put("format", dm.getSelectedOutputType().toLowerCase());
		modelMap.put("event", eventType);
		modelMap.put("denomType", denomTypeString);//returns denom. type string "Midpoint Method" or "Person-days Observed";
		modelMap.put("firstDate", (formatDate(firstDate)));// firstDate in specified format
		modelMap.put("lastDate", (formatDate(lastDate))); // lastDate in specified format
		modelMap.put("location", dm.getSelectedLocationsName()); // get location selection
		modelMap.put("individual", dm.getSelectedIndividualsName()); // get individual selection
		
		return new ModelAndView("populationReport", modelMap);
	}
	
	@RequestMapping(value="/marital.report") //TODO: change to rates
	public ModelAndView getSimpleRates() {
		Map<String,Object> modelMap = new HashMap<String,Object>();
	
		
		modelMap.put("populationReportRecords", getReportRecords());
		modelMap.put("format", getReportFormat());
		//	modelMap.put("method", dm.getSelectedDenomType());
		modelMap.put("firstDate", (formatDate(firstDate))  );
		modelMap.put("lastDate", (formatDate(lastDate))  );
		
		Map<String,Object> simpleParamMap = new HashMap<String,Object>();
		
		//simpleParamMap.put("Married", value); //set number of people married
		
		for (Map.Entry<String, Object> entry : simpleParamMap.entrySet()){
			   String key = entry.getKey();
			   String value = (String) entry.getValue();
			   //modelMap.put(key, value);
			}
		modelMap.putAll(simpleParamMap);
		return new ModelAndView("simpleReport", simpleParamMap);
	}	
	

	private Collection<ReportRecordBean> getReportRecords() {
		
		// get a set of individuals from the list of residencies
		Set<Individual> allInd = getIndividualsFromResidency(getResidencyList(firstDate,lastDate));
		
		ReportMaker rm = new ReportMaker();
		Collection<ReportRecordBean> beans = rm.makeReport(allInd, isMidPoint, firstDate, lastDate, eventType);
		return beans;
	}

	public Set<Individual> getIndividualsFromResidency(List<Residency> residencyList){
		Set<Individual> individs = new HashSet<Individual>();
		
		for(Residency resid : residencyList) {
			individs.add(resid.getIndividual());
		}
		return individs;
	}
	
	private String formatDate(Calendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(calendar.getTime());
	}
	
	public List<Residency> getResidencyList(Calendar start, Calendar end){
		List<Residency> newList = new ArrayList<Residency>();
		System.out.println("start is: " + formatDate(start));
		System.out.println("end is: " + formatDate(end));
		if (isMidPoint){
			// if using midpoint method, get residencies at the midpoint date
			newList = getResidenciesAtMidPoint(start, end);
		}else{
			//use residencies from the period
			newList = getResidenciesForPeriod(start, end);			
		}
		return newList;
	}
	
	public List<Residency> getResidenciesForPeriod(Calendar start, Calendar end){

		// get all residencies
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> periodResidencies = new ArrayList<Residency>();
		
		//Calendar today = Calendar.getInstance();
		Calendar resEndDate;
		
		for (Residency res : residencies){
			// handle residencies that haven't ended
			if (res.getEndDate() == null){
				resEndDate = Calendar.getInstance();
			} else resEndDate = (Calendar)res.getEndDate().clone();

			// for residency to be added to analysis set, its end date has to be on or after the midpoint 
			// NEVER endDate before midpoint...!endDate.before(midpoint)			
			if (  !(resEndDate.before(start))  &&  (!(res.getStartDate().after(resEndDate)))){
				
				// add residencies that qualify into the list
				periodResidencies.add(res);
			}
			resEndDate = null;
		}
		return periodResidencies;
	}	

	// select Location
	// var selectLoc = " AND EXISTS (SELECT residency.uuid FROM residency WHERE  residency.location_uuid IN (SELECT location.uuid FROM location WHERE location.extid IN ( "

	// midpoint population calculation	
	public List<Residency> getResidenciesAtMidPoint(Calendar start, Calendar end){

		// get all residencies
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> midPointResidencies = new ArrayList<Residency>();
		
		System.out.println("start is: " + formatDate(start));
		System.out.println("end is: " + formatDate(end));
		
		DateUtil dateUtil = new DateUtil();
		
		// get the midpoint between the dates of analyses
		Calendar midpoint = dateUtil.midPointDate(start, end);
		System.out.println("start is: " + formatDate(start));
		System.out.println("end is: " + formatDate(end));
		System.out.println("midpoint is: " + formatDate(midpoint));
		Calendar resEndDate;
		
		for (Residency res : residencies){
			
			// handle residencies that haven't ended
			if (res.getEndDate() == null){
				resEndDate = Calendar.getInstance();
			} else resEndDate = (Calendar)res.getEndDate().clone();
			
			// for residency to be added to analysis set, its end date has to be on or after the midpoint 
			// NEVER endDate before midpoint...!endDate.before(midpoint)			
			if (  !(resEndDate.before(midpoint))  &&  (!(res.getStartDate().after(midpoint)))){
				System.out.println(formatDate(midpoint));
				// add residencies that qualify into the list
				midPointResidencies.add(res);
			}
			resEndDate = null;
		}
		return midPointResidencies;
	}

	// the period is specified thus
	// interval runs from minAge to less than maxAge
	public Set<Individual> getIndGroup(Set<Individual> ind, double minAgeInDays, double maxAgeInDays){
		Set<Individual> newSet = new HashSet<Individual>();
		DateUtil dateUtil = new DateUtil();
		for (Individual individual : ind) {
			double currentAgeInDays = dateUtil.daysBetween(individual.getDob(), Calendar.getInstance());
			// e,g period is 1 - 4.999999999
			if( maxAgeInDays > currentAgeInDays && !(currentAgeInDays < minAgeInDays)){
				newSet.add(individual);
			}
		}
		return newSet;
	}

	public boolean midPointIsDenominatorType(String denomInstance){
		boolean test = false;		
		if(denomInstance.toString() == "Population at Mid-Point"){
			test = true;
		}		
		return test;
	}
	
	public Calendar getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Calendar firstDate) {
		this.firstDate = firstDate;
	}

	public Calendar getLastDate() {
		return lastDate;
	}

	public void setLastDate(Calendar lastDate) {
		this.lastDate = lastDate;
	}

	public String getReportFormat() {
		return reportFormat;
	}

	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}

	public Boolean getIsMidPoint() {
		return isMidPoint;
	}

	public void setIsMidPoint(Boolean isMidPoint) {
		this.isMidPoint = isMidPoint;
	}
}