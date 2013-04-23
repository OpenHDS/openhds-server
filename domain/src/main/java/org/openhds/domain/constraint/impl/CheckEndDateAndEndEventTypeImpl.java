package org.openhds.domain.constraint.impl;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.constraint.CheckEndDateAndEndEventType;
import org.openhds.domain.constraint.GenericEndDateEndEventConstraint;
import org.openhds.domain.service.impl.SitePropertiesServiceImpl;

public class CheckEndDateAndEndEventTypeImpl extends AppContextAware implements ConstraintValidator<CheckEndDateAndEndEventType, GenericEndDateEndEventConstraint> {

	private SitePropertiesServiceImpl siteProperties;
	
	public void initialize(CheckEndDateAndEndEventType arg0) {	
		siteProperties = (SitePropertiesServiceImpl)context.getBean("siteProperties");
	}

	public boolean isValid(GenericEndDateEndEventConstraint arg0, ConstraintValidatorContext arg1) {
		
	  	Calendar endDate = arg0.getEndDate();
        String endEventType = arg0.getEndType();

    	try {
    		
    		if (!endDate.equals(null) && endEventType.equals(siteProperties.getNotApplicableCode()))
    			return false;
    		
    	} catch(Exception e) {
    		
    		if (!endEventType.equals(siteProperties.getNotApplicableCode()))
    			return false;
    	}
    	
    	return true; 
	}
}
