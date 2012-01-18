package org.openhds.report.service.impl;

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
	
	public CalculationServiceImpl(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
		initializeGroups();
	}
	
	private void initializeGroups() {
		ageGrp1 = new ReportRecordBean();
		ageGrp2 = new ReportRecordBean();
		ageGrp3 = new ReportRecordBean();
		ageGrp4 = new ReportRecordBean();
		ageGrp5 = new ReportRecordBean();
		ageGrp6 = new ReportRecordBean();
		ageGrp7 = new ReportRecordBean();
		ageGrp8 = new ReportRecordBean();
		ageGrp9 = new ReportRecordBean();
		ageGrp10 = new ReportRecordBean();
		ageGrp11 = new ReportRecordBean();
		ageGrp12 = new ReportRecordBean();
		ageGrp13 = new ReportRecordBean();
		ageGrp14 = new ReportRecordBean();
		ageGrp15 = new ReportRecordBean();
		ageGrp16 = new ReportRecordBean();
		ageGrp17 = new ReportRecordBean();
		ageGrp18 = new ReportRecordBean();
		ageGrp19 = new ReportRecordBean();
		ageGrp20 = new ReportRecordBean();
		ageGrp21 = new ReportRecordBean();
		ageGrp22 = new ReportRecordBean();
		ageGrp23 = new ReportRecordBean();
		ageGrp24 = new ReportRecordBean();
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
