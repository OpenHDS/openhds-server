package org.openhds.domain.constraint;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Membership;
import org.openhds.domain.model.MembershipRelationToHeadType;
import org.openhds.domain.service.impl.SiteProperties;

public class CheckValidRelationshipToGroupHeadImpl extends AppContextAware implements ConstraintValidator<CheckValidRelationshipToGroupHead, Membership> {
	
	public void initialize(CheckValidRelationshipToGroupHead arg0) { 	}
	
	public boolean isValid(Membership mem,
			ConstraintValidatorContext arg1) {
		
		SiteProperties properties = (SiteProperties)context.getBean("siteProperties");
		
		if (properties.isValueExtensionsForMembershipCodesEnabled())
			return true;
		
		Calendar dobIndiv = mem.getIndividual().getDob();
		Calendar dobHead = mem.getSocialGroup().getGroupHead().getDob();
			
		if (mem.isDeleted())
			return true;
		
		// Indiv must be older than Group Head and must be Male
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.FATHER.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getMaleCode()) && dobIndiv.before(dobHead))
				return true;
		}
		
		// Indiv must be older than Group Head and must be Female
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.MOTHER.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getFemaleCode()) && dobIndiv.before(dobHead))
				return true;
		}
		
		// Indiv must be Female
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.SISTER.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getFemaleCode()))
				return true;
		}
		
		// Indiv must be Male
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.BROTHER.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getMaleCode()))
				return true;
		}
		
		// Indiv must be younger than Group Head and must be Male
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.SON.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getMaleCode()) && dobIndiv.after(dobHead))
				return true;
		}
		
		// Indiv must be younger than Group Head and must be Female
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.DAUGHTER.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getFemaleCode()) && dobIndiv.after(dobHead))
				return true;
		}

		// Indiv must be Male and the Group Head must be Female
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.HUSBAND.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getMaleCode()) && mem.getSocialGroup().getGroupHead().getGender().equals(properties.getFemaleCode()))
				return true;
		}

		// Indiv must be Female and the Group Head must be Male
		if (mem.getbIsToA().equals(MembershipRelationToHeadType.WIFE.toString())) {
			if (mem.getIndividual().getGender().equals(properties.getFemaleCode()) && mem.getSocialGroup().getGroupHead().getGender().equals(properties.getMaleCode()))
				return true;
		}		
		return false;
	}
}
