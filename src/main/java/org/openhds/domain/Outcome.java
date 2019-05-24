package org.openhds.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.annotations.Description;
import org.openhds.constraint.CheckEntityNotVoided;
import org.openhds.constraint.CheckFieldNotBlank;
import org.openhds.constraint.CheckIndividualNotUnknown;

@Description(description="An Outcome represents a result from a Pregnancy. " +
		"The Outcome contains information about the child and Memberships " +
		"in which the child belongs. When a child is born, the Memberships " +
		"are obtained from the mother.")
@Entity
@Table(name="outcome")
public class Outcome implements Serializable {

	private static final long serialVersionUID = -1667849707971051732L;

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(length=32)
	private String uuid;
	
	@CheckFieldNotBlank
	@Description(description="Pregnancy outcome type.")
	private String type;
	
	 @Description(description = "External Id of the individual. This id is used internally.")
	 private String childextId;
	
	
	@OneToOne(cascade = {CascadeType.ALL})
	@CheckEntityNotVoided(allowNull=true)
    @CheckIndividualNotUnknown
	@Description(description="The child that of the pregnancy, identified by external id.")
	private Individual child;
	
	@OneToOne
	@Description(description="Membership of the child, which is obtained from the mother at birth.")
	private Membership childMembership;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Individual getChild() {
		return child;
	}

	public void setChild(Individual child) {
		this.child = child;
	}

	public Membership getChildMembership() {
		return childMembership;
	}

	public void setChildMembership(Membership childMembership) {
		this.childMembership = childMembership;
	}
	
	public String getChildextId() {
		return childextId;
	}

	public void setChildextId(String childextId) {
		this.childextId = childextId;
	}
}
