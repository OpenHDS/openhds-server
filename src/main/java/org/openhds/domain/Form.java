package org.openhds.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.annotations.Description;
import org.openhds.constraint.CheckFieldNotBlank;
import org.openhds.constraint.Searchable;

@Description(description="Forms available.")
@Entity
@Table(name="form")
@XmlRootElement
public class Form extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 1022315305801238563L;

	
	@Searchable
	@CheckFieldNotBlank
	@Description(description="Active/Not Active.")
	String active;

	@Searchable
	@CheckFieldNotBlank
	@Description(description="Indicate if a form is defined for a gender in particular or not.")
	String gender;
	
	
	@Searchable
	@CheckFieldNotBlank
	@Description(description="Name of the form.")
	String formName;

	@Description(description="Name of the ODK core table.")
	String coreTable;

	@Description(description="Indicator for signaling the status of the form.")
    protected int status = 0;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}


	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getCoreTable() {
		return coreTable;
	}

	public void setCoreTable(String coreTable) {
		this.coreTable = coreTable;
	}
}
