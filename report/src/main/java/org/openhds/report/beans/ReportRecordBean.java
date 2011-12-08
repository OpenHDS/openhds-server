package org.openhds.report.beans;

import static org.openhds.report.calculations.Formulas.*;

public class ReportRecordBean {

	// based on population stuff for now
	String ageGroupName;
	public long male;
	public long female;
	public long totalGender;
	public double malePercent;
	public double femalePercent;
	public double totalPercent;
	public long totalIndividuals;

	Long numeratorTotal, denominatorTotal;
	private double min; // min age in days for category
	private double max; // days
	Double totalPDO = 0.0;
	Double malePDO = 0.0;
	Double femalePDO = 0.0;

	Double malePYO = 0.0, femalePYO = 0.0, totalPYO = 0.0;

	Double maleMR = 0.0, femaleMR = 0.0, totalMR = 0.0, fertiRate;

	Double allIndivsPYO, allMalePYO, allFemalePYO;

	Integer marriageCount, divorceCount, widowingsCount, sepCount, reconCount;

	// Death-Birth Ratios
	Double maleDBRatio, femaleDBRatio, ageTotalDBRatio;

	// Births
	Integer maleBirths, femaleBirths, totalBirths;
	Double motherPDO = 0.0;

	public ReportRecordBean() { }

	public ReportRecordBean(String name, double min, double max) {
		this.setAgeGroup(name);
		this.setMin(min);
		this.setMax(max);
	}

	public boolean isInCorrectAgeGroup(double currentAgeInDays) {
		return inInterval(currentAgeInDays, getMin(), getMax());
	}

	public void calculateDerived() {
		
		if (getNumeratorTotal()!=null && getNumeratorTotal() > 0) {
			setTotalGender(getMale() + getFemale());
			setMalePercent(100.00 * getMale() / getNumeratorTotal());
			setFemalePercent(100.00 * getFemale() / getNumeratorTotal());
			setTotalPercent(100.00 * getTotalGender() / getNumeratorTotal());
		}
		
		setPdo(getMalePDO() + getFemaleDPO());
		setMalePYO(malePDO / 365.25);
		setFemalePYO(femalePDO / 365.25);
		setTotalPYO(totalPDO / 365.25);

		// Event rate
		if (malePDO > 0 ) { // avoid division-by-zero errors
			setMaleMR(1000 * 365.25 * male / malePDO);}
			
		if (femalePDO > 0) {setFemaleMR(1000 * 365.25 * female / femalePDO);}
		
		if (totalPDO > 0) {	setTotalMR(1000 * 365.25 * totalGender / totalPDO);}
		
		// calculate fertility rate
		if (motherPDO > 0) { // avoid division-by-zero errors
			setFertiRate(1000 * 365.25 * (male + female) / motherPDO);
		}
		else{
			setFertiRate(0.0);
		}
	}

	public String getAgeGroup() {
		return ageGroupName;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroupName = ageGroup;
	}

	public long getMale() {
		return male;
	}

	public void setMale(long male) {
		this.male = male;
	}

	public long getFemale() {
		return female;
	}

	public void setFemale(long female) {
		this.female = female;
	}

	public long getTotalGender() {
		return getMale() + getFemale();
	}

	public void setTotalGender(long totalGender) {
		this.totalGender = totalGender;
	}

	public double getMalePercent() {
		return malePercent;
	}

	public void setMalePercent(double d) {
		this.malePercent = d;
	}

	public double getFemalePercent() {
		return femalePercent;
	}

	public void setFemalePercent(double d) {
		this.femalePercent = d;
	}

	public double getTotalPercent() {
		return totalPercent;
	}

	public void setTotalPercent(double totalPercent) {
		this.totalPercent = totalPercent;
	}

	public long getTotalIndividuals() {
		return totalIndividuals;
	}

	public void setTotalIndividuals(long totalIndividuals) {
		this.totalIndividuals = totalIndividuals;
	}

	public void addMale() {
		male++;
	}

	public void addFemale() {
		female++;
	}

	/**
	 * @return the ageGroupName
	 */
	public String getAgeGroupName() {
		return ageGroupName;
	}

	/**
	 * @param ageGroupName
	 *            the ageGroupName to set
	 */
	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @return the pdo
	 */
	public double getTotalPdo() {
		return getMalePDO() + getFemaleDPO();
	}

	/**
	 * @param pdo
	 *            the pdo to set
	 */
	public void setPdo(double pdo) {
		this.totalPDO = pdo;
	}

	/**
	 * @return the malePDO
	 */
	public double getMalePDO() {
		return malePDO;
	}

	/**
	 * @param malePDO
	 *            the malePDO to set
	 */
	public void setMalePDO(double malePDO) {
		this.malePDO = malePDO;
	}

	/**
	 * @return the femaleDPO
	 */
	public double getFemaleDPO() {
		return femalePDO;
	}

