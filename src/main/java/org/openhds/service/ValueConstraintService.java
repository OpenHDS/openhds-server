package org.openhds.service;

import java.util.List;
import java.util.Map;
import org.jdom2.Element;

public interface ValueConstraintService {
	
	boolean isConstraintDefined(String constraintName);
	
	Element findConstraintByName(String constraintName);
	
	boolean isValidConstraintValue(String constraintName, Object value);
	
	List<String> getAllConstraintNames();
	
	Map<String, String> getMapForConstraint(String constraintName);
}
