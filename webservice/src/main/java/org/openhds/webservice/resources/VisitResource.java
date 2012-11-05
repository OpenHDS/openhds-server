package org.openhds.webservice.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.VisitService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Visit;
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
@RequestMapping("/visits")
public class VisitResource {

    private final VisitService visitService;
    private final FieldBuilder fieldBuilder;

    @Autowired
    public VisitResource(VisitService visitService, FieldBuilder fieldBuilder, EntityService entityService) {
        this.visitService = visitService;
        this.fieldBuilder = fieldBuilder;
    }

    @XmlRootElement
    private static class Visits {
        private List<Visit> visits;

        @XmlElement(name = "visit")
        public List<Visit> getVisits() {
            return visits;
        }

        public void setVisits(List<Visit> visits) {
            this.visits = visits;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Visits getAllVisits() {
        List<Visit> allVisits = visitService.getAllVisits();
        List<Visit> copies = new ArrayList<Visit>(allVisits.size());

        for (Visit visit : allVisits) {
            Visit copy = buildCopyOfVisit(visit);

            copies.add(copy);
        }

        Visits visits = new Visits();
        visits.setVisits(copies);

        return visits;
    }

    protected Visit buildCopyOfVisit(Visit visit) {
        FieldWorker fw = new FieldWorker();
        fw.setExtId(visit.getCollectedBy().getExtId());

        Location location = new Location();
        location.setLocationLevel(null);
        location.setExtId(visit.getVisitLocation().getExtId());

        Visit copy = new Visit();
        copy.setCollectedBy(fw);
        copy.setVisitLocation(location);
        copy.setExtId(visit.getExtId());
        copy.setRoundNumber(visit.getRoundNumber());
        copy.setVisitDate(visit.getVisitDate());
        return copy;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody Visit visit) {
        ConstraintViolations cv = new ConstraintViolations();
        visit.setVisitLocation(fieldBuilder.referenceField(visit.getVisitLocation(), cv));
        visit.setCollectedBy(fieldBuilder.referenceField(visit.getCollectedBy(), cv));

        if (cv.hasViolations()) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        try {
            visitService.createVisit(visit);
        } catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Visit>(buildCopyOfVisit(visit), HttpStatus.CREATED);
    }
}
