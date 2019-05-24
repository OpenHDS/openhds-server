package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.constraint.CheckHouseholdHeadAge;
import org.openhds.constraint.CheckIndividualAge;
import org.openhds.domain.Individual;
import org.openhds.service.impl.SitePropertiesServiceImpl;

public class CheckHouseholdHeadAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckHouseholdHeadAge, Individual> {
	
	public void initialize(CheckHouseholdHeadAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfHouseholdHead();
		this.allowNull = arg0.allowNull();
	}
}
