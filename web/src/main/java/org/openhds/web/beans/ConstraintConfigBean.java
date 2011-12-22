package org.openhds.web.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import org.openhds.domain.extensions.ValueConstraintService;
import org.openhds.web.service.JsfService;

public class ConstraintConfigBean {
	
	String constraintId;
	String description;
	String value;
	
	UIInput constraintText = null;
	UIInput descriptionText = null;
	UIInput valueText = null;

	Map<String, String> constraints = new HashMap<String, String>();
	List<ExtensionConstraintBean> extensions = new ArrayList<ExtensionConstraintBean>();

	JsfService jsfService;
	ValueConstraintService valueConstraintService;
	
	public ConstraintConfigBean(JsfService jsfService, ValueConstraintService valueConstraintService) {
		this.jsfService = jsfService;
		this.valueConstraintService = valueConstraintService;	
		
		initExtensions();
	}
	
	public void clearFields() {
		HtmlInputText cText = (HtmlInputText) constraintText;
		HtmlInputText dText = (HtmlInputText) descriptionText;
		HtmlInputText vText = (HtmlInputText) valueText;
		cText.setValue("");
		dText.setValue("");
		vText.setValue("");
	}
	
	public void setup() {
		constraints.clear();
		clearFields();
		
		HtmlInputText cText = (HtmlInputText) constraintText;
		cText.setDisabled(false);
	}
	
	public void addValue() {
		constraints.put(description, value);
		
		HtmlInputText cText = (HtmlInputText) constraintText;
		HtmlInputText dText = (HtmlInputText) descriptionText;
		HtmlInputText vText = (HtmlInputText) valueText;
		cText.setDisabled(true);
		dText.setValue("");
		vText.setValue("");
	}
	
	public void clear() {
		constraints.clear();
		clearFields();
		
		HtmlInputText cText = (HtmlInputText) constraintText;
		cText.setDisabled(false);
	}
	
	public List<String> getConstraintKeys() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(constraints.keySet());
		return keys;
	}
	
	private void initExtensions() {
		List<String> names = valueConstraintService.getAllConstraintNames();
		for (String name : names) {
			ExtensionConstraintBean extBean = new ExtensionConstraintBean();	
			extBean.setConstraintId(name);		
			extBean.setConstraintMap(valueConstraintService.getMapForConstraint(name));
			extensions.add(extBean);
		}
	}

	public String getConstraintId() {
		return constraintId;
	}
	
	public void setConstraintId(String constraintId) {
		this.constraintId = constraintId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Map<String, String> getConstraints() {
		return constraints;
	}
	
	public void setConstraints(Map<String, String> constraints) {
		this.constraints = constraints;
	}
	
	public List<ExtensionConstraintBean> getExtensions() {
		return extensions;
	}
	
	public UIInput getConstraintText() {
		return constraintText;
	}

	public void setConstraintText(UIInput constraintText) {
		this.constraintText = constraintText;
	}

	public UIInput getDescriptionText() {
		return descriptionText;
	}

	public void setDescriptionText(UIInput descriptionText) {
		this.descriptionText = descriptionText;
	}

	public UIInput getValueText() {
		return valueText;
	}

	public void setValueText(UIInput valueText) {
		this.valueText = valueText;
	}
}
