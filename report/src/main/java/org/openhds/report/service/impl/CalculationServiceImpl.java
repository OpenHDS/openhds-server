package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.openhds.controller.service.PregnancyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.MortalityRecordBean;
import org.openhds.report.beans.ReportRecordBean;
import org.openhds.report.service.CalculationService;

public class CalculationServiceImpl implements CalculationService {

	List<ReportRecordBean> reportRecords = new ArrayList<ReportRecordBean>();
	
	SitePropertiesService siteProperties;
	GenericDao genericDao;
	PregnancyService pregnancyService;
	
	public CalculationServiceImpl(SitePropertiesService siteProperties, GenericDao genericDao,
			PregnancyService pregnancyService) {
		this.siteProperties = siteProperties;
		this.genericDao = genericDao;
		this.pregnancyService = pregnancyService;
	}
	
	public void initializeGroups(String path) {
		reportRecords.clear();
		
		if (path.equals("/outmigration.report") || path.equals("/inmigration.report")) {
			reportRecords.add(new ReportRecordBean("ALL", 0, 110));
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
		else if (path.equals("/mortality.report")) {
			reportRecords.add(new ReportRecordBean("ALL", 0, 110));
			reportRecords.add(new ReportRecordBean("< 12 months", 0, 1));
			reportRecords.add(new ReportRecordBean("1-4", 1, 5));
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
			reportRecords.add(new ReportRecordBean("65-69", 65, 70));
			reportRecords.add(new ReportRecordBean("70-74", 70, 75));
			reportRecords.add(new ReportRecordBean("75-79", 75, 80));
			reportRecords.add(new ReportRecordBean("80-84", 80, 85));
			reportRecords.add(new ReportRecordBean("85+", 85, 100));
		}
		else if (path.equals("/fertility.report")) {
			reportRecords.add(new ReportRecordBean("ALL", 15, 50));
			//ignored
			reportRecords.add(new ReportRecordBean("0-4", 0, 5));
			//ignored
			reportRecords.add(new ReportRecordBean("5-9", 5, 10));
			//ignored
			reportRecords.add(new ReportRecordBean("10-14", 10, 15));
			reportRecords.add(new ReportRecordBean("15-19", 15, 20));
			reportRecords.add(new ReportRecordBean("20-24", 20, 25));
			reportRecords.add(new ReportRecordBean("24-29", 25, 30));
			reportRecords.add(new ReportRecordBean("30-34", 30, 35));
			reportRecords.add(new ReportRecordBean("35-39", 35, 40));
			reportRecords.add(new ReportRecordBean("40-44", 40, 45));
			reportRecords.add(new ReportRecordBean("45-49", 45, 50));
		}
		else if (path.equals("/population.report")) {
			reportRecords.add(new ReportRecordBean("ALL", 0, 110));
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
			reportRecords.add(new ReportRecordBean("65-69", 65, 70));
			reportRecords.add(new ReportRecordBean("70-74", 70, 75));
			reportRecords.add(new ReportRecordBean("75-79", 75, 80));
			reportRecords.add(new ReportRecordBean("80-84", 80, 85));
			reportRecords.add(new ReportRecordBean("85-89", 85, 90));
			reportRecords.add(new ReportRecordBean("90-95", 90, 95));
			reportRecords.add(new ReportRecordBean("95+", 95, 110));
		}
	}
	
	public void completeReportRecordsForMidpoint(Calendar startDate, Calendar endDate) {	
		
		int daysBetween = (int) CalendarUtil.daysBetween(startDate, endDate);
		
		// calculate all
		ReportRecordBean ageGrpAll = reportRecords.get(0);
		for (int i = 1; i < reportRecords.size(); i++) {
			ageGrpAll.addNumeratorMale(reportRecords.get(i).getNumeratorMale());
			ageGrpAll.addNumeratorFemale(reportRecords.get(i).getNumeratorFemale());
			ageGrpAll.addDenominatorMale(reportRecords.get(i).getDenominatorMale());
			ageGrpAll.addDenominatorFemale(reportRecords.get(i).getDenominatorFemale());
		}
		ageGrpAll.setPdoMale(ageGrpAll.getDenominatorMale() * daysBetween);
		ageGrpAll.setPdoFemale(ageGrpAll.getDenominatorFemale() * daysBetween);
		ageGrpAll.setTotal(ageGrpAll.getNumeratorTotal());
		
		// calculate pdo's
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			record.setPdoMale(record.getDenominatorMale() * daysBetween);
			record.setPdoFemale(record.getDenominatorFemale() * daysBetween);
			record.setTotal(ageGrpAll.getNumeratorTotal());
		}
	}
	
