package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckFieldNotBlankImpl implements ConstraintValidator<CheckFieldNotBlank, String> {

	public void initialize(CheckFieldNotBlank arg0) {	}

	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if (arg0 == null) {
			return false;
		}
		
		if (arg0.trim().length() == 0) {
			return false;
		}	
		return true;
	}

}
