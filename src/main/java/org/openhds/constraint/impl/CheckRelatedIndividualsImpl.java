package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckRelatedIndividuals;
import org.openhds.domain.Relationship;

public class CheckRelatedIndividualsImpl implements ConstraintValidator<CheckRelatedIndividuals, Relationship> {

	public void initialize(CheckRelatedIndividuals arg0) {	}

	public boolean isValid(Relationship relationship, ConstraintValidatorContext arg1) {

		if (relationship.getIndividualA().getExtId().equals(relationship.getIndividualB().getExtId()))
			return false;
		return true;
	}
}
