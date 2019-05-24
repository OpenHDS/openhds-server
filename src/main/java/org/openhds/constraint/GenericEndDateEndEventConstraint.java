package org.openhds.constraint;

import java.util.Calendar;

public interface GenericEndDateEndEventConstraint {
	
	Calendar getEndDate();
	
	String getEndType();
}