package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.util.CalendarAdapter;

@Description(description="An OutMigration represents a migration out of the study area. " +
		"It contains information about the Individual who is out-migrating to a particular " +
		"Residency. It also contains information about the destination, date, and reason the " +
		"Indiviudal is migrating as well as the Visit that is associated with the migration. ")
@Entity
@Table(name="outmigration")
@XmlRootElement(name = "outmigration")
public class OutMigration extends AuditableCollectedEntity implements Serializable {

    private static final long serialVersionUID = 6736599408170070468L;
        
    @Searchable
    @NotNull
    @CheckEntityNotVoided
    @CheckIndividualNotUnknown
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Individual.class)
    @Description(description="Individual who is outmigrating, identified by external id.")
    Individual individual;
   
    @NotNull
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Residency.class)
    @Description(description="Residency the individual is outmigrating to.")
    Residency residency;
   
    @CheckFieldNotBlank
    @Searchable
    @Description(description="Destination of the outmigration.")
    String destination;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="Reason for outmigrating.")
    String reason;
   
    @Searchable
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Visit.class)
    @Description(description="Visit associated with the outmigration, identified by external id.")
    Visit visit;
   
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    @Past
    @Description(description="Recorded date of the outmigration.")
    Calendar recordedDate;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Residency getResidency() {
        return residency;
    }

    public void setResidency(Residency residency) {
        this.residency = residency;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Calendar getRecordedDate() {
        return recordedDate;
    }

    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public void setRecordedDate(Calendar recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }
}
