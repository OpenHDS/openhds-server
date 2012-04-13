package org.openhds.domain.constaint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.constraint.CheckInteger;

public class CheckIntegerImpl implements ConstraintValidator<CheckInteger, Object> {

	private CheckInteger checkInteger;

	public void initialize(CheckInteger arg0) {
		this.checkInteger = arg0;
	}

	public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
		if (arg0 == null) {
			return false;
		}
		try {
			int val = Integer.parseInt(arg0.toString());
			if (val >= checkInteger.min()) {
				return true;
			}
		} catch(Exception e) {
			
		}
		
		return false;
	}
}
