package org.openhds.domain.constaint.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.constraint.CheckGenderOfRelationship;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckGenderOfRelationshipImpl extends AppContextAware implements ConstraintValidator<CheckGenderOfRelationship, Relationship> {

	public void initialize(CheckGenderOfRelationship arg0) {	}

	public boolean isValid(Relationship relationship, ConstraintValidatorContext arg1) {
		
		SitePropertiesServiceImpl properties = (SitePropertiesServiceImpl)context.getBean("siteProperties");

		if (relationship.getIndividualA().getGender().equals(properties.getMaleCode()) && 
			relationship.getIndividualB().getGender().equals(properties.getFemaleCode()))
			return true;
		if (relationship.getIndividualA().getGender().equals(properties.getFemaleCode()) && 
			relationship.getIndividualB().getGender().equals(properties.getMaleCode()))
			return true;

		return false;
	}
}
