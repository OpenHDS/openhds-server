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

	public void setDenominatorMale(int denominatorMale) {
		this.denominatorMale += denominatorMale;
	}

	public int getDenominatorFemale() {
		return denominatorFemale;
	}

	public void setDenominatorFemale(int denominatorFemale) {
		this.denominatorFemale += denominatorFemale;
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
		return round(pdoMale, 2);
	}

	public void setPdoMale(double pdoMale) {
		this.pdoMale = pdoMale;
	}

	public double getPdoFemale() {
		return round(pdoFemale, 2);
	}

	public void setPdoFemale(double pdoFemale) {
		this.pdoFemale = pdoFemale;
	}
	
	public double getPyoMale() {
		if (getPdoMale() != 0) {
			double result = ((1000 * 365.25 * numeratorMale) / getPdoMale());
			return round(result, 2);
		}
		return 0;
	}

	public double getPyoFemale() {
		if (getPdoFemale() != 0) { 
			double result = ((1000 * 365.25 * numeratorFemale) / getPdoFemale());
			return round(result, 2);
		}
		return 0;
	}
	
	public double getPyoTotal() {
		if (getPdoMale() + getPdoFemale() > 0) {
			double result = (1000 * 365.25 * (numeratorMale + numeratorFemale)) / (getPdoMale() + getPdoFemale());
			return round(result, 2);
		}
	
		return 0;
	}
	
	public double getPyo() {
		return round((getPdoMale() + getPdoFemale()) / 365.25, 2);
	}
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

}
