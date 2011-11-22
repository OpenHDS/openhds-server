package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckIndividualParentAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckIndividualParentAge, Individual> {

	public void initialize(CheckIndividualParentAge arg0) {
		SiteProperties properties = (SiteProperties)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfParents();
	}
}
