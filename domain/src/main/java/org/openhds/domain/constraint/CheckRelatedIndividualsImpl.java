package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Relationship;

public class CheckRelatedIndividualsImpl implements ConstraintValidator<CheckRelatedIndividuals, Relationship> {

	public void initialize(CheckRelatedIndividuals arg0) {	}

	public boolean isValid(Relationship relationship, ConstraintValidatorContext arg1) {

		if (relationship.getIndividualA().getExtId().equals(relationship.getIndividualB().getExtId()))
			return false;
		return true;
	}
}
