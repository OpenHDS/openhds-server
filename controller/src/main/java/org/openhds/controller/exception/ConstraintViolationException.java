package org.openhds.controller.exception;

import java.util.List;

public class ConstraintViolationException extends Exception {

	private static final long serialVersionUID = 8223808741905469876L;
	private List<String> violations;

	public ConstraintViolationException(String msg, List<String> violations) {
		super(msg);
		this.violations = violations;
	}
	
	public List<String> getConstraintViolations() {
		return violations;
	}
}
