package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEndDateNotBeforeStartDate;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.GenericStartEndDateConstraint;
import org.openhds.domain.constraint.Searchable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Description(description="A Residency represents a home within the study area. " +
		"It contains information about the Individual who lives at the Residency " +
		"which is tied to a particular Location. It also contains information about " +
		"the date the Residency started and ended as well as the start and end types.")
@Entity
@CheckEndDateNotBeforeStartDate(allowNull=true)
@Table(name="residency")
@XmlRootElement
@JsonInclude(Include.NON_NULL)
public class Residency extends AuditableCollectedEntity implements GenericStartEndDateConstraint, Serializable {
	private static final long serialVersionUID = -4666666231598767965L;
    
	@Searchable
    @ManyToOne
    @Description(description="Individual who resides at this residency, identified by external id.")
    Individual individual;
    
	@Searchable
    @ManyToOne
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
