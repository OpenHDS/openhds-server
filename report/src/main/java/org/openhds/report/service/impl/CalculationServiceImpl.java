package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.openhds.controller.service.DemRatesService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.report.beans.ReportRecordBean;
import org.openhds.report.service.CalculationService;

public class CalculationServiceImpl implements CalculationService {

	/**
	 * Age Groups are as follows:
	 * 
	 * ageGrp1:  0-4
	 * ageGrp2:  5-9
	 * ageGrp3:  10-14
	 * ageGrp4:  15-19
	 * ageGrp5:  20-24
	 * ageGrp6: 25-29
	 * ageGrp7: 30-34
	 * ageGrp8: 35-39
	 * ageGrp9: 40-44
	 * ageGrp10: 45-49
	 * ageGrp11: 50-54
	 * ageGrp12: 55-59
	 * ageGrp13: 60-64
	 * ageGrp14: 65+
	 */
	List<ReportRecordBean> reportRecords = new ArrayList<ReportRecordBean>();
	
	SitePropertiesService siteProperties;
	DemRatesService demRatesService;
	GenericDao genericDao;
	
	public CalculationServiceImpl(SitePropertiesService siteProperties, 
			DemRatesService demRatesService, GenericDao genericDao) {
		this.siteProperties = siteProperties;
		this.demRatesService = demRatesService;
		this.genericDao = genericDao;
		initializeGroups();
	}
	
	private void initializeGroups() {
		reportRecords.add(new ReportRecordBean("ALL", 0, 100));
		reportRecords.add(new ReportRecordBean("0-4", 0, 5));
		reportRecords.add(new ReportRecordBean("5-9", 5, 10));
		reportRecords.add(new ReportRecordBean("10-14", 10, 15));
		reportRecords.add(new ReportRecordBean("15-19", 15, 20));
		reportRecords.add(new ReportRecordBean("20-24", 20, 25));
		reportRecords.add(new ReportRecordBean("25-29", 25, 30));
		reportRecords.add(new ReportRecordBean("30-34", 30, 35));
		reportRecords.add(new ReportRecordBean("35-39", 35, 40));
		reportRecords.add(new ReportRecordBean("40-44", 40, 45));
		reportRecords.add(new ReportRecordBean("45-49", 45, 50));
		reportRecords.add(new ReportRecordBean("50-54", 50, 55));
		reportRecords.add(new ReportRecordBean("55-59", 55, 60));
		reportRecords.add(new ReportRecordBean("60-64", 60, 65));
		reportRecords.add(new ReportRecordBean("65+", 65, 100));
		
	}
	
	public void completeReportRecords(Calendar startDate, Calendar endDate) {		
		// calculate pdo's
		int daysBetween = (int) daysBetween(startDate, endDate);
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			record.setPdoMale(record.getNumeratorMale() * daysBetween);
			record.setPdoFemale(record.getNumeratorFemale() * daysBetween);
			record.setPdoTotal();
			record.calculatePyoMaleTotal();
			record.calculatePyoFemaleTotal();
			record.setPyoTotal();
		}
		
