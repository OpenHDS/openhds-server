package org.openhds.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.annotations.Description;
import org.openhds.constraint.CheckFieldNotBlank;

@Entity
@Table(name="extension")
@Description(description="An Extension represents the value of the Class Extension it has " +
		"a reference to. This is helpful for assigning values for a target entity.")
public class Extension implements Serializable {

	private static final long serialVersionUID = -8961454872122692579L;
	
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    String uuid;
    
    @NotNull
    @Description(description="The entity external id that this extension refers to.")
    String entityExtId;
	    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Description(description="The target entity that this extension belongs to.")
	Visit entity;
	
	@ManyToOne
	@Description(description="The class extension that this extension belongs to.")
	ClassExtension classExtension;
    
	@NotNull
    @CheckFieldNotBlank
    @Description(description="The value of this extension.")
    String extensionValue;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

    public Visit getEntity() {
		return entity;
	}

	public void setEntity(Visit entity) {
		this.entity = entity;
	}
	
	public ClassExtension getClassExtension() {
		return classExtension;
	}

	public void setClassExtension(ClassExtension classExtension) {
		this.classExtension = classExtension;
	}

	public String getExtensionValue() {
		return extensionValue;
	}
	
	public void setExtensionValue(String extensionValue) {
		this.extensionValue = extensionValue;
	}
	
	public String getEntityExtId() {
		return entityExtId;
	}

	public void setEntityExtId(String entityExtId) {
		this.entityExtId = entityExtId;
	}
}
