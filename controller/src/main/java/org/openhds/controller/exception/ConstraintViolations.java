package org.openhds.controller.exception;

import java.util.List;

public class ConstraintViolations extends Exception {

	private static final long serialVersionUID = 4392790814928552607L;
	private List<String> violations;
	
	public ConstraintViolations(String msg, List<String> violations) {
		super(msg);
		this.violations = violations;
	}
	
	public ConstraintViolations(String msg) {
		super(msg);
	}

	public List<String> getViolations() {
		return violations;
	}
}
