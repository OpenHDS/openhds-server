package org.openhds.webservice.resources.xml;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.LocationHierarchyService;
import org.openhds.domain.Location;
import org.openhds.domain.wrappers.Locations;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/locations")
public class LocationResource {
    private static final Logger logger = LoggerFactory.getLogger(LocationResource.class);

    private final FieldBuilder fieldBuilder;
    private final LocationHierarchyService locationHierarchyService;
    private final FileResolver fileResolver;

    @Autowired
    public LocationResource(LocationHierarchyService locationHierarchyService, FieldBuilder fieldBuilder,
            FileResolver fileResolver) {
        this.locationHierarchyService = locationHierarchyService;
        this.fieldBuilder = fieldBuilder;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET, produces = "application/xml")
    public ResponseEntity<? extends Serializable> getLocationByExtId(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);
        if (location == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public Locations getAllLocations() {
        List<Location> locations = locationHierarchyService.getAllLocations();
        List<Location> copies = new ArrayList<Location>(locations.size());

        for (Location loc : locations) {
            Location copy = ShallowCopier.copyLocation(loc);
            copies.add(copy);
        }

        Locations allLocations = new Locations();
        allLocations.setLocations(copies);
        return allLocations;
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET, produces = "application/xml")
    public void getAllCachedLocations(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveLocationXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing location xml file: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/xml")
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
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/zipped", method = RequestMethod.GET)
    public void getAllZippedLocations(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveLocationZipFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing location zip file: " + e.getMessage());
        }
    }
}
