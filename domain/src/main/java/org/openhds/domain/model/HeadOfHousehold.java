package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;


@Description(description = "A Death represents the final event than an Individual can have within the system. It consists of the Individual who has passed on, the Visit associated with the Death, as well as descriptive information about the occurrence, cause, and date of the death. If the Individual had any Residencies, Relationships, or Memberships then they will become closed.")
//@CheckDeathDateGreaterThanBirthDate
@Entity
//@Table(name = "death")
@XmlRootElement(name = "death_of_hh")
//@XmlAccessorType(value = XmlAccessType.FIELD)
public class HeadOfHousehold
    extends AuditableCollectedEntity
    implements Serializable
{

    public final static long serialVersionUID = 4644236636908420061L;
    
//    @XmlTransient
    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = org.openhds.domain.model.Individual.class)
    private Individual oldHoh;
    
    @XmlTransient
    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = org.openhds.domain.model.Individual.class)
    private Individual newHoh;
    
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @Description(description="Visit that is associated with the pregnancy outcome, identified by the external id.")   
    private Visit visit;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @Description(description="FieldWorker that surveyed this event.")   
    private FieldWorker collectedBy;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description = "Place where the death occurred.")
    private String deathPlace;    
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description = "Cause of the death.")
    private String deathCause;   
    
    @NotNull(message = "You must provide a Death date")
    @Past(message = "Death date should be in the past")
    @Temporal(TemporalType.DATE)
    @Description(description = "Date of the Death.")
    private Calendar deathDate;    
    
    @OneToOne(cascade = CascadeType.MERGE)
    @Description(description = "Death of the HoH")
    private Death death;
    
    @OneToMany(cascade = CascadeType.MERGE)
    @Description(description = "Death of the HoH")
    private SocialGroup socialGroup;    
    
//    @XmlTransient
//    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = org.openhds.domain.model.wrappers.Individuals.class)
//    private Individuals householdMembers;
    
    @OneToMany(mappedBy = "individual")
    @Description(description = "The set of all memberships the individual is participating in.")
    private Set<Membership> memberships = new HashSet<Membership>();    
    
	public Individual getOldHoh() {
		return oldHoh;
	}
	public void setOldHoh(Individual oldHoh) {
		this.oldHoh = oldHoh;
	}
	public Individual getNewHoh() {
		return newHoh;
	}
	public void setNewHoh(Individual newHoh) {
		this.newHoh = newHoh;
	}
    
//	public Individuals getHouseholdMembers() {
//		return householdMembers;
//	}
//	public void setHouseholdMembers(Individuals householdMembers) {
//		this.householdMembers = householdMembers;
//	} 
	
    @XmlElementWrapper(name = "memberships")
    @XmlElement(name = "membership")
    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> list) {
    	memberships = list;
    }
    
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public FieldWorker getCollectedBy() {
		return collectedBy;
	}
	public void setCollectedBy(FieldWorker collectedBy) {
		this.collectedBy = collectedBy;
	}
	
    @XmlJavaTypeAdapter(org.openhds.domain.util.CalendarAdapter.class)
    public Calendar getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Calendar date) {
        deathDate = date;
    }
    
    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String place) {
        deathPlace = place;
    }

    public String getDeathCause() {
        return deathCause;
    }

    public void setDeathCause(String cause) {
        deathCause = cause;
    }
	public Death getDeath() {
		return death;
	}
	public void setDeath(Death death) {
		this.death = death;
	}
	public SocialGroup getSocialGroup() {
		return socialGroup;
	}
	public void setSocialGroup(SocialGroup socialGroup) {
		this.socialGroup = socialGroup;
	}
}
