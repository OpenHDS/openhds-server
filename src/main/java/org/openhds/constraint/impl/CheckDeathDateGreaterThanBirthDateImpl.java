package org.openhds.constraint.impl;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckDeathDateGreaterThanBirthDate;
import org.openhds.domain.Death;

public class CheckDeathDateGreaterThanBirthDateImpl implements ConstraintValidator<CheckDeathDateGreaterThanBirthDate, Death> {

    public void initialize(CheckDeathDateGreaterThanBirthDate arg0) { }

    public boolean isValid(Death arg0, ConstraintValidatorContext arg1) {

        Calendar deathDate = arg0.getDeathDate();
        Calendar birthDate = arg0.getIndividual().getDob();

        try {
            if (birthDate.compareTo(deathDate) <= 0) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }
}
