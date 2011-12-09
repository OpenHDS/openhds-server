package org.openhds.datageneration.generator;

import java.sql.SQLException;

import org.openhds.dao.service.GenericDao;
import org.openhds.datageneration.service.EventGenerator;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Visit;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.service.SitePropertiesService;

public abstract class AbstractEventGenerator implements EventGenerator {

	private int numberOfEventsGenerated;
	private final int numberToGenerate;
	protected final GenericDao genericDao;
	protected final EntityService entityService;
	protected final SitePropertiesService siteProperties;

	public AbstractEventGenerator(GenericDao genericDao, EntityService entityService, int numberToGenerate, SitePropertiesService siteProperties) {
		this.genericDao = genericDao;
		this.numberToGenerate = numberToGenerate;
		this.entityService = entityService;
		this.siteProperties = siteProperties;
	}
	
	public final boolean generateEvent(Visit visit, Individual targetIndividual) {
		if (hasReachedGeneratedTarget()) {
			return false;
		}
		
		return generateEventWithIndividual(visit, targetIndividual);
	}

	protected abstract boolean generateEventWithIndividual(Visit visit, Individual indiv);

	public boolean hasReachedGeneratedTarget() {
		return numberOfEventsGenerated >= numberToGenerate;
	}

	protected <T> boolean persistEntity(T entity, boolean isTargetEntity) {
		try {
			entityService.create(entity);
			if (isTargetEntity) {
				numberOfEventsGenerated++;
			}
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println();
		} catch (ConstraintViolations e) {
			System.out.println();
		} catch (SQLException e) {
			System.out.println();
		} catch (Exception e) {
			System.out.println();
		}
		
		return false;
	}
}