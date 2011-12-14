package org.openhds.community.beans;

import java.util.ArrayList;
import java.util.List;

public class OrgUnit {
	
	String id;
	String name;
	String code;
	String level;
	
	List<OrgUnit> children = new ArrayList<OrgUnit>();
	
	public OrgUnit(String id, String name, String code, String level) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.level = level;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<OrgUnit> getChildren() {
		return children;
	}

	public void setChildren(List<OrgUnit> children) {
		this.children = children;
	}
}
