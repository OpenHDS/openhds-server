package org.openhds.domain.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ColumnDummy {
	private String name;
	private String type;
	private String size;
	private String allow_null;
	private String default_value;
	
	public String getName() {
		return name;
	}
	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	@XmlAttribute
	public void setSize(String size) {
		this.size = size;
	}
	public String getAllow_null() {
		return allow_null;
	}
	@XmlAttribute
	public void setAllow_null(String allow_null) {
		this.allow_null = allow_null;
	}
	public String getDefault_value() {
		return default_value;
	}
	@XmlAttribute
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
}