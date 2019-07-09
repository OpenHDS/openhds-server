package org.openhds.webservice.resources.xml;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.RelationshipService;
import org.openhds.domain.Relationship;
import org.openhds.domain.wrappers.Relationships;
import org.openhds.util.ShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.CacheResponseWriter;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RelationshipResource.class);

    private RelationshipService relationshipService;
    private FieldBuilder fieldBuilder;
    private FileResolver fileResolver;

    @Autowired
    public RelationshipResource(RelationshipService relationshipService, FieldBuilder fieldBuilder,
            FileResolver fileResolver) {
        this.relationshipService = relationshipService;
        this.fieldBuilder = fieldBuilder;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public Relationships getAllRelationships() {
        List<Relationship> allRelationships = relationshipService.getAllRelationships();
        List<Relationship> copies = new ArrayList<Relationship>();

        for (Relationship relationship : allRelationships) {
            Relationship copy = ShallowCopier.copyRelationship(relationship);
            copies.add(copy);
        }

        Relationships relationships = new Relationships();
        relationships.setRelationships(copies);

        return relationships;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/xml")
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
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Relationship>(ShallowCopier.copyRelationship(relationship), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET)
    public void getCachedRelationships(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveRelationshipXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing relationship xml file: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/zipped", method = RequestMethod.GET)
    public void getZippedRelationships(HttpServletResponse response) {
      try {
          CacheResponseWriter.writeResponse(fileResolver.resolveRelationshipZipFile(), response);
      } catch (IOException e) {
          logger.error("Problem writing relationship zip file: " + e.getMessage());
      }
  }
}
