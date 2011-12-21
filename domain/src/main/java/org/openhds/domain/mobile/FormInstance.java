package org.openhds.domain.mobile;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@XmlRootElement(name = "form-instance")
@Entity
public class FormInstance {
	
	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
	private String uuid;
	
	String formInstanceId;
	
	@OneToMany
	private List<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFormInstanceId() {
		return formInstanceId;
	}

	public void setFormInstanceId(String formInstanceId) {
		this.formInstanceId = formInstanceId;
	}

	@XmlElement(name = "validation-message")
	public List<ValidationMessage> getValidationMessages() {
		return validationMessages;
	}

	public void setValidationMessages(List<ValidationMessage> validationMessages) {
		this.validationMessages = validationMessages;
	}
}
