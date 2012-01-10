package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.extensions.ExtensionConstraint;
import org.openhds.domain.util.CalendarAdapter;

@Entity
@Table(name="demrates")
public class DemRates implements Serializable {

	private static final long serialVersionUID = 6509997449040720062L;

	@Id 
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(length=32)
	String uuid;
	
    @Description(description="Name for this Demographic Rate.")
	String name;
	
    @Description(description="Start date of analysis.")
	Calendar startDate;
	
    @Description(description="End date of analysis.")
	Calendar endDate;
    
    @Description(description="Event type for this Demographic Rate.")
    String event;
    
    @ExtensionConstraint(constraint="demRatesConstraint", message="Invalid Value for denominator", allowNull=false)
    @Description(description="Denominator for this Demographic Rate (PDO or Midpoint)")
    String denominator;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDenominator() {
		return denominator;
	}

	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
}
