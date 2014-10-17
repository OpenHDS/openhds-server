package org.openhds.webservice.resources;

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractResource<T extends Serializable> {

    protected ResponseEntity<? extends Serializable> createResource(T item) {
        ConstraintViolations cv = new ConstraintViolations();
        setReferencedFields(item, cv);

        if (cv.hasViolations()) {
            return buildBadRequest(cv);
        }

        try {
            saveResource(item);
        } catch (ConstraintViolations violations) {
            return buildBadRequest(violations);
        }

        return new ResponseEntity<T>(copy(item), HttpStatus.CREATED);
    }

    protected ResponseEntity<WebServiceCallException> buildBadRequest(ConstraintViolations cv) {
        return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
    }

    protected abstract T copy(T item);

    protected abstract void saveResource(T item) throws ConstraintViolations;

    protected abstract void setReferencedFields(T item, ConstraintViolations cv);
    
    protected FieldWorker copyFieldWorker(FieldWorker collectedBy) {
        FieldWorker fw = new FieldWorker();
        fw.setExtId(collectedBy.getExtId());
        return fw;
    }

    protected Individual copyIndividual(Individual individual) {
        Individual copy = new Individual();
        copy.setExtId(individual.getExtId());
        return copy;
    }

    protected Visit copyVisit(Visit visitDeath) {
        Visit copy = new Visit();
        copy.setExtId(visitDeath.getExtId());
        return copy;
    }
    
    protected Location copyLocation(Location location) {
        Location copy = new Location();
        copy.setExtId(location.getExtId());
        return copy;
    }
    protected SocialGroup copySocialGroup(SocialGroup sg) {
        SocialGroup copy = new SocialGroup();
        copy.setExtId(sg.getExtId());

        Individual groupHead = new Individual();
        groupHead.setExtId(sg.getGroupHead().getExtId());
        copy.setGroupHead(groupHead);
        copy.setGroupName(sg.getGroupName());
        return copy;
    }
}
