package org.openhds.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.openhds.annotations.Description;
import org.openhds.constraint.CheckFieldNotBlank;
import org.openhds.constraint.Searchable;

@Description(description="The Location Hierarchy Level represents the specific " +
		"part of the Location Hierarchy that a Location resides in. The levels are " +
		"used in the configuration of the Location Hierarchy. Sample levels could be  " +
		"Region, District, Village. ")
@Entity
@Table(name="locationhierarchylevel")
public class LocationHierarchyLevel implements Serializable {
	
	private static final long serialVersionUID = -1070569257732332545L;

	@Id
	String uuid;
	
	@Description(description="A key to identify this level, assign 1, 2, 3, ... etc")
    int keyIdentifier;
	
	@NotNull
	@CheckFieldNotBlank
	@Searchable
	@Description(description="The name of this location hierarchy level.")
    String name;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public int getKeyIdentifier() {
		return keyIdentifier;
	}

	public void setKeyIdentifier(int keyIdentifier) {
		this.keyIdentifier = keyIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
