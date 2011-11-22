package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckIndividualGenderMaleImpl extends AppContextAware implements ConstraintValidator<CheckIndividualGenderMale, Individual> {

	private SiteProperties properties;
	private boolean allowNull;

	public void initialize(CheckIndividualGenderMale arg0) {
		properties = (SiteProperties)context.getBean("siteProperties");
		this.allowNull = arg0.allowNull();
	}

	public boolean isValid(Individual arg0, ConstraintValidatorContext arg1) {		
			
		if (allowNull && arg0 == null) {
			return true;
		}
		
		if (arg0.getExtId().equals(properties.getUnknownIdentifier()))
			return true;
		
		if (arg0.getGender().equals(properties.getMaleCode()))
			return true;
		
		return false;
	}
}