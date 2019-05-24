package org.openhds.domain;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.GenericGenerator;
import org.openhds.annotations.Description;
import org.openhds.constraint.CheckEndDateNotBeforeStartDate;
import org.openhds.constraint.CheckInteger;
import org.openhds.constraint.GenericStartEndDateConstraint;

@Description(description="A Round represents a range of dates in which Visits " +
		"can take place.")
@Entity
@CheckEndDateNotBeforeStartDate
@Table(name="round")
@XmlRootElement
public class Round implements Serializable, GenericStartEndDateConstraint {

	private static final long serialVersionUID = 1022315305801238563L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
	String uuid;
	
	@CheckInteger(min=0)
	@Description(description="Round number for the study.")
	Integer roundNumber;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	@Description(description="Start date of the round.")
	Calendar startDate;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	@Description(description="End date of the round.")
	Calendar endDate;
	
	@Description(description="Remarks about the round.")
	String remarks;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	@XmlJavaTypeAdapter(org.openhds.util.CalendarAdapter.class)
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	@XmlJavaTypeAdapter(org.openhds.util.CalendarAdapter.class)
	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
