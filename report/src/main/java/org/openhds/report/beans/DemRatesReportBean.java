package org.openhds.report.beans;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Ime Asangansi
 *	holds parameters and variables for the demographic rates reports
 */
public class DemRatesReportBean {

	// report parameters put into the map
	String reportFormat = "xls";  // xls, pdf, html, csv
	Boolean isMidPoint; // false means the entire duration (person days) is used
	Calendar firstDate = new GregorianCalendar();
	Calendar lastDate = new GregorianCalendar();
	String denomTypeString;
	String eventType;
	
	/**
	 * @return the reportFormat
	 */
	public String getReportFormat() {
		return reportFormat;
	}
	/**
	 * @param reportFormat the reportFormat to set
	 */
	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}
	/**
	 * @return the isMidPoint
	 */
	public Boolean getIsMidPoint() {
		return isMidPoint;
	}
	/**
	 * @param isMidPoint the isMidPoint to set
	 */
	public void setIsMidPoint(Boolean isMidPoint) {
		this.isMidPoint = isMidPoint;
	}
	/**
	 * @return the firstDate
	 */
	public Calendar getFirstDate() {
		return firstDate;
	}
	/**
	 * @param firstDate the firstDate to set
	 */
	public void setFirstDate(Calendar firstDate) {
		this.firstDate = firstDate;
	}
	/**
	 * @return the lastDate
	 */
	public Calendar getLastDate() {
		return lastDate;
	}
	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(Calendar lastDate) {
		this.lastDate = lastDate;
	}
	/**
	 * @return the denomTypeString
	 */
	public String getDenomTypeString() {
		return denomTypeString;
	}
	/**
	 * @param denomTypeString the denomTypeString to set
	 */
	public void setDenomTypeString(String denomTypeString) {
		this.denomTypeString = denomTypeString;
	}
	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}
	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

}
