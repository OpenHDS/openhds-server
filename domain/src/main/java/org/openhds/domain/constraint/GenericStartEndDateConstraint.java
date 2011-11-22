package org.openhds.domain.constraint;

import java.util.Calendar;

public interface GenericStartEndDateConstraint {
	
	Calendar getStartDate();
	
	Calendar getEndDate();
}