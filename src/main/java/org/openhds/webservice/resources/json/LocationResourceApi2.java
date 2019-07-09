package org.openhds.webservice.resources.json;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.LocationHierarchyService;
import org.openhds.domain.Location;
import org.openhds.domain.wrappers.Locations;
import org.openhds.util.JsonShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.CacheResponseWriter;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.openhds.webservice.util.Synchronization;
import org.openhds.webservice.util.SynchronizationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	@RequestMapping(method = RequestMethod.GET, value = "/{timestamp}", produces = "application/json")
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

	@RequestMapping(value="/bulkUpdate", method = RequestMethod.PUT, consumes= {"application/json"}, produces = { "application/json" })
	public ResponseEntity<? extends Serializable> pushUpdate(@RequestBody Locations locations) {
		long lastClientUpdate = locations.getTimestamp();
		long time = new Date().getTime();

		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		for(Location location: locations.getLocations()) {
			ConstraintViolations cv = new ConstraintViolations();
			location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
			location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));
			
			
			SynchronizationError err = new SynchronizationError();
			err.setEntityType("location");
			err.setEntityId(location.getExtId());
			err.setFieldworkerExtId(location.getCollectedBy().getExtId());
			List<String> violations = new ArrayList<String>();

			try {
				location.setServerUpdateTime(new Date().getTime());
				this.update(location, lastClientUpdate, time);
			} catch(ConstraintViolations e){
				violations.addAll(e.getViolations());
			}
			
			if(cv.hasViolations()) {
				violations.addAll(cv.getViolations());
			}
			
			errors.add(err);
		}

		Synchronization sync = new Synchronization();
		sync.setErrors(errors);
		sync.setSyncTime(new Date().getTime());
		return new ResponseEntity<Synchronization>(sync, HttpStatus.ACCEPTED);
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

	@RequestMapping(value = "/bulkInsert", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<? extends Serializable> bulkInsert(@RequestBody Locations locations) {
		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		
		for(Location loc: locations.getLocations()) {
			ConstraintViolations cv = new ConstraintViolations();
			
			SynchronizationError err = new SynchronizationError();
			loc.setCollectedBy(fieldBuilder.referenceField(loc.getCollectedBy(), cv));
			loc.setLocationLevel(fieldBuilder.referenceField(loc.getLocationLevel(), cv));
			
			err.setEntityType("location");
			err.setEntityId(loc.getExtId());
			err.setFieldworkerExtId(loc.getCollectedBy().getExtId());
			List<String> violations = new ArrayList<String>();
			
			try {
				locationHierarchyService.createLocation(loc);
			} catch (ConstraintViolations e) {
				
				violations.addAll(e.getViolations());
			} catch(DataIntegrityViolationException e) {
				violations.add(e.getMessage());
			}
			
			// Check violations for fieldworker and location level
			if(cv.hasViolations()) {
				violations.addAll(cv.getViolations());
			}
			
			// Violations have occurred, add list of entities where sync failed
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
		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();

		ConstraintViolations cv = new ConstraintViolations();

		SynchronizationError err = new SynchronizationError();
		err.setEntityType("location");
		err.setEntityId(location.getExtId());
		err.setFieldworkerExtId(location.getCollectedBy().getExtId());
		List<String> violations = new ArrayList<String>();
		
		location.setCollectedBy(fieldBuilder.referenceField(location.getCollectedBy(), cv));
		location.setLocationLevel(fieldBuilder.referenceField(location.getLocationLevel(), cv));

		if(cv.hasViolations()) {
			violations.addAll(cv.getViolations());
		}
		
		try {
			locationHierarchyService.createLocation(location);
		} catch (ConstraintViolations e) {
			violations.addAll(e.getViolations());
		} catch(DataIntegrityViolationException e) {
			violations.add("Location with this external id already exists");
		}
		
		err.setViolations(violations);
		errors.add(err);
		
		Synchronization sync = new Synchronization();
		sync.setErrors(errors);
		sync.setSyncTime(new Date().getTime());
		return new ResponseEntity<Synchronization>(sync, HttpStatus.CREATED);
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