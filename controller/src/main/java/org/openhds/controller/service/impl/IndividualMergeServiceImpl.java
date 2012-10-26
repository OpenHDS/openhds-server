package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.InMigrationService;
import org.openhds.controller.service.IndividualMergeService;
import org.openhds.controller.service.MembershipService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Residency;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the IndividualMergeService interface
 * This class achieves the merging process by combining the services of multiple entities
 * 
 * @author Dave
 *
 */
public class IndividualMergeServiceImpl implements IndividualMergeService {
	// using entity service for in migration and membership
	private GenericDao genericDao;
	private EntityService entityService;
	private MembershipService membershipService;
	private InMigrationService inMigrationService;
	
	public IndividualMergeServiceImpl(EntityService entityService, GenericDao genericDao, MembershipService membershipService,
		InMigrationService inMigrationService) {
		this.entityService = entityService;
		this.genericDao = genericDao;
		this.membershipService = membershipService;
		this.inMigrationService = inMigrationService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.openhds.service.IndividualMergeService#mergeIndividuals(org.openhds.model.Individual, org.openhds.model.Individual)
	 */
	@Transactional(rollbackFor={ConstraintViolations.class, SQLException.class})
	public int mergeIndividuals(Individual primary, Individual toMergeFrom, List<MergeEvents> events) throws ConstraintViolations, SQLException{
		// this is a naive implementation and will most likely be extended/specialized in the future
		// currently, only in migrations and memberships can be merged/copied from 2 individuals
		int eventsMerged = 0;
		
		// grab the in migrations first
		if (events.contains(MergeEvents.IN_MIGRATION)) {
			List<InMigration> inMigs = getInMigrationEvents(primary, toMergeFrom);
			eventsMerged += inMigs.size();
			
			for(InMigration inMig : inMigs) {
				inMigrationService.evaluateInMigration(inMig);
				entityService.create(inMig);
			}
		}
		
		if (events.contains(MergeEvents.MEMBERSHIP)) {
			// now the memberships
			List<Membership> memberships = getMembershipEvents(primary, toMergeFrom);
			eventsMerged += memberships.size();
			
			for(Membership mem : memberships) {
				membershipService.evaluateMembership(mem);
				entityService.create(mem);
			}
		}
		
		return eventsMerged;
	}
	
	/**
	 * Lookup and copy all memberships from one individual to another individual
	 * @param primary the individual who will receive the copied events
	 * @param toMergeFrom the individual to lookup events on
	 * @return a copy of any membership events from the toMergeFrom individual
	 */
	private List<Membership> getMembershipEvents(Individual primary, Individual toMergeFrom) {
		List<Membership> events = new ArrayList<Membership>();
		
		List<Membership> memberships = genericDao.findListByProperty(Membership.class, "individual", toMergeFrom);
		
		for(Membership mem : memberships) {
			Membership membership = copyMembership(primary, mem);
			events.add(membership);
		}
		
		return events;
	}

	/**
	 * Lookup and copy all in migration events from one individual to another
	 * @param primary the individual who will receive the copied events
	 * @param toMergeFrom the individual to lookup events on
	 * @return a copy of any in migrations from the toMergeFrom individual
	 */
	private List<InMigration> getInMigrationEvents(Individual primary, Individual toMergeFrom) {
		
		List<InMigration> events = new ArrayList<InMigration>();
		
		// first attempt to merge any in migration events
		List<InMigration> inMigrations = genericDao.findListByProperty(InMigration.class, "individual", toMergeFrom);
		
		for(InMigration inMig : inMigrations) {
			// make a copy of the in migration & residency
			InMigration inMigNew = copyInMigration(primary, inMig);
			Residency residencyNew = copyResidency(primary, inMig.getResidency());
			inMigNew.setResidency(residencyNew);
			events.add(inMigNew);
		}
		
		return events;
	}

	/**
	 * Copy a membership
	 * TODO: Possibly use the clone method? This could be troublesome because
	 * any changes made to the membership model object need to added
	 * here as well
	 * @param primary
	 * @param mem
	 * @return
	 */
	private Membership copyMembership(Individual primary, Membership mem) {
		Membership membership = new Membership();
		membership.setIndividual(primary);
		
		membership.setCollectedBy(mem.getCollectedBy());
		membership.setDeleted(mem.isDeleted());
		membership.setEndDate(mem.getEndDate());
		membership.setEndType(mem.getEndType());
		membership.setInsertBy(mem.getInsertBy());
		membership.setInsertDate(mem.getInsertDate());
		membership.setSocialGroup(mem.getSocialGroup());
		membership.setStartDate(mem.getStartDate());
		membership.setStartType(mem.getStartType());
		membership.setStatus(mem.getStatus());
		membership.setVoidBy(mem.getVoidBy());
		membership.setVoidDate(mem.getVoidDate());
		membership.setVoidReason(mem.getVoidReason());

		return membership;		
	}	
	
	/**
	 * Copy an in migration event
	 * TODO: use clone method?
	 * @param primary
	 * @param inMig
	 * @return
	 */
	private InMigration copyInMigration(Individual primary, InMigration inMig) {
		InMigration inMigNew = new InMigration();
		inMigNew.setCollectedBy(inMig.getCollectedBy());
		inMigNew.setDeleted(inMig.isDeleted());
		inMigNew.setIndividual(primary);
		inMigNew.setInsertBy(inMig.getInsertBy());
		inMigNew.setInsertDate(inMig.getInsertDate());
		inMigNew.setOrigin(inMig.getOrigin());
		inMigNew.setReason(inMig.getReason());
		inMigNew.setRecordedDate(inMig.getRecordedDate());
		inMigNew.setStatus(inMig.getStatus());
		inMigNew.setVisit(inMig.getVisit());
		inMigNew.setVoidBy(inMig.getVoidBy());
		inMigNew.setVoidDate(inMig.getVoidDate());
		inMigNew.setVoidReason(inMig.getVoidReason());
		
		return inMigNew;
	}

	/**
	 * Copy a residency
	 * TODO: use the clone method?
	 * @param primary
	 * @param origResidency
	 * @return
	 */
	private Residency copyResidency(Individual primary, Residency origResidency) {
		Residency residency = new Residency();
		residency.setEndDate(origResidency.getEndDate());
		residency.setEndType(origResidency.getEndType());
		residency.setIndividual(primary);
		residency.setLocation(origResidency.getLocation());
		residency.setStartDate(origResidency.getStartDate());
		residency.setStartType(origResidency.getStartType());
		residency.setCollectedBy(origResidency.getCollectedBy());
		
		return residency;
	}
}

