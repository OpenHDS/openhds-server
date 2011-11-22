package org.openhds.domain.constraint;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.Membership;

public class CheckStartDateGreaterThanBirthDateImpl implements ConstraintValidator<CheckStartDateGreaterThanBirthDate, Membership> {

    public void initialize(CheckStartDateGreaterThanBirthDate arg0) { }

    public boolean isValid(Membership arg0,
            ConstraintValidatorContext arg1) {

        Calendar startDate = arg0.getStartDate();
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
