package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckIndividualNotUnknownImpl extends AppContextAware implements ConstraintValidator<CheckIndividualNotUnknown, Individual> {

	private SiteProperties properties;
	
	public void initialize(CheckIndividualNotUnknown constraintAnnotation) {
		properties = (SiteProperties)context.getBean("siteProperties");
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
