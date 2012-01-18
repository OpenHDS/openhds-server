package org.openhds.report.beans;

public class ReportRecordBean {
	
	int denominatorMale;
	int denominatorFemale;
	
	int numeratorMale;
	int numeratorFemale;
	
	int numeratorTotal;
	int denominatorTotal;
	
	public ReportRecordBean() { }
	
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
	
	public int getDenominatorMale() {
		return denominatorMale;
	}

	public void setDenominatorMale(int denominatorMale) {
		this.denominatorMale = denominatorMale;
	}

	public int getDenominatorFemale() {
		return denominatorFemale;
	}

	public void setDenominatorFemale(int denominatorFemale) {
		this.denominatorFemale = denominatorFemale;
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
		return numeratorTotal;
	}

	public void setNumeratorTotal(int numeratorTotal) {
		this.numeratorTotal = numeratorTotal;
	}

	public int getDenominatorTotal() {
		return denominatorTotal;
	}

	public void setDenominatorTotal(int denominatorTotal) {
		this.denominatorTotal = denominatorTotal;
	}
}
