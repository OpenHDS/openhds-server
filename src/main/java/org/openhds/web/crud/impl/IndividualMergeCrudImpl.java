package org.openhds.web.crud.impl;

import java.util.ArrayList;
import java.util.List;
import org.openhds.domain.IndividualMerge;
import org.openhds.exception.ConstraintViolations;
import org.openhds.service.IndividualMergeService;
import org.openhds.service.IndividualMergeService.MergeEvents;

public class IndividualMergeCrudImpl extends EntityCrudImpl<IndividualMerge, String> {
	
	IndividualMergeService indivMergeService;

	public IndividualMergeCrudImpl(Class<IndividualMerge> entityClass) {
		super(entityClass);
	}

	@Override
	public String create() {
		// determine the event types to attempt to merge
		List<MergeEvents> events = new ArrayList<MergeEvents>();
		if (entityItem.isMergeInMigrations()) {
			events.add(MergeEvents.IN_MIGRATION);
		}
		
		if (entityItem.isMergeMemberships()) {
			events.add(MergeEvents.MEMBERSHIP);
		}
	
		if (events.size() == 0) {
			jsfService.addError("No events selected. Please specify the event types to merge");
			return "";
		}
		
		try {
			int eventsMerged = indivMergeService.mergeIndividuals(entityItem.getPrimary(), entityItem.getToMergeFrom(), events);
			
			if (eventsMerged == 0) {
				jsfService.addError("No events were merged for these 2 individuals.");
				return "";
			}
			jsfService.addMessage("Merged " + eventsMerged + " events.");
			
			createSetup();
			
			return "";
		}  catch (ConstraintViolations e) {
			jsfService.addError(e.getMessage());
		} catch (Exception e) {
			jsfService.addError("An error occurred while merging the individuals.");
		}
		
		return "";
	}
	
	public IndividualMergeService getIndivMergeService() {
		return indivMergeService;
	}

	public void setIndivMergeService(IndividualMergeService indivMergeService) {
		this.indivMergeService = indivMergeService;
	}
}
