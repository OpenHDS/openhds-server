package org.openhds.report.beans;

public class ReportRecordBean {
	
	String ageGroupName;
	
	int denominatorMale;
	int denominatorFemale;
	
	int numeratorMale;
	int numeratorFemale;
	
	int numeratorTotal;
	int denominatorTotal;
	
	double pdoMale;
	double pdoFemale;
	double pdoTotal;
	
	double pyoMale;
	double pyoFemale;
	double pyoTotal;
	
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
	
	public double getPdoTotal() {
		return round(pdoTotal, 2);
	}
	
	public double getPyoMale() {
		return round(pyoMale, 2);
	}

	public double getPyoFemale() {
		return round(pyoFemale, 2);
	}

	public void setPdoTotal() {
		pdoTotal = ((denominatorMale + denominatorFemale) / 365.25);
	}
	
	public void calculatePyoMaleTotal() {
		if (denominatorMale != 0) 
			pyoMale = ((1000 * 365.25 * numeratorMale) / denominatorMale);
		else
			pyoMale = 0;
	}
	
	public void calculatePyoFemaleTotal() {
		if (denominatorFemale != 0) 
			 pyoFemale = ((1000 * 365.25 * numeratorFemale) / denominatorFemale);
	}
	
	public double getPyoTotal() {
		return round(pyoTotal, 2);
	}

	public void setPyoTotal() {
		pyoTotal = pyoMale + pyoFemale;
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