	public void completeReportRecordsForPdo() {		
		// calculate all
		ReportRecordBean ageGrpAll = reportRecords.get(0);
		for (int i = 1; i < reportRecords.size(); i++) {
			ageGrpAll.addNumeratorMale(reportRecords.get(i).getNumeratorMale());
			ageGrpAll.addNumeratorFemale(reportRecords.get(i).getNumeratorFemale());
			ageGrpAll.addPdoMale(reportRecords.get(i).getPdoMale());
			ageGrpAll.addPdoFemale(reportRecords.get(i).getPdoFemale());
		}
		
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			record.setTotal(ageGrpAll.getNumeratorTotal());
		}
		ageGrpAll.setTotal(ageGrpAll.getNumeratorTotal());
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
			int ageAtBeg = (int) (CalendarUtil.daysBetween(res.getIndividual().getDob(), beginInterval) / 365.25);
			int ageAtEnd = (int) (CalendarUtil.daysBetween(res.getIndividual().getDob(), endInterval) / 365.25);
			int firstGroup = determineAgeGroup(ageAtBeg);
			int lastGroup = determineAgeGroup(ageAtEnd);
			
			int currentGroup = firstGroup;
			
			if (firstGroup == lastGroup) {
				
				ReportRecordBean group = reportRecords.get(currentGroup);
				if (res.getIndividual().getGender().equals(siteProperties.getMaleCode())) 
					group.addPdoMale((double)CalendarUtil.daysBetween(beginInterval, endInterval));
				else 
					group.addPdoFemale((double)CalendarUtil.daysBetween(beginInterval, endInterval));
			}
			// determine where to split the residencies
			else {
				
				do {				
					ReportRecordBean group = reportRecords.get(currentGroup);
					int difference = (int) (group.getMax() - ageAtBeg);
					int adjustedAge = ageAtBeg + difference;
					Calendar dob = res.getIndividual().getDob();
					
					Calendar groupEndDate = (Calendar) dob.clone();
					groupEndDate.add(Calendar.YEAR, adjustedAge);
					groupEndDate.add(Calendar.DAY_OF_MONTH, -1);
					
					if (groupEndDate.after(endInterval))
						groupEndDate = endInterval;
					
					if (res.getIndividual().getGender().equals(siteProperties.getMaleCode())) 
						group.addPdoMale((int)CalendarUtil.daysBetween(beginInterval, groupEndDate));
					else 
						group.addPdoFemale((int)CalendarUtil.daysBetween(beginInterval, groupEndDate));
					
					groupEndDate.add(Calendar.DAY_OF_MONTH, 1);
					beginInterval = (Calendar) groupEndDate.clone();
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
	
	public List<OutMigration> getOutMigrationsBetweenInterval(Calendar startDate, Calendar endDate) {
		
		List<OutMigration> outmigrations = genericDao.findAll(OutMigration.class, true);
		List<OutMigration> outmigrationsInInterval = new ArrayList<OutMigration>();
		
		for (OutMigration outmig : outmigrations) {		
			if (outmig.getRecordedDate().after(startDate) &&
					outmig.getRecordedDate().before(endDate)) {
				outmigrationsInInterval.add(outmig);
			}
		}
		return outmigrationsInInterval;
	}
	
	public List<Death> getDeathsBetweenInterval(Calendar startDate, Calendar endDate) {
		
		List<Death> deaths = genericDao.findAll(Death.class, true);
		List<Death> deathsInInterval = new ArrayList<Death>();
		
		for (Death death : deaths) {		
			if (death.getDeathDate().after(startDate) &&
					death.getDeathDate().before(endDate)) {
				deathsInInterval.add(death);
			}
		}
		return deathsInInterval;
	}
	
	public List<PregnancyOutcome> getPregnanciesBetweenInterval(Calendar startDate, Calendar endDate) {
		return pregnancyService.findAllLiveBirthsBetweenInterval(startDate, endDate);
	}
			
	/**
	 * Returns the index in which age report record the age belongs.
	 * Corresponds to the CalculationService reportRecord
	 */
	public int determineAgeGroup(int age) {
		
		int index = 0;
		for (ReportRecordBean record : reportRecords) {
			if (!record.getAgeGroupName().equals("ALL")) {
				if (age >= record.getMin() && age < record.getMax()) 
					break;
			}
			index++;
		}
		return index;
	}
		
	public void setAgeGroups(double age, Individual individual, boolean denominator) {	
		
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			if (age >= record.getMin() && age < record.getMax()) {
				if (individual.getGender().equals(siteProperties.getMaleCode())) {
					if (denominator) 
						record.addDenominatorMale();
					else
						record.addNumeratorMale();
				}
				else {
					if (denominator) 
						record.addDenominatorFemale();
					else
						record.addNumeratorFemale();
				}
			}
		}
	}
	
	public void setNumeratorsForPopulation() {
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			record.setNumeratorMale(record.getPdoMale() / 365.25);
			record.setNumeratorFemale(record.getPdoFemale() / 365.25);
			
		}
	}
	
