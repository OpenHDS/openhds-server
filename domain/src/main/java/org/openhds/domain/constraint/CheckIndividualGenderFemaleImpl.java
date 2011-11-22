package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckIndividualGenderFemaleImpl extends AppContextAware implements ConstraintValidator<CheckIndividualGenderFemale, Individual> {

	private boolean allowNull;
	private SiteProperties properties;

	public void initialize(CheckIndividualGenderFemale arg0) {
		properties = (SiteProperties)context.getBean("siteProperties");
		this.allowNull = arg0.allowNull();
	}

	public boolean isValid(Individual arg0, ConstraintValidatorContext arg1) {
				
		if (allowNull && arg0 == null) {
			return true;
		}
		
		if (arg0.getExtId().equals(properties.getUnknownIdentifier()))
			return true;
		
		if (arg0.getGender().equals(properties.getFemaleCode()))
			return true;
	
		
		return false;
	}
}