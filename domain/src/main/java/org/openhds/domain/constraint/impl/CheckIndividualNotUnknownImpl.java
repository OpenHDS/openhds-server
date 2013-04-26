package org.openhds.domain.constraint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckIndividualNotUnknownImpl extends AppContextAware implements ConstraintValidator<CheckIndividualNotUnknown, Individual> {

	private SitePropertiesServiceImpl properties;
	
	public void initialize(CheckIndividualNotUnknown constraintAnnotation) {
		properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
	}

	public boolean isValid(Individual value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		if (value.getExtId().equals(properties.getUnknownIdentifier())) {
			return false;
		}
		
		return true;
	}
}
