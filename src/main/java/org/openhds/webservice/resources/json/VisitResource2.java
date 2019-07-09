package org.openhds.webservice.resources.json;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.EntityService;
import org.openhds.service.VisitService;
import org.openhds.domain.Location;
import org.openhds.domain.Visit;
import org.openhds.domain.wrappers.Locations;
import org.openhds.domain.wrappers.Visits;
import org.openhds.util.ShallowCopier;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/visits2")
public class VisitResource2 {
    private static final Logger logger = LoggerFactory.getLogger(VisitResource2.class);

    private final VisitService visitService;
    private final FieldBuilder fieldBuilder;
    private final FileResolver fileResolver;

    @Autowired
    public VisitResource2(VisitService visitService, FieldBuilder fieldBuilder, EntityService entityService,
            FileResolver fileResolver) {
        this.visitService = visitService;
        this.fieldBuilder = fieldBuilder;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Visits getAllVisits() {
        List<Visit> allVisits = visitService.getAllVisits();
        List<Visit> copies = new ArrayList<Visit>(allVisits.size());

        for (Visit visit : allVisits) {
            Visit copy = ShallowCopier.copyVisit(visit);
            copies.add(copy);
        }

        Visits visits = new Visits();
        visits.setVisits(copies);
        return visits;
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<? extends Serializable> insert(@RequestBody Visit visit) {
        List<SynchronizationError> errors = new ArrayList<SynchronizationError>();

		ConstraintViolations cv = new ConstraintViolations();
		SynchronizationError err = new SynchronizationError();
		err.setEntityType("location");
		err.setEntityId(visit.getExtId());
		err.setFieldworkerExtId(visit.getCollectedBy().getExtId());
		List<String> violations = new ArrayList<String>();
		
        visit.setVisitLocation(fieldBuilder.referenceField(visit.getVisitLocation(), cv));
        visit.setCollectedBy(fieldBuilder.referenceField(visit.getCollectedBy(), cv));

        if (cv.hasViolations()) {
            violations.add(cv.getMessage());
        }

        try {
            visitService.createVisit(visit);
        } catch (ConstraintViolations e) {
            violations.add(cv.getMessage());
        }

        err.setViolations(violations);
		errors.add(err);
		
		Synchronization sync = new Synchronization();
		sync.setErrors(errors);
		sync.setSyncTime(new Date().getTime());
		return new ResponseEntity<Synchronization>(sync, HttpStatus.CREATED);
    }

    
    
    @RequestMapping(value="/bulkUpdate", method = RequestMethod.PUT, consumes= {"application/json"}, produces = { "application/json" })
	public ResponseEntity<? extends Serializable> pushUpdate(@RequestBody Visits visits) {
		long lastClientUpdate = visits.getUpdateTimestamp();
		long time = new Date().getTime();

		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		for(Visit v: visits.getVisits()) {
			ConstraintViolations cv = new ConstraintViolations();
			v.setCollectedBy(fieldBuilder.referenceField(v.getCollectedBy(), cv));
			v.setVisitLocation(fieldBuilder.referenceField(v.getVisitLocation(), cv));
			
			
			SynchronizationError err = new SynchronizationError();
			err.setEntityType("visit");
			err.setEntityId(v.getExtId());
			err.setFieldworkerExtId(v.getCollectedBy().getExtId());
			List<String> violations = new ArrayList<String>();

			try {
				v.setServerUpdateTime(new Date().getTime());
				this.update(v, lastClientUpdate, time);
			} catch(ConstraintViolations e){
				violations.addAll(e.getViolations());
			} catch(Exception e) {
				violations.add(e.getMessage());
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

	public void update(Visit v, long currentTimestamp, long lastClientUpdate) throws Exception {

		if(visitService.findVisitById(v.getExtId(), null) == null) {
			this.visitService.createVisit(v);
		} 

		else if(v.isDeleted()) {
			if(v.getUuid() == null)
				throw new ConstraintViolations("The visit uuid is null.");

			this.visitService.updateVisit(v);
		} 

		else if(visitService.findVisitById(v.getExtId(), null) != null){
			//Set UUID to prevent assignment of a new one.
			v.setUuid(visitService.findVisitById(v.getExtId(), null).getUuid());
			if(lastClientUpdate > currentTimestamp) {
				//Convert timestamp to calendar instance
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(currentTimestamp); 
				v.setInsertDate(c);
			}
			this.visitService.updateVisit(v);
		}
	}
 
	@RequestMapping(value = "/bulkInsert", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<? extends Serializable> bulkInsert(@RequestBody Visits visits) {
		List<SynchronizationError> errors = new ArrayList<SynchronizationError>();
		
		for(Visit v: visits.getVisits()) {
			ConstraintViolations cv = new ConstraintViolations();
			
			SynchronizationError err = new SynchronizationError();
			v.setCollectedBy(fieldBuilder.referenceField(v.getCollectedBy(), cv));
			v.setVisitLocation(fieldBuilder.referenceField(v.getVisitLocation(), cv));
			
			err.setEntityType("visit");
			err.setEntityId(v.getExtId());
			err.setFieldworkerExtId(v.getCollectedBy().getExtId());
			List<String> violations = new ArrayList<String>();
			 
			try {
				visitService.createVisit(v);
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
    public void getCachedVisits(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveVisitXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing visit xml file: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/zipped", method = RequestMethod.GET)
    public void getZippedVisits(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveVisitZipFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing visit xml file: " + e.getMessage());
        }
    }
}