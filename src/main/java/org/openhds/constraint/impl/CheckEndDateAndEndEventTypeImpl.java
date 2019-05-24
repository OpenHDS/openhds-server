package org.openhds.constraint.impl;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.AppContextAware;
import org.openhds.constraint.CheckEndDateAndEndEventType;
import org.openhds.constraint.GenericEndDateEndEventConstraint;
import org.openhds.service.impl.SitePropertiesServiceImpl;

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
