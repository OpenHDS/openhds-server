package org.openhds.domain.service;

import java.util.List;
import java.util.Map;
import org.jdom.Element;

public interface ValueConstraintService {
	
	boolean isConstraintDefined(String constraintName);
	
	Element findConstraintByName(String constraintName);
	
	boolean isValidConstraintValue(String constraintName, Object value);
	
	List<String> getAllConstraintNames();
	
	Map<String, String> getMapForConstraint(String constraintName);
}
