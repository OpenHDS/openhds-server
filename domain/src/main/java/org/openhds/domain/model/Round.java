package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEndDateGreaterThanStartDate;
import org.openhds.domain.constraint.CheckInteger;
import org.openhds.domain.constraint.GenericStartEndDateConstraint;

@Description(description="A Round represents a range of dates in which Visits " +
		"can take place.")
@Entity
@CheckEndDateGreaterThanStartDate
@Table(name="round")
public class Round implements Serializable, GenericStartEndDateConstraint {

	private static final long serialVersionUID = 1022315305801238563L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
	String uuid;
	
	@CheckInteger(min=1)
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

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

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
