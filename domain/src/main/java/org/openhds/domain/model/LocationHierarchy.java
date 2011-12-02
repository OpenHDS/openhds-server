package org.openhds.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;

@Description(description="The Location Hierarchy represents the overall structure " +
		"of all Locations within the study area. The levels of the hierarchy are " +
		"specified in a configuration file which may set the levels as follows: " +
		"Region, District, Village. Each record in this hierarchy will have " +
		"a uniquely generated identifier which the system uses internally. " +
		"Every record will also have a parent location name except the root. Finally, " +
		"all records within the hierarchy will have a name which must be unique. " +
		"Note that this is not to be confused with Location. The Location's name " +
		"field must reference a valid location name from this configured hierarchy.")
@Entity
@Table(name="locationhierarchy")
public class LocationHierarchy implements Serializable {
	
	private static final long serialVersionUID = -5334850119671675888L;
	
	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
	String uuid;
	
	@CheckFieldNotBlank
	@NotNull
	@Searchable
	@Description(description="External Id of the location hierarchy. This id is used internally.")
    String extId;
	
	@Description(description="Parent location's name.")
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = LocationHierarchy.class)
    LocationHierarchy parent;
		
	@NotNull
	@CheckFieldNotBlank
	@Searchable
	@Description(description="The name of this location hierarchy record.")
    String name;
	
	@Description(description="Parent level of the location hierarchy.")
	@ManyToOne
	LocationHierarchyLevel level;
	   	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getExtId() {
		return extId;
	}
	
	public void setExtId(String extId) {
		this.extId = extId;
	}
	
	public LocationHierarchy getParent() {
		return parent;
	}
	
	public void setParent(LocationHierarchy parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocationHierarchyLevel getLevel() {
		return level;
	}

	public void setLevel(LocationHierarchyLevel level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extId == null) ? 0 : extId.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LocationHierarchy))
			return false;
		LocationHierarchy other = (LocationHierarchy) obj;
		if (extId == null) {
			if (other.extId != null)
				return false;
		} else if (!extId.equals(other.extId))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
