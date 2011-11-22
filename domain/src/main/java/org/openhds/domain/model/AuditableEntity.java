package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;

/**
 * An AuditableEntity can be any entity stored in the database that needs to be audited
 */
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {
	
	private static final long serialVersionUID = -4703049354466276068L;
	
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    String uuid;

	@ManyToOne(fetch=FetchType.LAZY)
	@Description(description="The user that voided the data.")
    protected User voidBy;
	
	@Description(description="Reason for voiding the data.")
    protected String voidReason;
	
	@Description(description="Indicator for signaling some data to be deleted.")
    protected boolean deleted = false;
	
	@Description(description="Date that the data was voided.")
    protected Calendar voidDate;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @Description(description="User who inserted the data.")
    protected User insertBy;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of insertion.")
    protected Calendar insertDate;
    
    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getVoidBy() {
		return voidBy;
	}

	public void setVoidBy(User voidBy) {
		this.voidBy = voidBy;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Calendar getVoidDate() {
		return voidDate;
	}

	public void setVoidDate(Calendar voidDate) {
		this.voidDate = voidDate;
	}

	public User getInsertBy() {
		return insertBy;
	}

	public void setInsertBy(User insertBy) {
		this.insertBy = insertBy;
	}

	public Calendar getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Calendar insertDate) {
		this.insertDate = insertDate;
	}
}
