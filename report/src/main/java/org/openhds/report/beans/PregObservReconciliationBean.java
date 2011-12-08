package org.openhds.report.beans;

/**
 * JavaBean used for the Pregnancy Observation Reconciliation Report
 */
public class PregObservReconciliationBean {
	
	String indivId;
	String currentDate;
	String date;
	
	public String getIndivId() {
		return indivId;
	}
	
	public void setIndivId(String indivId) {
		this.indivId = indivId;
	}
	
	public String getCurrentDate() {
		return currentDate;
	}
	
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}
