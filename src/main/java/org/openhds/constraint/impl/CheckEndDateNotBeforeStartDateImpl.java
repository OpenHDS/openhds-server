package org.openhds.constraint.impl;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckEndDateNotBeforeStartDate;
import org.openhds.constraint.GenericStartEndDateConstraint;

public class CheckEndDateNotBeforeStartDateImpl implements
        ConstraintValidator<CheckEndDateNotBeforeStartDate, GenericStartEndDateConstraint>
{

    private boolean allowNull;

    public void initialize( CheckEndDateNotBeforeStartDate constraintAnnotation ){
        this.allowNull = constraintAnnotation.allowNull();
    }

    public boolean isValid( GenericStartEndDateConstraint arg0, ConstraintValidatorContext arg1 ){

        Calendar endDate = arg0.getEndDate();
        Calendar startDate = arg0.getStartDate();

        if( endDate == null && allowNull ){ return true; }

        try{

            if( startDate.compareTo( endDate ) > 0 ){
                return false;
            }

        }catch( Exception e ){
            return false;
        }

        return true;
    }
}
