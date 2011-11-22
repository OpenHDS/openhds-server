package org.openhds.controller.idgeneration;

import java.util.Collections;
import java.util.List;

public class IdSchemeResource {
	
	List<IdScheme> idScheme; 
	
	public IdScheme getIdSchemeByName(String name) {
		int index = Collections.binarySearch(this.getIdScheme(), new IdScheme(name));
		if (index == -1)
			return null;
		return this.getIdScheme().get(index);
	}
			
	public List<IdScheme> getIdScheme() {
		return idScheme;
	}

	public void setIdScheme(List<IdScheme> idScheme) {
		this.idScheme = idScheme;
	}
}
