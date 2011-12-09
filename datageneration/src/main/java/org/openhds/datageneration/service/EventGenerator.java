package org.openhds.datageneration.service;

import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Visit;

public interface EventGenerator {

	boolean generateEvent(Visit visit, Individual targetIndividual);

	boolean hasReachedGeneratedTarget();
	
}
