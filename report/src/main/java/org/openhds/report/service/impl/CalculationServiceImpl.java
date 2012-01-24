package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openhds.controller.service.DemRatesService;
import org.openhds.domain.model.Individual;
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
	
	public CalculationServiceImpl(SitePropertiesService siteProperties, DemRatesService demRatesService) {
		this.siteProperties = siteProperties;
		this.demRatesService = demRatesService;
		initializeGroups();
	}
	
	private void initializeGroups() {
		reportRecords.add(new ReportRecordBean("ALL"));
		reportRecords.add(new ReportRecordBean("0-4"));
		reportRecords.add(new ReportRecordBean("5-9"));
		reportRecords.add(new ReportRecordBean("10-14"));
		reportRecords.add(new ReportRecordBean("15-19"));
		reportRecords.add(new ReportRecordBean("20-24"));
		reportRecords.add(new ReportRecordBean("25-29"));
		reportRecords.add(new ReportRecordBean("30-34"));
		reportRecords.add(new ReportRecordBean("35-39"));
		reportRecords.add(new ReportRecordBean("40-44"));
		reportRecords.add(new ReportRecordBean("45-49"));
		reportRecords.add(new ReportRecordBean("50-54"));
		reportRecords.add(new ReportRecordBean("55-59"));
		reportRecords.add(new ReportRecordBean("60-64"));
		reportRecords.add(new ReportRecordBean("65+"));
		
	}
	
	public void completeReportRecords(Calendar startDate, Calendar endDate) {		
		// calculate pdo's
		int daysBetween = (int) demRatesService.daysBetween(startDate, endDate);
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
	
	public List<ReportRecordBean> getReportRecords() {
		return reportRecords;
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
}
