package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckDeathDateGreaterThanBirthDate;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.Searchable;

@Description(description="A Death represents the final event than an Individual can " +
		"have within the system. It consists of the Individual who has passed on, the " +
		"Visit associated with the Death, as well as descriptive information about the " +
		"occurrence, cause, and date of the death. If the Individual had any Residencies, " +
		"Relationships, or Memberships then they will become closed.")
@CheckDeathDateGreaterThanBirthDate
@Entity
@Table(name="death")
//@XmlRootElement(name = "death")
public class Death extends AuditableCollectedEntity implements Serializable {
    private static final long serialVersionUID = -6644256636909420061L;
	    
    @Searchable
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @CheckIndividualNotUnknown
	@Description(description="Individual who has died, identified by the external id.")
    Individual individual;
	
    @CheckFieldNotBlank
    @Searchable
	@Description(description="Place where the death occurred.")
    public String deathPlace;
    
    @CheckFieldNotBlank
    @Searchable
	@Description(description="Cause of the death.")
    public String deathCause;
    
    @NotNull
    @Past(message = "Start date should be in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of the death.")
    Calendar deathDate;
    
    @Searchable
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Description(description="Visit associated with the death, identified by external id.")
    Visit visitDeath;
    
    @Description(description="Age of death in number of days.")
    public Long ageAtDeath;
            
    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getDeathCause() {
        return deathCause;
    }

    public void setDeathCause(String deathCause) {
        this.deathCause = deathCause;
    }

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Calendar deathDate) {
        this.deathDate = deathDate;
    }

    public Visit getVisitDeath() {
        return visitDeath;
    }

    public void setVisitDeath(Visit visitDeath) {
        this.visitDeath = visitDeath;
    }
    
    public Long getAgeAtDeath() {
		return ageAtDeath;
	}

	public void setAgeAtDeath(Long ageAtDeath) {
		this.ageAtDeath = ageAtDeath;
	}
    
    @Override
    public String toString() {
        return "Death [deathCause=" + deathCause + ", deathDate=" + deathDate
                + ", deathPlace=" + deathPlace + ", individual=" + individual
                + "]";
    }
}