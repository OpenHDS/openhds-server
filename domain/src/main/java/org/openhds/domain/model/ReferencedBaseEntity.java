package org.openhds.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that identifies any of the core entities by it's id. 
 * This is used in web service calls to retrieve a list
 * of entities by their ids.
 */
public class ReferencedBaseEntity {
	
	String extId;
	String type;
	Map<String, String> params = new HashMap<String, String>();

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
