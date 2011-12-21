package org.openhds.domain.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.extensions.ExtensionConstraint;

@Description(description="All distinct Locations within the area of study are " +
		"represented here. A Location is identified by a uniquely generated " +
		"identifier that the system uses internally. Each Location has a name associated " +
		"with it and resides at a particular level within the Location Hierarchy.")
@Entity
@Table(name="location")
public class Location extends AuditableCollectedEntity implements Serializable {

    private static final long serialVersionUID = 169551578162260199L;
               
    @NotNull
    @CheckFieldNotBlank
    @Searchable
    @Description(description="External Id of the location. This id is used internally.")
    String extId;
   
    @CheckFieldNotBlank
    @Searchable
    @Description(description="Name of the location.")
    String locationName;
   
    @Description(description="Level of the location in the hierarchy.")
    @ManyToOne
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    LocationHierarchy locationLevel = new LocationHierarchy();
    
    @ExtensionConstraint(constraint="locationTypeConstraint", message="Invalid Value for location type",allowNull=true)
    @Description(description="The type of Location.")
    String locationType;
    
	@OneToMany(targetEntity=Residency.class)
	@JoinColumn(name = "location_uuid")
	List<Residency> residencies; 
               
	public String getLocationName() {
        return locationName;
    }

	public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getExtId() {
        return this.extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public LocationHierarchy getLocationLevel() {
		return locationLevel;
	}

	public void setLocationLevel(LocationHierarchy locationLevel) {
		this.locationLevel = locationLevel;
	}
	
	@Override
    public String toString() {
        return locationName;
    }
	
    public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public void setResidencies(List<Residency> residencies) {
		this.residencies = residencies;
	}

	public List<Residency> getResidencies() {
		return residencies;
	}
}
