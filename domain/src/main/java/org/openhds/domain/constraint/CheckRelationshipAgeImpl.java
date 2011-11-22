package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;

import org.openhds.domain.model.Individual;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckRelationshipAgeImpl extends CheckIndividualAge implements ConstraintValidator<CheckRelationshipAge, Individual> {
	
	public void initialize(CheckRelationshipAge arg0) {
		SiteProperties properties = (SiteProperties)context.getBean("siteProperties");
		requiredAge = properties.getMinimumAgeOfMarriage();
	}
}
