package org.openhds.webservice.resources;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.wrappers.Locations;
import org.openhds.domain.util.ShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.CacheResponseWriter;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.openhds.webservice.response.WebserviceResult;
import org.openhds.webservice.response.WebserviceResultHelper;
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

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<? extends Serializable> getLocationByExtIdJson(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);
        if (location == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        WebserviceResult result = new WebserviceResult();
        result.addDataElement("location", ShallowCopier.copyLocation(location));
        result.setResultCode(1);
        result.setStatus("success");
        result.setResultMessage("Location was found");
        
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{extId}", method = RequestMethod.GET, produces = "application/xml")
    public ResponseEntity<? extends Serializable> getLocationByExtIdXml(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);
        if (location == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public Locations getAllLocationsXml() {
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

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<WebserviceResult> getAllLocationsJson() {
        List<Location> locations = locationHierarchyService.getAllLocations();
        List<Location> copies = new ArrayList<Location>(locations.size());

        for (Location loc : locations) {
            Location copy = ShallowCopier.copyLocation(loc);
            copies.add(copy);
        }

        WebserviceResult result = new WebserviceResult();
        result.addDataElement("locations", copies);
        result.setResultCode(1);
        result.setStatus("success");
        result.setResultMessage(locations.size() + " locations were found.");
        
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET, produces = "application/xml")
    public void getAllCachedLocationsXml(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveLocationXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing location xml file: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET, produces = "application/json")
    public void getAllCachedLocationsJson(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveLocationXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing location xml file: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/xml", consumes = "application/xml")
    public ResponseEntity<? extends Serializable> insertXml(@RequestBody Location location) {
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

        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<? extends Serializable> insertJson(@RequestBody Location location) {
        ConstraintViolations cv = new ConstraintViolations();
        location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
        location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));

        if (cv.hasViolations()) {
        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to create location, there are " + cv.getViolations().size() + " constraint violations.");
        }

        try {
            locationHierarchyService.createLocation(location);
        } catch (ConstraintViolations e) {
        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to create location, there are " + cv.getViolations().size() + " constraint violations.");
        }

        WebserviceResult result = new WebserviceResult();
        result.addDataElement("location", ShallowCopier.copyLocation(location));
        result.setResultCode(1);
        result.setStatus("success");
        result.setResultMessage("Location created");
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "application/xml", consumes = "application/xml")
    public ResponseEntity<? extends Serializable> addOrUpdateXml(@RequestBody Location location) {
    	
        ConstraintViolations cv = new ConstraintViolations();
        location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
        location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));

        if (cv.hasViolations()) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

    	Location existingLocation  = locationHierarchyService.findLocationById(location.getExtId());
    	if (existingLocation == null) {
    		try {
				locationHierarchyService.createLocation(location);
			} catch (ConstraintViolations e) {
	            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(new ConstraintViolations(e.getMessage(), e.getViolations())), HttpStatus.BAD_REQUEST);

			}
            return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.CREATED);

    	}
    	
    	//updating fields on existing persistent object
    	existingLocation.setLocationName(location.getLocationName());

    	try {
			locationHierarchyService.updateLocation(existingLocation);
		} catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(new ConstraintViolations(e.getMessage(), e.getViolations())), HttpStatus.BAD_REQUEST);
		}
    	
        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity<? extends Serializable> addOrUpdateJson(@RequestBody Location location) {
    	
        ConstraintViolations cv = new ConstraintViolations();
        location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
        location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));

        if (cv.hasViolations()) {
        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to create or update location, there are " + cv.getViolations().size() + " constraint violations.");
        }

    	Location existingLocation  = locationHierarchyService.findLocationById(location.getExtId());
    	if (existingLocation == null) {
    		try {
				locationHierarchyService.createLocation(location);
			} catch (ConstraintViolations e) {
	        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to create or update location, there are " + cv.getViolations().size() + " constraint violations.");
			}
            WebserviceResult result = new WebserviceResult();
            result.addDataElement("location", ShallowCopier.copyLocation(location));
            result.setResultCode(1);
            result.setStatus("success");
            result.setResultMessage("Location was created");
            
            return new ResponseEntity<WebserviceResult>(result, HttpStatus.CREATED);

    	}
    	
    	//updating fields on existing persistent object
    	existingLocation.setLocationName(location.getLocationName());

    	try {
			locationHierarchyService.updateLocation(existingLocation);
		} catch (ConstraintViolations e) {
        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to create or update location, there are " + cv.getViolations().size() + " constraint violations.");
        }
    	
        WebserviceResult result = new WebserviceResult();
        result.addDataElement("location", ShallowCopier.copyLocation(location));
        result.setResultCode(1);
        result.setStatus("success");
        result.setResultMessage("Location was updated");
        
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.DELETE, produces = "application/xml")
    public ResponseEntity<? extends Serializable> deleteLocationByExtIdXml(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);

        if (location == null) {
        	return new ResponseEntity<String>(HttpStatus.GONE);
        }

    	try {
			locationHierarchyService.deleteLocation(location);
		} catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(new ConstraintViolations(e.getMessage(), e.getViolations())), HttpStatus.BAD_REQUEST);
		}
    	
        return new ResponseEntity<Location>(ShallowCopier.copyLocation(location), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{extId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<? extends Serializable> deleteLocationByExtIdJson(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);

        if (location == null) {
        	return new ResponseEntity<String>(HttpStatus.GONE);
        }

    	try {
			locationHierarchyService.deleteLocation(location);
		} catch (ConstraintViolations cv) {
        	return WebserviceResultHelper.constraintViolationResponse(cv, "Unable to delete location, there are " + cv.getViolations().size() + " constraint violations.");
        }

        WebserviceResult result = new WebserviceResult();
        result.addDataElement("location", ShallowCopier.copyLocation(location));
        result.setResultCode(1);
        result.setStatus("success");
        result.setResultMessage("Location was deleted");

        return new ResponseEntity<WebserviceResult>(result, HttpStatus.OK);
    }
}
