package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckDropdownMenuItemSelected;
import org.openhds.domain.InMigration;

public class CheckDropdownMenuItemSelectedImpl implements ConstraintValidator<CheckDropdownMenuItemSelected, InMigration> {

	public void initialize(CheckDropdownMenuItemSelected arg0) {	}

	public boolean isValid(InMigration arg0, ConstraintValidatorContext arg1) {
				
		if (arg0.getReason().equals("BLANK")) {
			return false;
		}
		
		return true;
	}

	
}
