package org.openhds.constraint.impl;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.constraint.CheckRecordedDateGreaterThanInsertDate;
import org.openhds.domain.PregnancyObservation;

public class CheckRecordedDateGreaterThanInsertDateImpl implements ConstraintValidator<CheckRecordedDateGreaterThanInsertDate, PregnancyObservation> {

	public void initialize(CheckRecordedDateGreaterThanInsertDate arg0) { }
	
	public boolean isValid(PregnancyObservation arg0,
			ConstraintValidatorContext arg1) {
		
		Calendar insertDate = arg0.getMother().getInsertDate();
		Calendar recordedDate = arg0.getRecordedDate();
		
		 try {
        	 if (insertDate.before(recordedDate)) 
                 return true;
        } 
		catch (Exception e) { }

		return false;
	}

}
