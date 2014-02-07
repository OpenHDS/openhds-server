package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.RoundService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Round;

public class RoundServiceImpl implements RoundService {

    private EntityService entityService;
    private GenericDao genericDao;

    public RoundServiceImpl(GenericDao genericDao, EntityService entityService) {
        this.genericDao = genericDao;
        this.setEntityService(entityService);
    }

    public void evaluateRound(Round round) throws ConstraintViolations {
        if (round.getUuid() == null && isDuplicateRoundNumber(round)) {
            throw new ConstraintViolations(
                    "A round already exists with that round number. Please enter in a unique round number.");
        }
    }

    private boolean isDuplicateRoundNumber(Round round) {
        Round r = genericDao.findByProperty(Round.class, "roundNumber", round.getRoundNumber());
        return r != null;
    }

    @Override
    public List<Round> getAllRounds() {
        return genericDao.findAll(Round.class, false);
    }

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}
	
	@Override
	public List<Round> getLastRound() {
		return genericDao.findMax(Round.class, false, "roundNumber", false);
	}

}
