package org.openhds.controller.service.impl;

import org.openhds.dao.service.GenericDao;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.RoundService;
import org.openhds.domain.model.Round;

public class RoundServiceImpl implements RoundService {

	private GenericDao genericDao;
	
	public RoundServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public void evaluateRound(Round round) throws ConstraintViolations {
		if (round.getUuid() == null && isDuplicateRoundNumber(round)) {
			throw new ConstraintViolations("A round already exists with that round number. Please enter in a unique round number.");
		}
	}

	private boolean isDuplicateRoundNumber(Round round) {
		Round r = genericDao.findByProperty(Round.class, "roundNumber", round.getRoundNumber());
		return r != null;
	}
}
