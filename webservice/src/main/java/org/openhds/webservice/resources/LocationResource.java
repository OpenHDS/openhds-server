package org.openhds.webservice.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/locations")
public class LocationResource {

    private final FieldBuilder fieldBuilder;
    private final LocationHierarchyService locationHierarchyService;

    @Autowired
    public LocationResource(LocationHierarchyService locationHierarchyService, FieldBuilder fieldBuilder) {
        this.locationHierarchyService = locationHierarchyService;
        this.fieldBuilder = fieldBuilder;
    }

    @XmlRootElement
    private static class Locations {

        private List<Location> locations;

        @XmlElement(name = "location")
        public List<Location> getLocations() {
            return locations;
        }

        public void setLocations(List<Location> locations) {
            this.locations = locations;
        }

    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET)
    public ResponseEntity<? extends Serializable> getLocationByExtId(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);
        if (location == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Location>(copyLocation(location), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Locations getAllLocations() {
        List<Location> locations = locationHierarchyService.getAllLocations();
        List<Location> copies = new ArrayList<Location>(locations.size());

        for (Location loc : locations) {
            Location copy = copyLocation(loc);
            copies.add(copy);
        }

        Locations allLocations = new Locations();
        allLocations.setLocations(copies);
        return allLocations;
    }

    protected Location copyLocation(Location loc) {
        Location copy = new Location();
        
        copy.setAccuracy(getEmptyStringIfBlank(loc.getAccuracy()));
        copy.setAltitude(getEmptyStringIfBlank(loc.getAltitude()));
        copy.setLatitude(getEmptyStringIfBlank(loc.getLatitude()));
        copy.setLongitude(getEmptyStringIfBlank(loc.getLongitude()));
        
        LocationHierarchy level = new LocationHierarchy();
        level.setExtId(loc.getLocationLevel().getExtId());
        copy.setLocationLevel(level);

        copy.setExtId(loc.getExtId());
        copy.setLocationName(loc.getLocationName());
        copy.setLocationType(loc.getLocationType());

        FieldWorker fw = new FieldWorker();
        fw.setExtId(loc.getCollectedBy().getExtId());
        copy.setCollectedBy(fw);
        return copy;
    }

    private String getEmptyStringIfBlank(String accuracy) {
        if (accuracy == null || accuracy.trim().isEmpty()) {
            return "";
        }
        
        return accuracy;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody Location location) {
        ConstraintViolations cv = new ConstraintViolations();
        location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
        location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));

        if (cv.hasViolations()) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        try {
            locationHierarchyService.createLocation(location);
        } catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Location>(copyLocation(location), HttpStatus.CREATED);
    }
}
