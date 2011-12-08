package org.openhds.report.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openhds.domain.model.Individual;
import org.openhds.controller.util.CalendarUtil;
import org.openhds.controller.util.DateUtil;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.report.beans.MaritalRecordBean;
import org.openhds.report.beans.ReportRecordBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Ime Asangansi
 */
public class ReportMaker {

	ReportRecordBean all, neonatal, postNatal, infant, _0To4, _1To4, _5To9,
			_10To14, _15To19, _20To24, _25To29, _30To34, _35To39;
	ReportRecordBean _40To44, _45To49, _50To54, _55To59, _60To64, _65To69, _70To74,
			_75To79, _80To84, _85To89, _90To94, _95plus;

	long  reportTotalMale, reportTotalFemale;

	Calendar timeForAgeCalculation;

	Map<String, Object> nameValues = new HashMap<String, Object>();
	
	@Autowired
	DateUtil dateUtil;
	
	@Autowired
	SitePropertiesService siteProperties;

	@Autowired
	SpecialQueries specialQueries;
	
	private ReportRecordBean _15To49;
	private ReportRecordBean _65plus;
	
	ReportRecordBean  blank;

	public ReportMaker() { }

	public ReportRecordBean makeSimpleReport(Set<Individual> all) {
		ReportRecordBean rr = new ReportRecordBean();
		return rr;
	}

