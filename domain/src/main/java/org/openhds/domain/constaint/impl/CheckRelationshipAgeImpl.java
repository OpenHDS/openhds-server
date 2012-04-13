package org.openhds.domain.constaint.impl;

import javax.validation.ConstraintValidator;

import org.openhds.domain.constraint.CheckIndividualAge;
import org.openhds.domain.constraint.CheckRelationshipAge;
import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckRelationshipAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckRelationshipAge, Individual> {
	
	public void initialize(CheckRelationshipAge arg0) {
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfMarriage();
	}
}
