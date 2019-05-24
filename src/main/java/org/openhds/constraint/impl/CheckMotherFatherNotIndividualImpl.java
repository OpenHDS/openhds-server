package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckMotherFatherNotIndividual;
import org.openhds.domain.Individual;

public class CheckMotherFatherNotIndividualImpl implements ConstraintValidator<CheckMotherFatherNotIndividual, Individual> {

	public void initialize(CheckMotherFatherNotIndividual arg0) {	}

	public boolean isValid(Individual individual, ConstraintValidatorContext arg1) {			
	
		if (individual.getMother().getExtId().equals(individual.getExtId()) || 
				individual.getFather().getExtId().equals(individual.getExtId()))
				return false;
		return true;
	}
}