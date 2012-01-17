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
	
	public void setAgeGroups(long age, Individual individual) {
		if (age < 1) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp1.addMale();
			else
				ageGrp1.addFemale();
		}
		else if (age >= 1 && age < 2) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp2.addMale();
			else
				ageGrp2.addFemale();
		}
		else if (age >= 2 && age < 3) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp3.addMale();
			else
				ageGrp3.addFemale();
		}
		else if (age >= 3 && age < 4) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp4.addMale();
			else
				ageGrp4.addFemale();
		}
		else if (age >= 4 && age < 5) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp5.addMale();
			else
				ageGrp5.addFemale();
		}
		else if (age >= 5 && age < 10) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp6.addMale();
			else
				ageGrp6.addFemale();
		}
		else if (age >= 10 && age < 15) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp7.addMale();
			else
				ageGrp7.addFemale();
		}
		else if (age >= 15 && age < 20) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp8.addMale();
			else
				ageGrp8.addFemale();
		}
		else if (age >= 20 && age < 25) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp9.addMale();
			else
				ageGrp9.addFemale();
		}
		else if (age >= 25 && age < 30) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp10.addMale();
			else
				ageGrp10.addFemale();
		}
		else if (age >= 30 && age < 35) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp11.addMale();
			else
				ageGrp11.addFemale();
		}
		else if (age >= 35 && age < 40) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp12.addMale();
			else
				ageGrp12.addFemale();
		}
		else if (age >= 40 && age < 45) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp13.addMale();
			else
				ageGrp13.addFemale();
		}
		else if (age >= 45 && age < 50) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp14.addMale();
			else
				ageGrp14.addFemale();
		}
		else if (age >= 50 && age < 55) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp15.addMale();
			else
				ageGrp15.addFemale();
		}
		else if (age >= 55 && age < 60) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp16.addMale();
			else
				ageGrp16.addFemale();
		}
		else if (age >= 60 && age < 65) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp17.addMale();
			else
				ageGrp17.addFemale();
		}
		else if (age >= 65 && age < 70) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp18.addMale();
			else
				ageGrp18.addFemale();
		}
		else if (age >= 70 && age < 75) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp19.addMale();
			else
				ageGrp19.addFemale();
		}
		else if (age >= 75 && age < 80) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp20.addMale();
			else
				ageGrp20.addFemale();
		}
		else if (age >= 80 && age < 85) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp21.addMale();
			else
				ageGrp21.addFemale();
		}
		else if (age >= 85 && age < 90) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp22.addMale();
			else
				ageGrp22.addFemale();
		}
		else if (age >= 90 && age < 95) {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp23.addMale();
			else
				ageGrp23.addFemale();
		}
		else {
			if (individual.getGender().equals(siteProperties.getMaleCode()))
				ageGrp24.addMale();
			else
				ageGrp24.addFemale();
		}
	}
}
