package org.openhds.report.beans;

public class ReportRecordBean {
	
	String ageGroupName;
	
	int denominatorMale;
	int denominatorFemale;
	
	int numeratorMale;
	int numeratorFemale;
	
	int numeratorTotal;
	int denominatorTotal;
	
	long pdoMale;
	long pdoFemale;
	long pdoTotal;
	
	long pyoMale;
	long pyoFemale;
	long pyoTotal;
	
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
	
	public void addDenominatorMale(long amount) {
		denominatorMale += amount;
	}
	
	public void addDenominatorFemale(long amount) {
		denominatorFemale += amount;
	}
	
	public void addNumeratorMale(long amount) {
		numeratorMale += amount;
	}
	
	public void addNumeratorFemale(long amount) {
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
	
	public long getPdoMale() {
		return pdoMale;
	}

	public void setPdoMale(long pdoMale) {
		this.pdoMale = pdoMale;
	}

	public long getPdoFemale() {
		return pdoFemale;
	}

	public void setPdoFemale(long pdoFemale) {
		this.pdoFemale = pdoFemale;
	}
	
	public long getPdoTotal() {
		return pdoTotal;
	}
	
	public long getPyoMale() {
		return pyoMale;
	}

	public long getPyoFemale() {
		return pyoFemale;
	}

	public void setPdoTotal() {
		pdoTotal = (long) ((pdoMale + pdoFemale) / 365.25);
	}
	
	public void calculatePyoMaleTotal() {
		if (pdoMale != 0) 
			pyoMale = (long) ((numeratorMale / pdoMale) * 1000 * 365.25);
	}
	
	public void calculatePyoFemaleTotal() {
		if (pdoFemale != 0) 
			 pyoFemale = (long) ((numeratorFemale / pdoFemale) * 1000 * 365.25);
	}
	
	public long getPyoTotal() {
		return pyoTotal;
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
}
