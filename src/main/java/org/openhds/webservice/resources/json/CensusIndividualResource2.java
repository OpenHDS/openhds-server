package org.openhds.webservice.resources.json;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.BaselineService;
import org.openhds.domain.CensusIndividual;
import org.openhds.domain.EventType;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Individual;
import org.openhds.domain.Membership;
import org.openhds.domain.Relationship;
import org.openhds.domain.SocialGroup;
import org.openhds.domain.wrappers.CensusIndividuals;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.util.Synchronization;
import org.openhds.webservice.util.SynchronizationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/census")
public class CensusIndividualResource2 {
	private BaselineService baselineService;

	private final FieldBuilder fieldBuilder;

	@Autowired
	public CensusIndividualResource2(BaselineService baseline, FileResolver fileResolver, 
			FieldBuilder fieldBuilder) {

		this.baselineService = baseline;
		this.fieldBuilder = fieldBuilder;
	}

	@RequestMapping(value = "/bulkInsert", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<? extends Serializable> bulkInsert(@RequestBody CensusIndividuals censusIndividuals) {
		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		for(CensusIndividual ind: censusIndividuals.getIndividuals()) {
			SynchronizationError err = new SynchronizationError();
			err.setEntityType("individual");
			err.setEntityId(ind.getIndividual().getExtId());
			err.setFieldworkerExtId(ind.getCollectedBy().getExtId());
			List<String> violations = new ArrayList<String>();

			ConstraintViolations cv = new ConstraintViolations();
			ind.setCollectedBy(fieldBuilder.referenceField(ind.getCollectedBy(), cv));
			ind.setLocation(fieldBuilder.referenceField(ind.getLocation(), cv));
			ind.setSocialGroup(fieldBuilder.referenceField(ind.getSocialGroup(), cv));
			if(ind.getIndividual() != null) {
				ind.getIndividual().setCollectedBy(fieldBuilder.referenceField(ind.getIndividual().getCollectedBy(), cv));
				ind.getIndividual().setMother(fieldBuilder.referenceField(ind.getIndividual().getMother(), cv, null));
				ind.getIndividual().setFather(fieldBuilder.referenceField(ind.getIndividual().getFather(), cv, null));
			}

			if(cv.hasViolations()) {
				violations.addAll(cv.getViolations());
				err.setViolations(violations);
				errors.add(err);
			} else {
				try {
					buildMembershipResidencyAndIndividual(ind);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					violations.add(e.getMessage());
				} catch (ConstraintViolations e) {
					// TODO Auto-generated catch block
					violations.add(e.getMessage());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					violations.add(e.getMessage());
				}
			}

			if(violations.size() > 0) {
				err.setViolations(violations);
				errors.add(err);
			}

		}
		Synchronization sync = new Synchronization();
		sync.setErrors(errors);
		sync.setSyncTime(new Date().getTime());
		return new ResponseEntity<Synchronization>(sync, HttpStatus.ACCEPTED);

	}

	public void buildMembershipResidencyAndIndividual(CensusIndividual ind) throws IllegalArgumentException, ConstraintViolations, SQLException {
		Membership mem = createMembershipObject(ind.getIndividual(), ind.getbIsToA(), ind.getCollectedBy(), ind.getSocialGroup());
		Relationship rel = null;


		if(ind.getSpouse() != null) {
			rel = createRelationshipObject(ind.getIndividual(), ind.getSpouse(), ind.getbIsToA(), ind.getCollectedBy());
			Calendar cal = Calendar.getInstance();
			cal.set(2018, 5, 5);
			baselineService.createResidencyMembershipAndRelationshipForIndividual(ind.getIndividual(), mem, rel, ind.getLocation(), 
					ind.getCollectedBy(), cal);
		} else 
			baselineService.createResidencyAndMembershipForIndividual(ind.getIndividual(), mem, ind.getLocation(), ind.getCollectedBy(), 
					Calendar.getInstance());

	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<? extends Serializable> insert(@RequestBody CensusIndividual ind) {
		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		SynchronizationError err = new SynchronizationError();
		err.setEntityType("individual");
		err.setEntityId(ind.getIndividual().getExtId());
		err.setFieldworkerExtId(ind.getCollectedBy().getExtId());
		List<String> violations = new ArrayList<String>();

		ConstraintViolations cv = new ConstraintViolations();
		ind.setCollectedBy(fieldBuilder.referenceField(ind.getCollectedBy(), cv));
		ind.setLocation(fieldBuilder.referenceField(ind.getLocation(), cv));
		ind.setSocialGroup(fieldBuilder.referenceField(ind.getSocialGroup(), cv));
		if(ind.getIndividual() != null) {
			ind.getIndividual().setCollectedBy(fieldBuilder.referenceField(ind.getIndividual().getCollectedBy(), cv));
			ind.getIndividual().setMother(fieldBuilder.referenceField(ind.getIndividual().getMother(), cv, null));
			ind.getIndividual().setFather(fieldBuilder.referenceField(ind.getIndividual().getFather(), cv, null));
		}

		if(cv.hasViolations()) {
			violations.addAll(cv.getViolations());
			err.setViolations(violations);
			errors.add(err);

			Synchronization sync = new Synchronization();
			sync.setErrors(errors);
			sync.setSyncTime(new Date().getTime());
			return new ResponseEntity<Synchronization>(sync, HttpStatus.ACCEPTED);
		}


		Membership mem = createMembershipObject(ind.getIndividual(), ind.getbIsToA(), ind.getCollectedBy(), ind.getSocialGroup());
		Relationship rel = null;

		try {
			this.baselineService.createIndividual(ind.getIndividual());
			if(ind.getSpouse() != null) {
				rel = createRelationshipObject(ind.getIndividual(), ind.getSpouse(), ind.getbIsToA(), ind.getCollectedBy());
				Calendar cal = Calendar.getInstance();
				cal.set(2018, 5, 5);
				baselineService.createResidencyMembershipAndRelationshipForIndividual(ind.getIndividual(), mem, rel, ind.getLocation(), 
						ind.getCollectedBy(), cal);
			} else 
				baselineService.createResidencyAndMembershipForIndividual(ind.getIndividual(), mem, ind.getLocation(), ind.getCollectedBy(), 
						Calendar.getInstance());
		} catch (ConstraintViolations e) {
			violations.addAll(e.getViolations());
		} catch(SQLException e) {
			violations.add(e.getMessage());
		}

		Synchronization sync = new Synchronization();
		sync.setErrors(errors);
		sync.setSyncTime(new Date().getTime());
		return new ResponseEntity<Synchronization>(sync, HttpStatus.ACCEPTED);
	}


	private  Membership createMembershipObject(Individual ind, String bIsToA, FieldWorker collectedBy, SocialGroup sg) {
		Membership membership = new Membership();
		membership.setIndividual(ind);
		membership.setbIsToA(bIsToA);
		membership.setCollectedBy(collectedBy);
		membership.setSocialGroup(sg);
		membership.setDeleted(false);
		membership.setInsertDate(Calendar.getInstance());

		membership.setStartDate(Calendar.getInstance());
		membership.setStartType(EventType.ENU.toString());
		membership.setEndType("NA");
		membership.setEndDate(null);
		return membership;
	}

	private Relationship createRelationshipObject(Individual indA, Individual indB, String aIsToB, FieldWorker collectedBy) {
		Relationship rel = new Relationship();
		rel.setIndividualA(indA);
		rel.setIndividualB(indB);
		rel.setCollectedBy(collectedBy);
		rel.setDeleted(false);
		rel.setEndType("NA");
		rel.setEndDate(null);
		return rel;
	}
}