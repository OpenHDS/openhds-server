package org.openhds.web.service;

import java.util.List;

public interface EntityClassValidationService<T> {
	
	boolean checkConstraints(T entityItem);
		
	<S> List<String> validateType(S entity);
}
