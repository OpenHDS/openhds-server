package org.openhds.report.beans;

public class ReportRecordBean {
	
	String ageGroupName;
	
	int denominatorMale;
	int denominatorFemale;
	
	int numeratorMale;
	int numeratorFemale;
	
	double pdoMale;
	double pdoFemale;
	
	int min;
	int max;
	
	public ReportRecordBean(String ageGroupName, int min, int max) {
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
	
	public String getAgeGroupName() {
		return ageGroupName;
	}

	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}
	
	public int getDenominatorMale() {
		return denominatorMale;
	}

	public int getDenominatorFemale() {
		return denominatorFemale;
	}

	public int getNumeratorMale() {
		return numeratorMale;
	}

	public void setNumeratorMale(int numeratorMale) {
		this.numeratorMale = numeratorMale;
	}

	public int getNumeratorFemale() {
		return numeratorFemale;
	}

	public void setNumeratorFemale(int numeratorFemale) {
		this.numeratorFemale = numeratorFemale;
	}
	
	public int getNumeratorTotal() {
		return numeratorMale + numeratorFemale;
	}

	public int getDenominatorTotal() {
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
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

}
