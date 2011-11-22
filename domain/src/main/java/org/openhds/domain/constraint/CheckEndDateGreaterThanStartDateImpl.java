package org.openhds.domain.constraint;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckEndDateGreaterThanStartDateImpl implements ConstraintValidator<CheckEndDateGreaterThanStartDate, GenericStartEndDateConstraint> {

    private boolean allowNull;

    public void initialize(CheckEndDateGreaterThanStartDate constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

	public boolean isValid(GenericStartEndDateConstraint arg0, ConstraintValidatorContext arg1) {
		  
		Calendar endDate = arg0.getEndDate();
        Calendar startDate = arg0.getStartDate();
        
    	if (endDate == null && allowNull)
    		return true;
      
        try {
        	        	
        	if (endDate.before(startDate) || endDate.equals(startDate)) {
                return false;
            }
        	
        } catch (Exception e) {
        	return false;
        }
       
        return true;
	}
}