	/**
	 * @param femaleDPO
	 *            the femaleDPO to set
	 */
	public void setFemaleDPO(double femaleDPO) {
		this.femalePDO = femaleDPO;
	}

	public void addFemaleDPO(double newFemaleDPO) {
		femalePDO += newFemaleDPO;
	}

	public void addMaleDPO(double newMaleDPO) {
		malePDO += newMaleDPO;
	}

	/**
	 * @return the maleMR
	 */
	public double getMaleMR() {
		return maleMR;
	}

	/**
	 * @param maleMR
	 *            the maleMR to set
	 */
	public void setMaleMR(double maleMR) {
		this.maleMR = maleMR;
	}

	/**
	 * @return the femaleMR
	 */
	public double getFemaleMR() {
		return femaleMR;
	}

	/**
	 * @param femaleMR
	 *            the femaleMR to set
	 */
	public void setFemaleMR(double femaleMR) {
		this.femaleMR = femaleMR;
	}

	/**
	 * @return the totalMR
	 */
	public double getTotalMR() {
		return totalMR;
	}

	/**
	 * @param totalMR
	 *            the totalMR to set
	 */
	public void setTotalMR(double totalMR) {
		this.totalMR = totalMR;
	}

	/**
	 * @return the malePYO
	 */
	public Double getMalePYO() {
		return malePYO;
	}

	/**
	 * @param malePYO
	 *            the malePYO to set
	 */
	public void setMalePYO(Double malePYO) {
		this.malePYO = malePYO;
	}

	/**
	 * @return the femalePYO
	 */
	public Double getFemalePYO() {
		return femalePYO;
	}

	/**
	 * @param femalePYO
	 *            the femalePYO to set
	 */
	public void setFemalePYO(Double femalePYO) {
		this.femalePYO = femalePYO;
	}

	/**
	 * @return the totalPYO
	 */
	public Double getTotalPYO() {
		return totalPYO;
	}

	/**
	 * @param totalPYO
	 *            the totalPYO to set
	 */
	public void setTotalPYO(Double totalPYO) {
		this.totalPYO = totalPYO;
	}

	/**
	 * @return the marriageCount
	 */
	public Integer getMarriageCount() {
		return marriageCount;
	}

	/**
	 * @param marriageCount
	 *            the marriageCount to set
	 */
	public void setMarriageCount(Integer marriageCount) {
		this.marriageCount = marriageCount;
	}

	/**
	 * @return the divorceCount
	 */
	public Integer getDivorceCount() {
		return divorceCount;
	}

	/**
	 * @param divorceCount
	 *            the divorceCount to set
	 */
	public void setDivorceCount(Integer divorceCount) {
		this.divorceCount = divorceCount;
	}

	/**
	 * @return the widowingsCount
	 */
	public Integer getWidowingsCount() {
		return widowingsCount;
	}

	/**
	 * @param widowingsCount
	 *            the widowingsCount to set
	 */
	public void setWidowingsCount(Integer widowingsCount) {
		this.widowingsCount = widowingsCount;
	}

	/**
	 * @return the sepCount
	 */
	public Integer getSepCount() {
		return sepCount;
	}

	/**
	 * @param sepCount
	 *            the sepCount to set
	 */
	public void setSepCount(Integer sepCount) {
		this.sepCount = sepCount;
	}

	/**
	 * @return the reconCount
	 */
	public Integer getReconCount() {
		return reconCount;
	}

	/**
	 * @param reconCount
	 *            the reconCount to set
	 */
	public void setReconCount(Integer reconCount) {
		this.reconCount = reconCount;
	}

	/**
	 * @return the numeratorTotal
	 */
	public Long getNumeratorTotal() {
		return numeratorTotal;
	}

	/**
	 * @param numeratorTotal
	 *            the numeratorTotal to set
	 */
	public void setNumeratorTotal(Long numeratorTotal) {
		this.numeratorTotal = numeratorTotal;
	}

	/**
	 * @return the denominatorTotal
	 */
	public Long getDenominatorTotal() {
		return denominatorTotal;
	}

	/**
	 * @param denominatorTotal
	 *            the denominatorTotal to set
	 */
	public void setDenominatorTotal(Long denominatorTotal) {
		this.denominatorTotal = denominatorTotal;
	}

	/**
	 * @return the fertiRate
	 */
	public Double getFertiRate() {
		return fertiRate;
	}

	/**
	 * @param fertiRate the fertiRate to set
	 */
	public void setFertiRate(Double fertiRate) {
		this.fertiRate = fertiRate;
	}

	public void addMotherDPO(Long pd) {
		motherPDO += pd;
		
	}

	/**
	 * @return the motherPDO
	 */
	public Double getMotherPDO() {
		return motherPDO;
	}

	/**
	 * @param motherPDO the motherPDO to set
	 */
	public void setMotherPDO(Double motherPDO) {
		this.motherPDO = motherPDO;
	}
}
