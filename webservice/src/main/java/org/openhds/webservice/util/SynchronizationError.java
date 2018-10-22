package org.openhds.webservice.util;

import java.util.List;

import org.openhds.controller.exception.*;

public class SynchronizationError {
	private String entityType;
	private String fieldworkerExtId;
	private String entityId;
	private List<String> violations;
	
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public String getFieldworkerExtId() {
		return fieldworkerExtId;
	}
	
	public void setFieldworkerExtId(String fieldworkerExtId) {
		this.fieldworkerExtId = fieldworkerExtId;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public List<String> getViolations() {
		return violations;
	}
	
	public void setViolations(List<String> violations) {
		this.violations = violations;
	}
	
	
}