	// for each individual, it goes through all the report objects and updates them
	public List makeReport(Set<Individual> selectedPop, Boolean isMidPoint,
			Calendar firstDate, Calendar lastDate, String eventType) {

		List<ReportRecordBean> resultRecords = getEmptyRecordObjects();
		List<Individual> numerator = null;
		List<Individual> storedDenominator = new ArrayList<Individual>(selectedPop);

		long numeratorTotalIndiv, denominatorTotalIndivs;

		// time for age calculation refers to the age group the individual will be added to.
		// and this depends on the denominator type - whether Population at midpoint or Person Years
		if (isMidPoint) {
			// if using midpoint method, let the ages be based on the midpoint of the period
			Calendar midpoint = dateUtil.midPointDate(firstDate, lastDate);
			timeForAgeCalculation = midpoint;
		} else {
			// use the ages at the end date of analysis
			timeForAgeCalculation = lastDate;
		}
		
		// loop through all individuals in the locations for selected periods

		// If denominator is not null, calculate the numerator
		if ((selectedPop != null) && selectedPop.size() > 0) {
			if (eventType.equals("Mortality")) {
				numerator = specialQueries.getDeathsFromIndividuals(selectedPop);
				while(resultRecords.size() > 18) {
					resultRecords.remove(18);
				}
				this._65plus = new ReportRecordBean("65+", (15 * 365.25), (50 * 365.25));
				resultRecords.add(18, _65plus);	
			} 
			else if (eventType.equals("In-Migration")) {
				numerator = specialQueries.getInMigrationsFromIndividuals(selectedPop);
				while(resultRecords.size() > 18) {
					resultRecords.remove(18);
				}
				this._65plus = new ReportRecordBean("65+", (15 * 365.25), (50 * 365.25));
				resultRecords.add(18, _65plus);
			} 
			else if (eventType.equals("Population")) {
				numerator = new ArrayList<Individual>(selectedPop);
			} 
			else if (eventType.equals("Fertility")) {
				numerator = specialQueries.getLiveBirthsFromIndividuals(selectedPop, firstDate, lastDate);
				resultRecords = new ArrayList<ReportRecordBean>();
				// using 1000 as maximum age for the last group
				//this.all = new ReportRecord("All", 0, (50 * 365.25));

				this._15To19 = new ReportRecordBean("15-19", (15 * 365.25), (20 * 365.25));
				this._20To24 = new ReportRecordBean("20-24", (20 * 365.25), (25 * 365.25));
				this._25To29 = new ReportRecordBean("25-29", (25 * 365.25), (30 * 365.25));
				this._30To34 = new ReportRecordBean("30-34", (30 * 365.25), (35 * 365.25));
				this._35To39 = new ReportRecordBean("35-39", (35 * 365.25), (40 * 365.25));
				this._40To44 = new ReportRecordBean("40-44", (40 * 365.25), (45 * 365.25));
				this._45To49 = new ReportRecordBean("45-49", (45 * 365.25), (50 * 365.25));
				this._15To49 = new ReportRecordBean("15-49(GFR)", (15 * 365.25), (50 * 365.25));
				
				// ReportRecord extra = new ReportRecord("Misc", (4 * 365.25), (4 * 365.25));
				//resultRecords.add(0, all);
				resultRecords.add(0, _15To19);
				resultRecords.add(1, _20To24);
				resultRecords.add(2, _25To29);
				resultRecords.add(3, _30To34);
				resultRecords.add(4, _35To39);
				resultRecords.add(5, _40To44);
				resultRecords.add(6, _45To49);
				resultRecords.add(7, _15To49);
			} 
			else if (eventType.equals("Out-Migration")) {
				numerator = specialQueries.getOutMigrationsFromIndividuals(selectedPop);
				
				while(resultRecords.size() > 18) {
					resultRecords.remove(18);
				}
				this._65plus = new ReportRecordBean("65+", (15 * 365.25), (50 * 365.25));
				resultRecords.add(18, _65plus);
			}
			else if (eventType.equals("Internal-Migration")) {
				numerator = specialQueries.getInternalMigrationsFromIndividuals(selectedPop);
				
				while(resultRecords.size() > 18){
					resultRecords.remove(18);
				}
				this._65plus = new ReportRecordBean("65+", (15 * 365.25), (50 * 365.25));	
				resultRecords.add(18, _65plus);
			} 
			else if (eventType.equals("Marital")) {
				List<MaritalRecordBean> marital = new ArrayList<MaritalRecordBean>();

				Long denom = specialQueries.getMaritalCount(selectedPop,
						siteProperties.getMarriageCode(), lastDate, firstDate) / 1000;

				//marital rate = count/denom
				marital.add(
						0,
						new MaritalRecordBean("Marital", specialQueries
								.getMaritalCount(selectedPop,
										siteProperties.getMarriageCode(), lastDate, firstDate), denom));
				marital.add(
						1,
						new MaritalRecordBean("Divorces", specialQueries
								.getMaritalCount(selectedPop,
										siteProperties.getDivorceCode(), lastDate, firstDate), denom));
				marital.add(
						2,
						new MaritalRecordBean("Widowings", specialQueries
								.getMaritalCount(selectedPop,
										siteProperties.getWidowCode(), lastDate, firstDate), denom));
				marital.add(
						3,
						new MaritalRecordBean("Seperations", specialQueries
								.getMaritalCount(selectedPop,
										siteProperties.getSeparatedCode(), lastDate, firstDate), denom));
				marital.add(
						4,
						new MaritalRecordBean("Reconciliiations", specialQueries
								.getMaritalCount(selectedPop,
										siteProperties.getReconcileCode(), lastDate, firstDate), denom));

				return marital;
			} 
			else if (eventType.equals("Child-Mortality-Ratios")) {
				numerator = new ArrayList<Individual>(selectedPop);
				resultRecords = getChildrenRecords();
			} 
			else {
				// empty collection
				numerator = new ArrayList<Individual>();
			}
		} 
		 // if denominator is null
		else {
			return resultRecords;
		}

		// Then deal with setting numerators
		if (!(numerator == null) && !numerator.isEmpty() ) {
			// code here works on updating the report records with data from the
			// individuals
			for (Individual individual : numerator) {
				// get individual's age at the midpoint or at the end of the duration
				double currentAgeInDays;
				boolean fertility = eventType.equals("Fertility") && individual.getMother() != null;
				Calendar mother = individual.getMother().getDob();
				
				if (!eventType.equals("Fertility")) {
					currentAgeInDays = dateUtil.daysBetween(
							individual.getDob(), timeForAgeCalculation);
				}
				else if (eventType.equals("Fertility") && individual.getMother() != null){
					currentAgeInDays = dateUtil.daysBetween(
							mother, timeForAgeCalculation);
				}
				else {
					Calendar defaultMotherAge = Calendar.getInstance();
					defaultMotherAge.set(1900,1, 1);
					currentAgeInDays = dateUtil.daysBetween(
							defaultMotherAge, timeForAgeCalculation);
				}
				for (ReportRecordBean record : resultRecords) {

					ReportRecordBean rec = record;
					// if age group applies, increment female count
					if (rec.isInCorrectAgeGroup(currentAgeInDays)) {
						// set the record's variables here

						if (individual.getGender().equals(siteProperties.getFemaleCode())) {
							// reportTotalFemale++;
							rec.addFemale();
						}
						else if (individual.getGender().equals(siteProperties.getMaleCode())) {
							// reportTotalMale++;
							rec.addMale();
						}	
						if (fertility) {
							rec.addMotherDPO(individual.getMother().getPD(firstDate, lastDate));
						}						
					}
				}// closes records for loop
			}// closes individual loop
		}// closes if
		
		// calculate denominator-based variables
		// get the maledpo, femaledpo and total dpo here
		if(numerator != null &&(!storedDenominator.isEmpty() && !(storedDenominator == null))) {
			// code here works on updating the report records with data from the
			// individuals
			denominatorTotalIndivs = storedDenominator.size();
			// get total number of individuals in dss for analysis
			numeratorTotalIndiv = numerator.size();
			
			for (Individual individual : storedDenominator) {
				
				double currentAgeInDays = dateUtil.daysBetween(individual.getDob(), timeForAgeCalculation);
				denominatorTotalIndivs = storedDenominator.size();
				for (ReportRecordBean record : resultRecords) {

					ReportRecordBean rec = record;
					// if age group applies, increment female count
					if (rec.isInCorrectAgeGroup(currentAgeInDays)) {
						// set the record's variables here
						
						if (individual.getGender().equals(siteProperties.getFemaleCode())) {
							rec.addFemaleDPO(individual.getPD(firstDate,lastDate));
						}
						else if (individual.getGender().equals(siteProperties.getMaleCode())) {
							rec.addMaleDPO(individual.getPD(firstDate, lastDate));
						}
					}
				}// closes records for loop
			}// closes individual loop

			// at this point all the reportRecord Objects in the
			// collectRecords() collection are updated with gender
			// now we update them with aggregate values et al
			for (ReportRecordBean rec : resultRecords) {

				// set thhe record's own denominator
				rec.setNumeratorTotal(numeratorTotalIndiv);
				rec.setDenominatorTotal(denominatorTotalIndivs);				
				
				// now do other calculations and populate derived figures
				rec.calculateDerived();
			}
		} // closes if
		
		if (eventType.equals("Population")) {
			resultRecords.add(4, blank);
			return resultRecords;
		}
		else{
			return resultRecords;
		}
	}

