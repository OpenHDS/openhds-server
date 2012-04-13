package org.openhds.domain.constaint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.domain.constraint.CheckHouseholdHeadAge;
import org.openhds.domain.constraint.CheckIndividualAge;
import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckHouseholdHeadAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckHouseholdHeadAge, Individual> {
	
	public void initialize(CheckHouseholdHeadAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfHouseholdHead();
		this.allowNull = arg0.allowNull();
	}
}
