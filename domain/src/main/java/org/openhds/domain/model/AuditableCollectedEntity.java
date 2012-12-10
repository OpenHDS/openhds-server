package org.openhds.domain.model;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.openhds.domain.annotations.Description;

/**
 * An AuditableCollectedEntity is any entity that is recorded or collected by a Field Worker
 * that needs to be audited
 */
@MappedSuperclass
public abstract class AuditableCollectedEntity extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3558979775991767767L;

    @Description(description="Status of the data.")
    protected String status;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=FieldWorker.class)
    @Description(description="The field worker who collected the data, identified by external id.")
    protected FieldWorker collectedBy;
    
    @Description(description="Error message if the entity failed validation.")
    protected String statusMessage;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FieldWorker getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(FieldWorker collectedBy) {
		this.collectedBy = collectedBy;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
