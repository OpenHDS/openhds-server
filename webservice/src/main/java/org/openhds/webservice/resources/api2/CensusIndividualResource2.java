package org.openhds.webservice.resources.api2;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.BaselineService;
import org.openhds.controller.service.FieldWorkerService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.domain.model.CensusIndividual;
import org.openhds.domain.model.EventType;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.util.JsonShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
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
	private SocialGroupService socialGroupService;
	private IndividualService individualService;
	private LocationHierarchyService locationService;
	private FieldWorkerService fwService;
	private final FieldBuilder fieldBuilder;

	@Autowired
	public CensusIndividualResource2(BaselineService baseline, FileResolver fileResolver, 
			SocialGroupService sg, IndividualService individual, LocationHierarchyService locService, 
			FieldWorkerService fwService, FieldBuilder fieldBuilder) {

		this.baselineService = baseline;
		this.socialGroupService = sg;
		this.individualService = individual;
		this.locationService = locService;
		this.fwService = fwService;
		this.fieldBuilder = fieldBuilder;
	}

	@RequestMapping(method= RequestMethod.POST, value = "/ind", consumes="application/json", produces="application/json")
	public ResponseEntity<? extends Serializable> insert(@RequestBody Individual ind){
		ConstraintViolations cv = new ConstraintViolations();
		if(individualService.findIndivById(ind.getExtId()) != null) {
			cv.addViolations("Individual already exists");
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
		}
		
		try {
			ind.setCollectedBy(fieldBuilder.referenceField(ind.getCollectedBy(), cv));
			ind.setMother(individualService.findIndivById(ind.getMother().getExtId()));
			ind.setFather(individualService.findIndivById(ind.getFather().getExtId()));
			individualService.createIndividual(ind);
		} catch (ConstraintViolations e) {
			// TODO Auto-generated catch block
			cv.addViolations(e.getMessage());
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Individual>(JsonShallowCopier.shallowCopyIndividual(ind), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<? extends Serializable> insert(@RequestBody CensusIndividual ind) {
		ConstraintViolations cv = new ConstraintViolations();
		FieldWorker fw = null;
		try {
			fw = this.fwService.findFieldWorkerById(ind.getCollectedBy());
		} catch (ConstraintViolations e1) {
			return new ResponseEntity<FieldWorker>(fw, HttpStatus.BAD_GATEWAY);
		}
		SocialGroup social = null;
		Individual individual = null;
		if(ind.getSocialGroupExtId().isEmpty() || ind.getSocialGroupHeadExtId().isEmpty() || ind.getLocationExtId().isEmpty())
			return new ResponseEntity<Individual>(JsonShallowCopier.shallowCopyIndividual(individual), HttpStatus.BAD_GATEWAY);

		individual = ind.getIndividual();
		
		if(individual == null) {
			return new ResponseEntity<Individual>(JsonShallowCopier.shallowCopyIndividual(individual), HttpStatus.BAD_GATEWAY);
		}
		
		individual.setCollectedBy(fieldBuilder.referenceField(individual.getCollectedBy(), cv));
		individual.setMother(individualService.findIndivById(individual.getMother().getExtId()));
		individual.setFather(individualService.findIndivById(individual.getFather().getExtId()));
	

		if(cv.hasViolations()) {
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_GATEWAY);
		}
		try {
			social = socialGroupService.findSocialGroupById(ind.getSocialGroupExtId(), "Social Group does not exist");
		} catch (Exception e) {
			cv.addViolations(e.getMessage());	
			cv.addViolations("Social group cannot be found.");
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_GATEWAY);
		}
		
		Location location = locationService.findLocationById(ind.getLocationExtId());
		Membership mem = createMembershipObject(individual, ind.getbIsToA(), fw, social);
		Relationship rel = null;
		
		try {
			if(ind.getSpouse() != null) {
				rel = createRelationshipObject(individual, ind.getSpouse(), ind.getbIsToA(), fw);
				Calendar cal = Calendar.getInstance();
				cal.set(2018, 5, 5);
				baselineService.createResidencyMembershipAndRelationshipForIndividual(individual, mem, rel, location, fw, cal);
			} else 
				baselineService.createResidencyAndMembershipForIndividual(individual, mem, location, fw, Calendar.getInstance());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			cv.addViolations(e.getMessage());
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_GATEWAY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			cv.addViolations(e.getMessage());
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_GATEWAY);

		} catch (ConstraintViolations e) {
			// TODO Auto-generated catch block
			cv.addViolations(e.getMessage());
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<Individual>(JsonShallowCopier.shallowCopyIndividual(individual), HttpStatus.CREATED);
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
