package org.openhds.domain.constraint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.domain.constraint.CheckIndividualAge;
import org.openhds.domain.constraint.CheckIndividualParentAge;
import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckIndividualParentAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckIndividualParentAge, Individual> {

	public void initialize(CheckIndividualParentAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfParents();
	}
}
