package org.openhds.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;

@Description(description="The Location Hierarchy Level represents the specific " +
		"part of the Location Hierarchy that a Location resides in. The levels are " +
		"used in the configuration of the Location Hierarchy. Sample levels could be  " +
		"Region, District, Village. ")
@Entity
@Table(name="locationhierarchylevel")
public class LocationHierarchyLevel implements Serializable {
	
	private static final long serialVersionUID = -1070569257732332545L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
	String uuid;
	
	@Description(description="A key to identify this level, assign 1, 2, 3, ... etc")
    public int keyIdentifier;
	
	@NotNull
	@CheckFieldNotBlank
	@Searchable
	@Description(description="The name of this location hierarchy level.")
    public String name;

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