	public void setAgeGroupsForBirths(double age, Individual individual, PregnancyOutcome outcome) {	
		
		for (int i = 1; i < reportRecords.size(); i++) {
			ReportRecordBean record = reportRecords.get(i);
			if (age >= record.getMin() && age < record.getMax()) {
				for (Outcome o : outcome.getOutcomes()) {
					if (o.getType().equals(siteProperties.getLiveBirthCode())) {
						if (o.getChild().getGender().equals(siteProperties.getMaleCode())) 
							record.addNumeratorMale();
						else
							record.addNumeratorFemale();
					}
				}
			}
		}
	}
	
	public void setInfantGroups(double age, Individual individual, Calendar startDate, Calendar endDate, 
			MortalityRecordBean neoNatalRecord, MortalityRecordBean postNatalRecord, MortalityRecordBean infantRecord) {
			
		if (age >= neoNatalRecord.getMin() && age < neoNatalRecord.getMax()) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) 
				neoNatalRecord.addNeoNatalMale();
			else 
				neoNatalRecord.addNeoNatalFemale();
		}
		else if (age >= postNatalRecord.getMin() && age < postNatalRecord.getMax()) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) 
				postNatalRecord.addPostNatalMale();
			else
				postNatalRecord.addPostNatalFemale();
		}
		
		if (age >= infantRecord.getMin() && age < infantRecord.getMax()) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) 
				infantRecord.addInfantMale();
			else
				infantRecord.addInfantFemale();
		}
		int maleTotal = pregnancyService.findAllBirthsBetweenIntervalByGender(startDate, endDate, 0);
		int femaleTotal = pregnancyService.findAllBirthsBetweenIntervalByGender(startDate, endDate, 1);
		neoNatalRecord.setTotalMaleOutcomes(maleTotal);
		neoNatalRecord.setTotalFemaleOutcomes(femaleTotal);
		postNatalRecord.setTotalMaleOutcomes(maleTotal);
		postNatalRecord.setTotalFemaleOutcomes(femaleTotal);
		infantRecord.setTotalMaleOutcomes(maleTotal);
		infantRecord.setTotalFemaleOutcomes(femaleTotal);
	}
		
	public List<ReportRecordBean> getReportRecords() {
		return reportRecords;
	}
}
