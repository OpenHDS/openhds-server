package org.openhds.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.exception.ConstraintViolations;

@XmlRootElement(name="failure")
public class WebServiceCallException implements Serializable {
	
    private static final long serialVersionUID = -5429038867393148120L;
    private List<String> errors = new ArrayList<String>();

	public WebServiceCallException() {   		
	}
	
	public WebServiceCallException(ConstraintViolations violations) {
		for(String violation : violations.getViolations()) {
			errors.add(violation);
		}
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
