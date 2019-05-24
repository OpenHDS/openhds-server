package org.openhds.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.annotations.Description;
import org.openhds.constraint.CheckEndDateNotBeforeStartDate;
import org.openhds.constraint.CheckFieldNotBlank;
import org.openhds.constraint.GenericStartEndDateConstraint;
import org.openhds.constraint.Searchable;
import org.openhds.util.CalendarAdapter;

@Description(description="A Residency represents a home within the study area. " +
		"It contains information about the Individual who lives at the Residency " +
		"which is tied to a particular Location. It also contains information about " +
		"the date the Residency started and ended as well as the start and end types.")
@Entity
@CheckEndDateNotBeforeStartDate(allowNull=true)
@Table(name="residency")
@XmlRootElement(name="residency")
public class Residency extends AuditableCollectedEntity implements GenericStartEndDateConstraint, Serializable {
	private static final long serialVersionUID = -4666666231598767965L;
    
	@Searchable
    @ManyToOne
    @Description(description="Individual who resides at this residency, identified by external id.")
    Individual individual;
    
	@Searchable
    @ManyToOne(cascade = CascadeType.MERGE)
    @Description(description="Location of the residency, identified by external id.")
    Location location;
    
    @NotNull
    @Past(message = "Insert date should be in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Residency start date.")
    Calendar startDate;
    
    @CheckFieldNotBlank
    @Description(description="Residency start type.")
    String startType;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Residency end date.")
    Calendar endDate;
    
    @Description(description="Residency end type.")
    String endType;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
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
    
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public String getStartType() {
		return startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

	public String getEndType() {
		return endType;
	}
  
	public void setEndType(String endType) {
		this.endType = endType;
	}
}
