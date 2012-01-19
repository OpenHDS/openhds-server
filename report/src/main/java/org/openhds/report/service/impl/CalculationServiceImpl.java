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
	 * ageGrp1:  <= 0
	 * ageGrp2:  1
	 * ageGrp3:  2
	 * ageGrp4:  3
	 * ageGrp5:  4
	 * ageGrp6:  5-9
	 * ageGrp7:  10-14
	 * ageGrp8:  15-19
	 * ageGrp9:  20-24
	 * ageGrp10: 25-29
	 * ageGrp11: 30-34
	 * ageGrp12: 35-39
	 * ageGrp13: 40-44
	 * ageGrp14: 45-49
	 * ageGrp15: 50-54
	 * ageGrp16: 55-59
	 * ageGrp17: 60-64
	 * ageGrp18: 65-69
	 * ageGrp19: 70-74
	 * ageGrp20: 75-79
	 * ageGrp21: 80-84
	 * ageGrp22: 85-89
	 * ageGrp23: 90-94
	 * ageGrp24: >= 95
	 */
	ReportRecordBean ageGrp1, ageGrp2, ageGrp3, ageGrp4, ageGrp5, ageGrp6, ageGrp7, ageGrp8, ageGrp9,
		ageGrp10, ageGrp11, ageGrp12, ageGrp13, ageGrp14, ageGrp15, ageGrp16, ageGrp17, ageGrp18, 
		ageGrp19, ageGrp20, ageGrp21, ageGrp22, ageGrp23, ageGrp24;
	
	SitePropertiesService siteProperties;
	DemRatesService demRatesService;
	
	public CalculationServiceImpl(SitePropertiesService siteProperties, DemRatesService demRatesService) {
		this.siteProperties = siteProperties;
		this.demRatesService = demRatesService;
		initializeGroups();
	}
	
	private void initializeGroups() {
		ageGrp1 = new ReportRecordBean("0");
		ageGrp2 = new ReportRecordBean("1");
		ageGrp3 = new ReportRecordBean("2");
		ageGrp4 = new ReportRecordBean("3");
		ageGrp5 = new ReportRecordBean("4");
		ageGrp6 = new ReportRecordBean("5-9");
		ageGrp7 = new ReportRecordBean("10-14");
		ageGrp8 = new ReportRecordBean("15-19");
		ageGrp9 = new ReportRecordBean("20-24");
		ageGrp10 = new ReportRecordBean("25-29");
		ageGrp11 = new ReportRecordBean("30-34");
		ageGrp12 = new ReportRecordBean("35-39");
		ageGrp13 = new ReportRecordBean("40-44");
		ageGrp14 = new ReportRecordBean("45-49");
		ageGrp15 = new ReportRecordBean("50-54");
		ageGrp16 = new ReportRecordBean("55-59");
		ageGrp17 = new ReportRecordBean("60-64");
		ageGrp18 = new ReportRecordBean("65-69");
		ageGrp19 = new ReportRecordBean("70-74");
		ageGrp20 = new ReportRecordBean("75-79");
		ageGrp21 = new ReportRecordBean("80-84");
		ageGrp22 = new ReportRecordBean("85-89");
		ageGrp23 = new ReportRecordBean("90-94");
		ageGrp24 = new ReportRecordBean("95+");
	}
		
	public void setDenominatorTotals() {
		ageGrp1.setDenominatorTotal(ageGrp1.getDenominatorMale() + ageGrp1.getDenominatorFemale());
		ageGrp2.setDenominatorTotal(ageGrp2.getDenominatorMale() + ageGrp2.getDenominatorFemale());
		ageGrp3.setDenominatorTotal(ageGrp3.getDenominatorMale() + ageGrp3.getDenominatorFemale());
		ageGrp4.setDenominatorTotal(ageGrp4.getDenominatorMale() + ageGrp4.getDenominatorFemale());
		ageGrp5.setDenominatorTotal(ageGrp5.getDenominatorMale() + ageGrp5.getDenominatorFemale());
		ageGrp6.setDenominatorTotal(ageGrp6.getDenominatorMale() + ageGrp6.getDenominatorFemale());
		ageGrp7.setDenominatorTotal(ageGrp7.getDenominatorMale() + ageGrp7.getDenominatorFemale());
		ageGrp8.setDenominatorTotal(ageGrp8.getDenominatorMale() + ageGrp8.getDenominatorFemale());
		ageGrp9.setDenominatorTotal(ageGrp9.getDenominatorMale() + ageGrp9.getDenominatorFemale());
		ageGrp10.setDenominatorTotal(ageGrp10.getDenominatorMale() + ageGrp10.getDenominatorFemale());
		ageGrp11.setDenominatorTotal(ageGrp11.getDenominatorMale() + ageGrp11.getDenominatorFemale());
		ageGrp12.setDenominatorTotal(ageGrp12.getDenominatorMale() + ageGrp12.getDenominatorFemale());
		ageGrp13.setDenominatorTotal(ageGrp13.getDenominatorMale() + ageGrp13.getDenominatorFemale());
		ageGrp14.setDenominatorTotal(ageGrp14.getDenominatorMale() + ageGrp14.getDenominatorFemale());
		ageGrp15.setDenominatorTotal(ageGrp15.getDenominatorMale() + ageGrp15.getDenominatorFemale());
		ageGrp16.setDenominatorTotal(ageGrp16.getDenominatorMale() + ageGrp16.getDenominatorFemale());
		ageGrp17.setDenominatorTotal(ageGrp17.getDenominatorMale() + ageGrp17.getDenominatorFemale());
		ageGrp18.setDenominatorTotal(ageGrp18.getDenominatorMale() + ageGrp18.getDenominatorFemale());
		ageGrp19.setDenominatorTotal(ageGrp19.getDenominatorMale() + ageGrp19.getDenominatorFemale());
		ageGrp20.setDenominatorTotal(ageGrp20.getDenominatorMale() + ageGrp20.getDenominatorFemale());
		ageGrp21.setDenominatorTotal(ageGrp21.getDenominatorMale() + ageGrp21.getDenominatorFemale());
		ageGrp22.setDenominatorTotal(ageGrp22.getDenominatorMale() + ageGrp22.getDenominatorFemale());
		ageGrp23.setDenominatorTotal(ageGrp23.getDenominatorMale() + ageGrp23.getDenominatorFemale());
		ageGrp24.setDenominatorTotal(ageGrp24.getDenominatorMale() + ageGrp24.getDenominatorFemale());
	}
	
	public void setNumeratorTotals() {
		ageGrp1.setNumeratorTotal(ageGrp1.getNumeratorMale() + ageGrp1.getNumeratorFemale());
		ageGrp2.setNumeratorTotal(ageGrp2.getNumeratorMale() + ageGrp2.getNumeratorFemale());
		ageGrp3.setNumeratorTotal(ageGrp3.getNumeratorMale() + ageGrp3.getNumeratorFemale());
		ageGrp4.setNumeratorTotal(ageGrp4.getNumeratorMale() + ageGrp4.getNumeratorFemale());
		ageGrp5.setNumeratorTotal(ageGrp5.getNumeratorMale() + ageGrp5.getNumeratorFemale());
		ageGrp6.setNumeratorTotal(ageGrp6.getNumeratorMale() + ageGrp6.getNumeratorFemale());
		ageGrp7.setNumeratorTotal(ageGrp7.getNumeratorMale() + ageGrp7.getNumeratorFemale());
		ageGrp8.setNumeratorTotal(ageGrp8.getNumeratorMale() + ageGrp8.getNumeratorFemale());
		ageGrp9.setNumeratorTotal(ageGrp9.getNumeratorMale() + ageGrp9.getNumeratorFemale());
		ageGrp10.setNumeratorTotal(ageGrp10.getNumeratorMale() + ageGrp10.getNumeratorFemale());
		ageGrp11.setNumeratorTotal(ageGrp11.getNumeratorMale() + ageGrp11.getNumeratorFemale());
		ageGrp12.setNumeratorTotal(ageGrp12.getNumeratorMale() + ageGrp12.getNumeratorFemale());
		ageGrp13.setNumeratorTotal(ageGrp13.getNumeratorMale() + ageGrp13.getNumeratorFemale());
		ageGrp14.setNumeratorTotal(ageGrp14.getNumeratorMale() + ageGrp14.getNumeratorFemale());
		ageGrp15.setNumeratorTotal(ageGrp15.getNumeratorMale() + ageGrp15.getNumeratorFemale());
		ageGrp16.setNumeratorTotal(ageGrp16.getNumeratorMale() + ageGrp16.getNumeratorFemale());
		ageGrp17.setNumeratorTotal(ageGrp17.getNumeratorMale() + ageGrp17.getNumeratorFemale());
		ageGrp18.setNumeratorTotal(ageGrp18.getNumeratorMale() + ageGrp18.getNumeratorFemale());
		ageGrp19.setNumeratorTotal(ageGrp19.getNumeratorMale() + ageGrp19.getNumeratorFemale());
		ageGrp20.setNumeratorTotal(ageGrp20.getNumeratorMale() + ageGrp20.getNumeratorFemale());
		ageGrp21.setNumeratorTotal(ageGrp21.getNumeratorMale() + ageGrp21.getNumeratorFemale());
		ageGrp22.setNumeratorTotal(ageGrp22.getNumeratorMale() + ageGrp22.getNumeratorFemale());
		ageGrp23.setNumeratorTotal(ageGrp23.getNumeratorMale() + ageGrp23.getNumeratorFemale());
		ageGrp24.setNumeratorTotal(ageGrp24.getNumeratorMale() + ageGrp24.getNumeratorFemale());
	}
	
	public List<ReportRecordBean> getReportRecords() {
		List<ReportRecordBean> reports = new ArrayList<ReportRecordBean>();
		reports.add(ageGrp1);
		reports.add(ageGrp2);
		reports.add(ageGrp3);
		reports.add(ageGrp4);
		reports.add(ageGrp5);
		reports.add(ageGrp6);
		reports.add(ageGrp7);
		reports.add(ageGrp8);
		reports.add(ageGrp9);
		reports.add(ageGrp10);
		reports.add(ageGrp11);
		reports.add(ageGrp12);
		reports.add(ageGrp13);
		reports.add(ageGrp14);
		reports.add(ageGrp15);
		reports.add(ageGrp16);
		reports.add(ageGrp17);
		reports.add(ageGrp18);
		reports.add(ageGrp19);
		reports.add(ageGrp20);
		reports.add(ageGrp21);
		reports.add(ageGrp22);
		reports.add(ageGrp23);
		reports.add(ageGrp24);
		return reports;
	}
	
	public void setAgeGroups(long age, Individual individual, boolean denominator) {
		if (age < 1) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp1.addDenominatorMale();
				else
					ageGrp1.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp1.addDenominatorFemale();
				else
					ageGrp1.addNumeratorFemale();
			}
		}
		else if (age >= 1 && age < 2) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp2.addDenominatorMale();
				else
					ageGrp2.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp2.addDenominatorFemale();
				else
					ageGrp2.addNumeratorFemale();
			}
		}
		else if (age >= 2 && age < 3) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp3.addDenominatorMale();
				else
					ageGrp3.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp3.addDenominatorFemale();
				else
					ageGrp3.addNumeratorFemale();
			}
		}
		else if (age >= 3 && age < 4) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp4.addDenominatorMale();
				else
					ageGrp4.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp4.addDenominatorFemale();
				else
					ageGrp4.addNumeratorFemale();
			}
		}
		else if (age >= 4 && age < 5) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp5.addDenominatorMale();
				else
					ageGrp5.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp5.addDenominatorFemale();
				else
					ageGrp5.addNumeratorFemale();
			}
		}
		else if (age >= 5 && age < 10) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp6.addDenominatorMale();
				else
					ageGrp6.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp6.addDenominatorFemale();
				else
					ageGrp6.addNumeratorFemale();
			}
		}
		else if (age >= 10 && age < 15) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp7.addDenominatorMale();
				else
					ageGrp7.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp7.addDenominatorFemale();
				else
					ageGrp7.addNumeratorFemale();
			}
		}
		else if (age >= 15 && age < 20) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp8.addDenominatorMale();
				else
					ageGrp8.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp8.addDenominatorFemale();
				else
					ageGrp8.addNumeratorFemale();
			}
		}
		else if (age >= 20 && age < 25) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp9.addDenominatorMale();
				else
					ageGrp9.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp9.addDenominatorFemale();
				else
					ageGrp9.addNumeratorFemale();
			}
		}
		else if (age >= 25 && age < 30) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp10.addDenominatorMale();
				else
					ageGrp10.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp10.addDenominatorFemale();
				else
					ageGrp10.addNumeratorFemale();
			}
		}
		else if (age >= 30 && age < 35) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp11.addDenominatorMale();
				else
					ageGrp11.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp11.addDenominatorFemale();
				else
					ageGrp11.addNumeratorFemale();
			}
		}
		else if (age >= 35 && age < 40) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp12.addDenominatorMale();
				else
					ageGrp12.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp12.addDenominatorFemale();
				else
					ageGrp12.addNumeratorFemale();
			}
		}
		else if (age >= 40 && age < 45) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp13.addDenominatorMale();
				else
					ageGrp13.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp13.addDenominatorFemale();
				else
					ageGrp13.addNumeratorFemale();
			}
		}
		else if (age >= 45 && age < 50) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp14.addDenominatorMale();
				else
					ageGrp14.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp14.addDenominatorFemale();
				else
					ageGrp14.addNumeratorFemale();
			}
		}
		else if (age >= 50 && age < 55) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp15.addDenominatorMale();
				else
					ageGrp15.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp15.addDenominatorFemale();
				else
					ageGrp15.addNumeratorFemale();
			}
		}
		else if (age >= 55 && age < 60) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp16.addDenominatorMale();
				else
					ageGrp16.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp16.addDenominatorFemale();
				else
					ageGrp16.addNumeratorFemale();
			}
		}
		else if (age >= 60 && age < 65) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp17.addDenominatorMale();
				else
					ageGrp17.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp17.addDenominatorFemale();
				else
					ageGrp17.addNumeratorFemale();
			}
		}
		else if (age >= 65 && age < 70) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp18.addDenominatorMale();
				else
					ageGrp18.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp18.addDenominatorFemale();
				else
					ageGrp18.addNumeratorFemale();
			}
		}
		else if (age >= 70 && age < 75) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp19.addDenominatorMale();
				else
					ageGrp19.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp19.addDenominatorFemale();
				else
					ageGrp19.addNumeratorFemale();
			}
		}
		else if (age >= 75 && age < 80) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp20.addDenominatorMale();
				else
					ageGrp20.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp20.addDenominatorFemale();
				else
					ageGrp20.addNumeratorFemale();
			}
		}
		else if (age >= 80 && age < 85) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp21.addDenominatorMale();
				else
					ageGrp21.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp21.addDenominatorFemale();
				else
					ageGrp21.addNumeratorFemale();
			}
		}
		else if (age >= 85 && age < 90) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp22.addDenominatorMale();
				else
					ageGrp22.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp22.addDenominatorFemale();
				else
					ageGrp22.addNumeratorFemale();
			}
		}
		else if (age >= 90 && age < 95) {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp23.addDenominatorMale();
				else
					ageGrp23.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp23.addDenominatorFemale();
				else
					ageGrp23.addNumeratorFemale();
			}
		}
		else {
			if (individual.getGender().equals(siteProperties.getMaleCode())) {
				if (denominator)	
					ageGrp24.addDenominatorMale();
				else
					ageGrp24.addNumeratorMale();
			}
			else {
				if (denominator)
					ageGrp24.addDenominatorFemale();
				else
					ageGrp24.addNumeratorFemale();
			}
		}
	}
}
