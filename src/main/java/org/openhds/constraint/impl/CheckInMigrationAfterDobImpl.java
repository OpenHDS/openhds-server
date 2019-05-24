package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckInMigrationAfterDob;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;

public class CheckInMigrationAfterDobImpl implements ConstraintValidator<CheckInMigrationAfterDob, InMigration> {

	public void initialize(CheckInMigrationAfterDob constraintAnnotation) { }

	/**
	 * The in migration date should be after the individuals dob
	 */
	public boolean isValid(InMigration value, ConstraintValidatorContext context) {
		Individual indiv = value.getIndividual();
		
		if (value.getRecordedDate().compareTo(indiv.getDob()) > 0) {
			return true;
		}
		
		return false;
	}
}
