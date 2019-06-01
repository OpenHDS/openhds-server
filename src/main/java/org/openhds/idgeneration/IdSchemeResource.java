package org.openhds.idgeneration;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdSchemeResource {
	
	@Autowired
	private List<IdScheme> idScheme; 
	
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

	@PostConstruct
	public void sort() {
		Collections.sort(idScheme);
	}
}
