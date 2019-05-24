package org.openhds.constraint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.constraint.CheckIndividualAge;
import org.openhds.constraint.CheckRelationshipAge;
import org.openhds.domain.Individual;
import org.openhds.service.impl.SitePropertiesServiceImpl;

public class CheckRelationshipAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckRelationshipAge, Individual> {
	
	public void initialize(CheckRelationshipAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfMarriage();
	}
}
