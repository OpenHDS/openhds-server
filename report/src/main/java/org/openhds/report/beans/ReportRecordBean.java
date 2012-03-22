package org.openhds.report.beans;

public class ReportRecordBean {
	
	String ageGroupName;
	
	double denominatorMale;
	double denominatorFemale;
	
	double numeratorMale;
	double numeratorFemale;
	
	// this field's value is set to the AgeGroupAll's numerator total
	double total;
	
	double pdoMale;
	double pdoFemale;
	
	double min;
	double max;
	
	public ReportRecordBean(String ageGroupName, double min, double max) {
		this.ageGroupName = ageGroupName;
		this.min = min;
		this.max = max;
	}
	
	public void addDenominatorMale() {
		denominatorMale++;
	}
	
	public void addDenominatorFemale() {
		denominatorFemale++;
	}
	
	public void addNumeratorMale() {
		numeratorMale++;
	}
	
	public void addNumeratorFemale() {
		numeratorFemale++;
	}	
	
	public void addDenominatorMale(double amount) {
		denominatorMale += amount;
	}
	
	public void addDenominatorFemale(double amount) {
		denominatorFemale += amount;
	}
	
	public void addNumeratorMale(double amount) {
		numeratorMale += amount;
	}
	
	public void addNumeratorFemale(double amount) {
		numeratorFemale += amount;
	}
	
	public void addPdoMale() {
		pdoMale++;
	}
	
	public void addPdoFemale() {
		pdoFemale++;
	}	
	
	public void setDenominatorMale(double denominatorMale) {
		this.denominatorMale = denominatorMale;
	}

	public void setDenominatorFemale(double denominatorFemale) {
		this.denominatorFemale = denominatorFemale;
	}
	
	public String getAgeGroupName() {
		return ageGroupName;
	}

	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}
	
	public double getDenominatorMale() {
		return denominatorMale;
	}

	public double getDenominatorFemale() {
		return denominatorFemale;
	}

	public double getNumeratorMale() {
		return numeratorMale;
	}

	public void setNumeratorMale(double numeratorMale) {
		this.numeratorMale = numeratorMale;
	}

	public double getNumeratorFemale() {
		return numeratorFemale;
	}

	public void setNumeratorFemale(double numeratorFemale) {
		this.numeratorFemale = numeratorFemale;
	}
	
	public double getNumeratorTotal() {
		return numeratorMale + numeratorFemale;
	}

	public double getDenominatorTotal() {
		return denominatorMale + denominatorFemale;
	}
	
	public double getPdoMale() {
		return pdoMale;
	}

	public void setPdoMale(double pdoMale) {
		this.pdoMale = pdoMale;
	}
	
	public void addPdoMale(double pdoMale) {
		this.pdoMale += pdoMale;
	}

	public double getPdoFemale() {
		return pdoFemale;
	}

	public void setPdoFemale(double pdoFemale) {
		this.pdoFemale = pdoFemale;
	}
	
	public void addPdoFemale(double pdoFemale) {
		this.pdoFemale += pdoFemale;
	}
	
	public double getPyoMale() {
		if (getPdoMale() != 0) {
			return ((1000 * 365.25 * numeratorMale) / getPdoMale());
		}
		return 0;
	}

	public double getPyoFemale() {
		if (getPdoFemale() != 0) { 
			return ((1000 * 365.25 * numeratorFemale) / getPdoFemale());
		}
		return 0;
	}
	
	public double getPyoTotal() {
		if (getPdoMale() + getPdoFemale() > 0) {
			return (1000 * 365.25 * (numeratorMale + numeratorFemale)) / (getPdoMale() + getPdoFemale());
		}
		return 0;
	}
	
	public double getPyo() {
		return (getPdoMale() + getPdoFemale()) / 365.25;
	}
	
	public double getPyoForFemaleOnly() {
		return (getPdoFemale()) / 365.25;
	}
	
	public double getFertilityRate() {
		if (getPyoForFemaleOnly() > 0) {		
			return ((1000 * 365.25 * getNumeratorTotal()) / getPdoFemale());
		}
		return 0;
	}
	
	public double getTotalFertilityRate() {
		if (getPdoFemale() > 0)
			return ((getNumeratorTotal() / getPdoFemale()) * 365.24 * 5);
		return 0;
	}
	
	public double getEventRateMale() {
		if (getPdoMale() > 0)
			return ((getNumeratorTotal() * 1000 * 365.25) / getPdoMale());
		return 0;
	}
	
	public double getEventRateFemale() {
		if (getPdoFemale() > 0)
			return ((getNumeratorTotal() * 1000 * 365.25) / getPdoFemale());
		return 0;
	}
	
	public double getEventRateTotal() {
		return getEventRateMale() + getEventRateFemale();
	}
	
	public double getMalePercent() {
		if (getDenominatorTotal() > 0) {
			return ((100 * getNumeratorMale()) / getDenominatorTotal());
		}
		return 0;
	}
	
	public double getFemalePercent() {
		if (getDenominatorTotal() > 0) {
			return ((100 * getNumeratorFemale()) / getDenominatorTotal());
		}
		return 0;
	}
	
	public double getMaleAndFemalePercent() {
		if (getDenominatorTotal() > 0) {
			return ((100 * (getNumeratorTotal())) / getTotal());
		}
		return 0;
	}
	
	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
	
	public double getMin() {
		return min;
	}
	
	public void setMin(double min) {
		this.min = min;
	}
	
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
