package org.openhds.webservice.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.RelationshipService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Relationship;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/relationships")
public class RelationshipResource {

	private RelationshipService relationshipService;
	private FieldBuilder fieldBuilder;

	@Autowired
	public RelationshipResource(RelationshipService relationshipService, FieldBuilder fieldBuilder) {
		this.relationshipService = relationshipService;
		this.fieldBuilder = fieldBuilder;
	}

	@XmlRootElement
	private static class Relationships {

		private List<Relationship> relationships;

		@XmlElement(name = "relationship")
		public List<Relationship> getRelationships() {
			return relationships;
		}

		public void setRelationships(List<Relationship> copies) {
			this.relationships = copies;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Relationships getAllRelationships() {
		List<Relationship> allRelationships = relationshipService.getAllRelationships();
		List<Relationship> copies = new ArrayList<Relationship>();

		for (Relationship relationship : allRelationships) {
			Relationship copy = copyRelationship(relationship);
			copies.add(copy);
		}

		Relationships relationships = new Relationships();
		relationships.setRelationships(copies);

		return relationships;
	}

	private Relationship copyRelationship(Relationship relationship) {
		Relationship copy = new Relationship();
		copy.setaIsToB(relationship.getaIsToB());

		Individual individual = new Individual();
		individual.setExtId(relationship.getIndividualA().getExtId());
		copy.setIndividualA(individual);

		individual = new Individual();
		individual.setExtId(relationship.getIndividualB().getExtId());
		copy.setIndividualB(individual);

		copy.setStartDate(relationship.getStartDate());
		return copy;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<? extends Serializable> insert(@RequestBody Relationship relationship) {
		ConstraintViolations cv = new ConstraintViolations();
		relationship.setIndividualA(fieldBuilder.referenceField(relationship.getIndividualA(), cv,
				"Invalid external id for individual A"));
		relationship.setIndividualB(fieldBuilder.referenceField(relationship.getIndividualB(), cv,
				"Invalid external id for individual B"));
		relationship.setCollectedBy(fieldBuilder.referenceField(relationship.getCollectedBy(), cv));

		if (cv.hasViolations()) {
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
		}

		try {
			relationshipService.createRelationship(relationship);
		} catch (ConstraintViolations e) {
			return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Relationship>(copyRelationship(relationship), HttpStatus.CREATED);
	}
}