		// calculate all
		ReportRecordBean ageGrpAll = reportRecords.get(0);
		for (int i = 1; i < reportRecords.size(); i++) {
			ageGrpAll.addNumeratorMale(reportRecords.get(i).getNumeratorMale());
			ageGrpAll.addNumeratorFemale(reportRecords.get(i).getNumeratorFemale());
			ageGrpAll.addDenominatorMale(reportRecords.get(i).getDenominatorMale());
			ageGrpAll.addDenominatorFemale(reportRecords.get(i).getDenominatorFemale());
		}
		ageGrpAll.setNumeratorTotal(ageGrpAll.getNumeratorMale() + ageGrpAll.getNumeratorFemale());
		ageGrpAll.setDenominatorTotal(ageGrpAll.getDenominatorMale()+ ageGrpAll.getDenominatorFemale());	
		ageGrpAll.setPdoMale(ageGrpAll.getDenominatorMale() * daysBetween);
		ageGrpAll.setPdoFemale(ageGrpAll.getDenominatorFemale() * daysBetween);
		ageGrpAll.setPdoTotal();
		ageGrpAll.calculatePyoMaleTotal();
		ageGrpAll.calculatePyoFemaleTotal();
		ageGrpAll.setPyoTotal();
	}
		
	public void setDenominatorTotals() {	
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean report = reportRecords.get(i);
			report.setDenominatorTotal(report.getDenominatorMale() + report.getDenominatorFemale());
		}
	}
	
	public void setNumeratorTotals() {
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean report = reportRecords.get(i);
			report.setNumeratorTotal(report.getNumeratorMale() + report.getNumeratorFemale());
		}
	}
	
	public List<Residency> getResidenciesAtMidPoint(Calendar midpoint) {
		
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> midPointResidencies = new ArrayList<Residency>();
			
		Calendar resEndDate = null;
		for (Residency res : residencies) {
			// handle residencies that haven't ended, if null then use the date of the last visit
			if (res.getEndDate() == null) {
	
				// get all visits for this residency
				List<Visit> visits = genericDao.findListByProperty(Visit.class, "visitLocation", res.getLocation());
				
				if (visits.size() > 0) {
					// grab latest visit
					Calendar latestDate = new GregorianCalendar(1900, Calendar.JANUARY, 1);
					for (Visit visit : visits) {
						Calendar visitDate = visit.getVisitDate();
						if (visitDate.after(latestDate))
							latestDate = visitDate;
					}		
					resEndDate = latestDate;
					res.setEndDate(resEndDate);
				}
			}
			else 
				resEndDate = res.getEndDate();

			if (resEndDate != null) {
				// for residency to be added to analysis set, 
				// its end date has to be on or after the midpoint
				// and its start date has to be on or before the midpoint
				if ((resEndDate.after(midpoint) || resEndDate.equals(midpoint)) && 
					((res.getStartDate().before(midpoint) || res.getStartDate().equals(midpoint)))) {
					midPointResidencies.add(res);
				}
			}
			resEndDate = null;
		}
		return midPointResidencies;
	}
	
	public List<Residency> getResidenciesInBetween(Calendar startDate, Calendar endDate) {
		
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> validResidencies = new ArrayList<Residency>();

		Calendar resEndDate = null;
		for (Residency res : residencies) {
			
			// handle residencies that haven't ended, if null then use the date of the last visit
			if (res.getEndDate() == null) {
				
				// get all visits for this residency
				List<Visit> visits = genericDao.findListByProperty(Visit.class, "visitLocation", res.getLocation());
				
				if (visits.size() > 0) {
					// grab latest visit
					Calendar latestDate = new GregorianCalendar(1900, Calendar.JANUARY, 1);
					for (Visit visit : visits) {
						Calendar visitDate = visit.getVisitDate();
						if (visitDate.after(latestDate))
							latestDate = visitDate;
					}		
					resEndDate = latestDate;
					res.setEndDate(resEndDate);
				}
			}
			else 
				resEndDate = res.getEndDate();
			
			// for residency to be added to analysis set, 
			// its start date has to be on or before the end date
			// and its start date has to be on or before the midpoint
			if ((res.getStartDate().before(endDate) || res.getStartDate().equals(endDate)) && 
					(resEndDate == null || res.getEndDate().after(startDate) || res.getEndDate().equals(startDate))) {
				validResidencies.add(res);
			}
		}
		return validResidencies;
	}
	
	public void setIntervalsOfResidencies(List<Residency> list, Calendar startDate, Calendar endDate) {
				
		for (Residency res : list) {
			Calendar beginInterval = null;
			Calendar endInterval = null;
			
			// determine intervals
			if (res.getStartDate().after(startDate)) 
				beginInterval = res.getStartDate();
			else
				beginInterval = startDate;
			if (res.getEndDate().before(endDate))
				endInterval = res.getEndDate();
			else
				endInterval = endDate;	
			
			// determine age groups at beginning and end of residency
			int ageAtBeg = (int) (daysBetween(beginInterval, res.getIndividual().getDob()) / 365.25);
			int ageAtEnd = (int) (daysBetween(endInterval, res.getIndividual().getDob()) / 365.25);
			int firstGroup = determineAgeGroup(ageAtBeg);
			int lastGroup = determineAgeGroup(ageAtEnd);
			
			int currentGroup = firstGroup;
			
			if ((endInterval.after(beginInterval) || endInterval.equals(beginInterval)) && 
					(firstGroup == lastGroup)) {
				
				ReportRecordBean group = reportRecords.get(currentGroup);
				if (res.getIndividual().getGender().equals(siteProperties.getMaleCode())) 
					group.setDenominatorMale(daysBetween(beginInterval, endInterval));
				else 
					group.setDenominatorFemale(daysBetween(beginInterval, endInterval));
				
				daysBetween(beginInterval, endInterval);
			}
			// determine where to split the residencies
			else {
				
				do {				
					ReportRecordBean group = reportRecords.get(currentGroup);
					int difference = group.getMax() - ageAtBeg;
					int adjustedAge = ageAtBeg + difference;
					Calendar dob = res.getIndividual().getDob();
					
					Calendar groupEndDate = dob;
					groupEndDate.add(Calendar.YEAR, adjustedAge);
					groupEndDate.add(Calendar.DAY_OF_MONTH, -1); 
					
					if (res.getIndividual().getGender().equals(siteProperties.getMaleCode())) 
						group.setDenominatorMale(daysBetween(beginInterval, groupEndDate));
					else 
						group.setDenominatorFemale(daysBetween(beginInterval, groupEndDate));

					beginInterval = groupEndDate;
					currentGroup++;

				} while (currentGroup <= lastGroup);
			}
		}
	}
	
	public List<InMigration> getInMigrationsBetweenInterval(Calendar startDate, Calendar endDate) {
		
		List<InMigration> inmigrations = genericDao.findAll(InMigration.class, true);
		List<InMigration> inmigrationsInInterval = new ArrayList<InMigration>();
		
		for (InMigration inmig : inmigrations) {		
			if (inmig.getRecordedDate().after(startDate) &&
				inmig.getRecordedDate().before(endDate)) {
				inmigrationsInInterval.add(inmig);
			}
		}
		return inmigrationsInInterval;
	}
	
	public Calendar getMidPointDate(Calendar startDate, Calendar endDate) {
		int daysBtw = (int)daysBetween(startDate, endDate);
		Calendar midPoint = (Calendar)startDate.clone();
		midPoint.add(Calendar.DATE, (int) (daysBtw * 0.5));
		return midPoint;
	}
	
	public int daysBetween(Calendar startDate, Calendar endDate) {  
		Calendar date = (Calendar) startDate.clone();  
		int daysBetween = 0;  
		while (date.before(endDate)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
		    daysBetween++;  
		}  
		return daysBetween;  
	} 
		
	/**
	 * Returns the index in which age report record the age belongs.
	 * Corresponds to the CalculationService reportRecord
	 */
	public int determineAgeGroup(int age) {
		if (age >= 0 && age < 5)
			return 1;
		else if (age >= 5 && age < 10)
			return 2;
		else if (age >= 10 && age < 15)
			return 3;
		else if (age >= 15 && age < 20)
			return 4;
		else if (age >= 20 && age < 25)
			return 5;
		else if (age >= 25 && age < 30)
			return 6;
		else if (age >= 30 && age < 35)
			return 7;
		else if (age >= 35 && age < 40)
			return 8;
		else if (age >= 40 && age < 45)
			return 9;
		else if (age >= 45 && age < 50)
			return 10;
		else if (age >= 50 && age < 55)
			return 11;
		else if (age >= 55 && age < 60)
			return 12;
		else if (age >= 60 && age < 65)
			return 13;
		else
			return 14;
	}
		
	public void setAgeGroups(long age, Individual individual, boolean denominator) {
		if (age >= 0 && age < 5) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(1).addDenominatorMale();
				else
					reportRecords.get(1).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(1).addDenominatorFemale();
				else
					reportRecords.get(1).addNumeratorFemale();
			}
		}
		else if (age >= 5 && age < 10) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(2).addDenominatorMale();
				else
					reportRecords.get(2).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(2).addDenominatorFemale();
				else
					reportRecords.get(2).addNumeratorFemale();
			}
		}
		else if (age >= 10 && age < 15) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(3).addDenominatorMale();
				else
					reportRecords.get(3).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(3).addDenominatorFemale();
				else
					reportRecords.get(3).addNumeratorFemale();
			}
		}
		else if (age >= 15 && age < 20) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(4).addDenominatorMale();
				else
					reportRecords.get(4).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(4).addDenominatorFemale();
				else
					reportRecords.get(4).addNumeratorFemale();
			}
		}
		else if (age >= 20 && age < 25) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(5).addDenominatorMale();
				else
					reportRecords.get(5).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(5).addDenominatorFemale();
				else
					reportRecords.get(5).addNumeratorFemale();
			}
		}
		else if (age >= 25 && age < 30) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(6).addDenominatorMale();
				else
					reportRecords.get(6).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(6).addDenominatorFemale();
				else
					reportRecords.get(6).addNumeratorFemale();
			}
		}
		else if (age >= 30 && age < 35) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(7).addDenominatorMale();
				else
					reportRecords.get(7).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(7).addDenominatorFemale();
				else
					reportRecords.get(7).addNumeratorFemale();
			}
		}
		else if (age >= 35 && age < 40) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(8).addDenominatorMale();
				else
					reportRecords.get(8).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(8).addDenominatorFemale();
				else
					reportRecords.get(8).addNumeratorFemale();
			}
		}
		else if (age >= 40 && age < 45) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(9).addDenominatorMale();
				else
					reportRecords.get(9).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(9).addDenominatorFemale();
				else
					reportRecords.get(9).addNumeratorFemale();
			}
		}
		else if (age >= 45 && age < 50) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(10).addDenominatorMale();
				else
					reportRecords.get(10).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(10).addDenominatorFemale();
				else
					reportRecords.get(10).addNumeratorFemale();
			}
		}
		else if (age >= 50 && age < 55) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(11).addDenominatorMale();
				else
					reportRecords.get(11).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(11).addDenominatorFemale();
				else
					reportRecords.get(11).addNumeratorFemale();
			}
		}
		else if (age >= 55 && age < 60) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(12).addDenominatorMale();
				else
					reportRecords.get(12).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(12).addDenominatorFemale();
				else
					reportRecords.get(12).addNumeratorFemale();
			}
		}
		else if (age >= 60 && age < 65) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(13).addDenominatorMale();
				else
					reportRecords.get(13).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(13).addDenominatorFemale();
				else
					reportRecords.get(13).addNumeratorFemale();
			}
		}
		else if (age >= 65) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					reportRecords.get(14).addDenominatorMale();
				else
					reportRecords.get(14).addNumeratorMale();
			}
			else {
				if (denominator)
					reportRecords.get(14).addDenominatorFemale();
				else
					reportRecords.get(14).addNumeratorFemale();
			}
		}
	}
	
	public List<ReportRecordBean> getReportRecords() {
		return reportRecords;
	}
}
