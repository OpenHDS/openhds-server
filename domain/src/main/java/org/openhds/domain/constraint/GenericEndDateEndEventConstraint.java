package org.openhds.domain.constraint;

import java.util.Calendar;

public interface GenericEndDateEndEventConstraint {
	
	Calendar getEndDate();
	
	String getEndType();
}