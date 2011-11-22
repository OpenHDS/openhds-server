package org.openhds.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;

@Description(description="The Whitelist contains information about the " +
		"IP Addresses which are allowed to access the system's webservices.")
@Entity
@Table(name="whitelist")
public class Whitelist implements Serializable {

	private static final long serialVersionUID = -1714824195686095050L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    String uuid;
    
	@Searchable
    @CheckFieldNotBlank
    @Description(description="The address to approve.")
	String address;
		
    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
