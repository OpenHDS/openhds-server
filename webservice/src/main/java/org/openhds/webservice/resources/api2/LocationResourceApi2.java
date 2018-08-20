package org.openhds.webservice.resources.api2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.wrappers.Locations;
import org.openhds.domain.util.JsonShallowCopier;
import org.openhds.domain.util.ShallowCopier;
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
@RequestMapping("/locations2")
public class LocationResourceApi2 {
    private static final Logger logger = LoggerFactory.getLogger(LocationResourceApi2.class);

    private final FieldBuilder fieldBuilder;
    private final LocationHierarchyService locationHierarchyService;
    private final FileResolver fileResolver;

    @Autowired
    public LocationResourceApi2(LocationHierarchyService locationHierarchyService, FieldBuilder fieldBuilder,
            FileResolver fileResolver) {
        this.locationHierarchyService = locationHierarchyService;
        this.fieldBuilder = fieldBuilder;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<? extends Serializable> getLocationByExtId(@PathVariable String extId) {
        Location location = locationHierarchyService.findLocationById(extId);
        if (location == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Location>(JsonShallowCopier.copyLocation(location), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Locations getAllLocations() {
        List<Location> locations = locationHierarchyService.getAllLocations();
        List<Location> copies = new ArrayList<Location>(locations.size());

        for (Location loc : locations) {
            Location copy = JsonShallowCopier.copyLocation(loc);
            copies.add(copy);
        }

        Locations allLocations = new Locations();
        allLocations.setLocations(copies);
        allLocations.setTimestamp(new Date().getTime());
        return allLocations;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pull/{timestamp}", produces = "application/json")
	public ResponseEntity<Locations> getUpdatedLocations(@PathVariable long timestamp) {
		long time = new Date().getTime();

		List<Location> locations = locationHierarchyService.getAllLocations();
		List<Location> copies = new ArrayList<Location>(locations.size());

		for (Location loc : locations) {
			long compTime;
			if(loc.getServerUpdateTime() == 0)
				compTime = loc.getServerInsertTime();
			else 
				compTime = loc.getServerUpdateTime();
			
			if(timestamp <= compTime && timestamp < time) {
				copies.add(JsonShallowCopier.copyLocation(loc));
			}
		}
		Locations all = new Locations();
		ArrayList<Location> allLocations = new ArrayList<Location>();
		allLocations.addAll(copies);
		all.setLocations(allLocations);
		all.setTimestamp(time);
		return new ResponseEntity<Locations>(all, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value="/pushUpdates", method = RequestMethod.PUT, consumes= {"application/json"}, produces = { "application/json" })
	public ResponseEntity<? extends Serializable> pushUpdate(@RequestBody Locations locations) {
		long lastClientUpdate = locations.getTimestamp();
		long time = new Date().getTime();

		ConstraintViolations cv = new ConstraintViolations();

		for(Location location: locations.getLocations()) {
			try {
				location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
				location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));
				location.setServerUpdateTime(new Date().getTime());
				this.update(location, lastClientUpdate, time);
			} catch(ConstraintViolations e){
				return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(time, HttpStatus.ACCEPTED);
	}

	public void update(Location loc, long currentTimestamp, long lastClientUpdate) throws ConstraintViolations {

		if(locationHierarchyService.findLocationById(loc.getExtId()) == null) {
			this.locationHierarchyService.createLocation(loc);
		} 
		
		else if(loc.isDeleted()) {
			if(loc.getUuid() == null)
				throw new ConstraintViolations("The location uuid is null.");
			
			this.locationHierarchyService.updateLocation(loc);
		} 
		
		else if(locationHierarchyService.findLocationById(loc.getExtId()) != null){
			//Set UUID to prevent assignment of a new one.
			loc.setUuid(locationHierarchyService.findLocationById(loc.getExtId()).getUuid());
			if(lastClientUpdate > currentTimestamp) {
				//Convert timestamp to calendar instance
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(currentTimestamp);
				loc.setInsertDate(c);
			}
			this.locationHierarchyService.updateLocation(loc);
		}
	}
	
    @RequestMapping(value = "/cached", method = RequestMethod.GET, produces = "application/json")
    public void getAllCachedLocations(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveLocationXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing location xml file: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
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

        return new ResponseEntity<Location>(JsonShallowCopier.copyLocation(location), HttpStatus.CREATED);
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