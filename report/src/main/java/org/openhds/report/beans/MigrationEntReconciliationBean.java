package org.openhds.report.beans;

/**
 * JavaBean used for the Migration Ent Reconciliation Report
 * @author Dave Roberge
 */
public class MigrationEntReconciliationBean {

	String indivId;
	String date;
	String name;
	String fieldWorker;
	
	public String getIndivId() {
		return indivId;
	}
	
	public void setIndivId(String indivId) {
		this.indivId = indivId;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFieldWorker() {
		return fieldWorker;
	}
	
	public void setFieldWorker(String fieldWorker) {
		this.fieldWorker = fieldWorker;
	}
}
