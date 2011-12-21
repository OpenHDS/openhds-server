package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckInMigrationAfterDob;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.util.CalendarAdapter;

@Description(description="An InMigration represents a migration into the study area. " +
		"It contains information about the Individual who is in-migrating to a particular " +
		"Residency. It also contains information about the origin, date, and reason the " +
		"Indiviudal is migrating as well as the Visit that is associated with the migration.")
@Entity
@CheckInMigrationAfterDob
@Table(name="inmigration")
@XmlRootElement(name = "inmigration")
public class InMigration extends AuditableCollectedEntity implements Serializable {
	private static final long serialVersionUID = 7889700709284952892L;
    
	@Searchable
    @NotNull
    @CheckIndividualNotUnknown
    @ManyToOne
    @Description(description="The individual who is inmigrating, identified the by external id.")
    Individual individual;
    
    @OneToOne
    @NotNull
    @Description(description="The residency the individual is inmigrating to.")
    Residency residency = new Residency();
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="The origin of the inmigration.")
    String origin;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="The reason for inmigrating.")
    String reason;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Recorded date of the inmigration.")
    Calendar recordedDate;
    
    @Searchable
    @NotNull
    @ManyToOne
    @Description(description="Visit associated with the inmigration, identified by external id.")
    Visit visit;

    @Enumerated(EnumType.STRING)
    @Description(description="The migration type.")
    MigrationType migType = MigrationType.INTERNAL_INMIGRATION;    
    
    @Description(description="Flag for indicating if the individual who is inmigrating is known or not.")
    Boolean unknownIndividual = false;
    
    public Residency getResidency() {
        return residency;
    }

    public void setResidency(Residency residency) {
        this.residency = residency;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Calendar recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public MigrationType getMigType() {
		return migType;
	}

	public void setMigType(MigrationType migType) {
		this.migType = migType;
	}
	
	
	public void setMigTypeInternal(){
		this.setMigType(MigrationType.INTERNAL_INMIGRATION);
	}
	
	public void setMigTypeExternal(){
		this.setMigType(MigrationType.EXTERNAL_INMIGRATION);
	}

	public Boolean isUnknownIndividual() {
		return unknownIndividual;
	}

	public void setUnknownIndividual(Boolean unknownIndividual) {
		this.unknownIndividual = unknownIndividual;
	}
}
