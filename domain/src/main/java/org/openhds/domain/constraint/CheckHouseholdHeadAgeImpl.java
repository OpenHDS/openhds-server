package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckHouseholdHeadAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckHouseholdHeadAge, Individual> {
	
	public void initialize(CheckHouseholdHeadAge arg0) {
		SiteProperties properties = (SiteProperties)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfHouseholdHead();
		this.allowNull = arg0.allowNull();
	}
}
