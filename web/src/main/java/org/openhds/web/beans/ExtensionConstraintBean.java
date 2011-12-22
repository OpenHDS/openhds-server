package org.openhds.web.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for converting xml entries into corresponding objects
 */
public class ExtensionConstraintBean {
	
	String constraintId;
	Map<String, String> constraintMap = new HashMap<String, String>();
	
	public List<String> getConstraintKeys() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(constraintMap.keySet());
		return keys;
	}
	
	public String getConstraintId() {
		return constraintId;
	}
	
	public void setConstraintId(String constraintId) {
		this.constraintId = constraintId;
	}
	
	public Map<String, String> getConstraintMap() {
		return constraintMap;
	}
	
	public void setConstraintMap(Map<String, String> constraintMap) {
		this.constraintMap = constraintMap;
	}
}
