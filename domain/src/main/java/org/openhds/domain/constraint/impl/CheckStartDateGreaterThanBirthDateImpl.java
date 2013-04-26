package org.openhds.domain.constraint.impl;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.constraint.CheckStartDateGreaterThanBirthDate;
import org.openhds.domain.model.Membership;

public class CheckStartDateGreaterThanBirthDateImpl implements ConstraintValidator<CheckStartDateGreaterThanBirthDate, Membership> {

    public void initialize(CheckStartDateGreaterThanBirthDate arg0) { }

    public boolean isValid(Membership arg0,
            ConstraintValidatorContext arg1) {

        Calendar startDate = arg0.getStartDate();
        java.util.Date sDate = startDate.getTime();
        String sDateString = sDate.toString();
        Calendar dob = arg0.getIndividual().getDob();

        try {
            if (dob.before(startDate) || dob.equals(startDate)) {
                return true;

            }
        } catch (Exception e) {
        }

        return false;
    }
}