	public List<ReportRecordBean> getChildrenRecords() {

		List<ReportRecordBean> records = new ArrayList<ReportRecordBean>();
		
		neonatal = new ReportRecordBean("Neonatal", 0, 28);
		this.postNatal = new ReportRecordBean("PostNatal", 28, 365.25);
		this.infant = new ReportRecordBean("Infant", 0, 365.25);
		
		records.add(1, neonatal);
		records.add(2, postNatal);
		records.add(3, infant);

		return records;
	}

	public List<ReportRecordBean> getEmptyRecordObjects() {

		List<ReportRecordBean> records = new ArrayList<ReportRecordBean>();
		// using 1000 as maximum age for the last group
		this.all = new ReportRecordBean("All", 0, (365.25 * 1000));

		neonatal = new ReportRecordBean("Neonatal", 0, 28);
		this.postNatal = new ReportRecordBean("Postnatal", 28, 365.25);
		this.infant = new ReportRecordBean("Infant", 0, 365.25);

		this._0To4 = new ReportRecordBean("0-4", 0, (365.25 * 5));
		this._1To4 = new ReportRecordBean("1-4", 365.25, (5 * 365.25));
		this._5To9 = new ReportRecordBean("5-9", (5 * 365.25), (10 * 365.25));
		this._10To14 = new ReportRecordBean("10-14", (10 * 365.25), (15 * 365.25));
		this._15To19 = new ReportRecordBean("15-19", (15 * 365.25), (20 * 365.25));
		this._20To24 = new ReportRecordBean("20-24", (20 * 365.25), (25 * 365.25));
		this._25To29 = new ReportRecordBean("25-29", (25 * 365.25), (30 * 365.25));
		this._30To34 = new ReportRecordBean("30-34", (30 * 365.25), (35 * 365.25));
		this._35To39 = new ReportRecordBean("35-39", (35 * 365.25), (40 * 365.25));
		this._40To44 = new ReportRecordBean("40-44", (40 * 365.25), (45 * 365.25));
		this._45To49 = new ReportRecordBean("45-49", (45 * 365.25), (50 * 365.25));
		this._50To54 = new ReportRecordBean("50-54", (50 * 365.25), (55 * 365.25));
		this._55To59 = new ReportRecordBean("55-59", (55 * 365.25), (60 * 365.25));
		this._60To64 = new ReportRecordBean("60-64", (60 * 365.25), (65 * 365.25));
		this._65To69 = new ReportRecordBean("65-69", (65 * 365.25), (70 * 365.25));
		this._70To74 = new ReportRecordBean("70-74", (70 * 365.25), (75 * 365.25));
		this._75To79 = new ReportRecordBean("75-79", (75 * 365.25), (80 * 365.25));
		this._80To84 = new ReportRecordBean("80-84", (80 * 365.25), (85 * 365.25));
		this._85To89 = new ReportRecordBean("85-89", (85 * 365.25), (90 * 365.25));
		this._90To94 = new ReportRecordBean("90-94", (90 * 365.25), (95 * 365.25));
		this._95plus = new ReportRecordBean("95plus", (95 * 365.25), (1000 * 365.25));
		
		// ReportRecord extra = new ReportRecord("Misc", (4 * 365.25), (4 *
		// 365.25));
		records.add(0, all);
		records.add(1, neonatal);
		records.add(2, postNatal);
		records.add(3, infant);
		records.add(4, _0To4);
		records.add(5, _1To4);
		records.add(6, _5To9);
		records.add(7, _10To14);
		records.add(8, _15To19);
		records.add(9, _20To24);
		records.add(10, _25To29);
		records.add(11, _30To34);
		records.add(12, _35To39);
		records.add(13, _40To44);
		records.add(14, _45To49);
		records.add(15, _50To54);
		records.add(16, _55To59);
		records.add(17, _60To64);
		records.add(18, _65To69);
		records.add(19, _70To74);
		records.add(20, _75To79);
		records.add(21, _80To84);
		records.add(22, _85To89);
		records.add(23, _90To94);
		records.add(24, _95plus);
		// records.add(0, extra);
		return records;
	}
	
	public Calendar getTimeSliceForAges() {
		return timeForAgeCalculation;
	}

	public void setTimeSliceForAges(Calendar timeSliceForAges) {
		this.timeForAgeCalculation = timeSliceForAges;
	}

	public SitePropertiesService getSiteProperties() {
		return siteProperties;
	}

	public void setSiteProperties(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
	}

	public SpecialQueries getSpecialQueries() {
		return specialQueries;
	}

	public void setSpecialQueries(SpecialQueries specialQueries) {
		this.specialQueries = specialQueries;
	}
}