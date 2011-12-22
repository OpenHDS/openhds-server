package org.openhds.web.beans;

import org.openhds.web.service.JsfService;

public class ExtensionConfigBean {
	
	String entityType;
	String attributeName;
	String attributeDescription;
	String attributeType;
	String attributeConstraint;
	
	JsfService jsfService;

	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public String getAttributeDescription() {
		return attributeDescription;
	}
	
	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}
	
	public String getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	
	public String getAttributeConstraint() {
		return attributeConstraint;
	}
	
	public void setAttributeConstraint(String attributeConstraint) {
		this.attributeConstraint = attributeConstraint;
	}
	
	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
}
