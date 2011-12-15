package org.openhds.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckInteger;
import org.openhds.domain.constraint.Searchable;

@Description(description="A Visit represents a Field Worker's observation " +
		"of a specific Location within the study area at a particular date. It " +
		"can be identified by a uniquely generated identifier which the system " +
		"uses internally.")
@Entity
@Table(name="visit")
public class Visit extends AuditableCollectedEntity implements Serializable {

    private static final long serialVersionUID = -211408757055967973L;
   
    @Searchable
    @Description(description="External Id of the visit. This id is used internally.")
    String extId;

    @Searchable
    @ManyToOne
    @Description(description="Location that this visit is for.")
    Location visitLocation;
    
    @NotNull
    @Past(message = "Visit date should be in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of the visit.")
    Calendar visitDate;
    
	@CheckInteger(min=1)
	@Description(description="Round number for the visit.")
	public Integer roundNumber;
	
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy="entity")
    @Description(description="The assigned extension and their values specific for this entity.")
    List<Extension> extensions = new ArrayList<Extension>();
   
	public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getExtId() {
        return extId;
    }

    public Location getVisitLocation() {
        return visitLocation;
    }

    public void setVisitLocation(Location visitLocation) {
        this.visitLocation = visitLocation;
    }

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getVisitDate() {
        return this.visitDate;
    }

    public void setVisitDate(Calendar visitDate) {
        this.visitDate = visitDate;
    }

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}
	
	public List<Extension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}
}
