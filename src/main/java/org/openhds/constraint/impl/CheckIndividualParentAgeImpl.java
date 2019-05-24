package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.constraint.CheckIndividualAge;
import org.openhds.constraint.CheckIndividualParentAge;
import org.openhds.domain.Individual;
import org.openhds.service.impl.SitePropertiesServiceImpl;

public class CheckIndividualParentAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckIndividualParentAge, Individual> {

	public void initialize(CheckIndividualParentAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfParents();
	}
}
