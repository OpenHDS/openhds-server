package org.openhds.webservice.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.events.dao.EventsDAO;
import org.openhds.events.domain.EventMetaData;
import org.openhds.events.domain.OpenHDSEvent;
import org.openhds.webservice.response.WebserviceResult;
import org.openhds.webservice.response.constants.ResultCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/events")
public class EventsResource {
    private static final Logger logger = LoggerFactory.getLogger(EventsResource.class);

    @Autowired
    private EventsDAO eventsDAO;

    @Autowired
    private GenericDao genericDAO;

    private static final String SQL_STRING = "select * from Events p where ((:actionType IS NULL OR p.actionType = :actionType) AND (:entityType is NULL OR p.entityType = :entityType) AND (p.insertDate between :minDate and :maxDate) AND (not exists (select c.uuid from Events p2 join EventMetaData c on p2.uuid = c.event_uuid where c.system = :system and p2.uuid = p.uuid) or exists (select c.uuid from Events p2 join EventMetaData c on p2.uuid = c.event_uuid where c.system = :system AND (c.status = :status) and p2.uuid = p.uuid)))";
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getEvents(@RequestParam(value="system", required=false) String system,
            @RequestParam(value="actionType", required=false) String actionType,
            @RequestParam(value="status", required=false) String status,
            @RequestParam(value="result", required=false) String result,
            @RequestParam(value="entityType", required=false) String entityType,
            @RequestParam(value="minDate", required=false) String minDate,
            @RequestParam(value="maxDate", required=false) String maxDate) throws NoSuchMethodException, SecurityException, ConstraintViolations {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar minRange = Calendar.getInstance();
        Calendar maxRange = Calendar.getInstance();
        try {

            if (minDate != null) {
                minRange.setTime(format.parse(minDate));
            } else {
                minRange.setTime(format.parse("1970-1-1"));
            }

            if (maxDate != null) {
                maxRange.setTime(format.parse(maxDate));
            } else {
                maxRange.setTime(format.parse("5000-1-1"));
            }

        } catch (ParseException e) {
            WebserviceResult errorResult = new WebserviceResult();
            errorResult.setResultMessage(e.getMessage());
            errorResult.setResultCode(ResultCodes.BAD_PARAMETER_CODE);
            errorResult.setStatus(ResultCodes.ERROR);

            return new ResponseEntity<WebserviceResult>(errorResult, HttpStatus.BAD_REQUEST);
        }

        if (system == null || system.trim().length() == 0) {
            system = "default";
        }

        if (status == null || status.trim().length() == 0) {
            status = "unread";
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("system", system);
        parameters.put("maxDate", maxRange);
        parameters.put("minDate", minRange);
        parameters.put("actionType", actionType);
        parameters.put("entityType", entityType);
        parameters.put("status", status);

        List<OpenHDSEvent> events = eventsDAO.findAllEventsByQuery(SQL_STRING, parameters);
        List<OpenHDSEvent> shallowCopies = new ArrayList<OpenHDSEvent>();

        for (OpenHDSEvent event : events) {
            shallowCopies.add(shallowCopyEvent(event));
        }

        WebserviceResult successResult = new WebserviceResult();
        successResult.addDataElement("events", shallowCopies);
        successResult.setResultCode(ResultCodes.SUCCESS_CODE);
        successResult.setStatus(ResultCodes.SUCCESS);
        successResult.setResultMessage("There are " + events.size() + " matching records");

        //update as read right before response
        
        updateEvents(system, events);

        return new ResponseEntity<WebserviceResult>(successResult, HttpStatus.OK);
    }

    /**
     * This method performs an update of all events that were just read by the system,
     * incrementing number of times read by 1, setting status to read, and updating the
     * result if there is one
     * @param system
     * @param result
     * @param events
     */
    private void updateEvents(String system, List<OpenHDSEvent> events) {
        for (OpenHDSEvent event : events) {
            if (system == null) { //default event MetaData
                EventMetaData md = event.findMetaData("default");
                md.setNumTimesRead(md.getNumTimesRead() + 1);
                md.setStatus("read");
                md.setLastUpdated(Calendar.getInstance());
                eventsDAO.updateEvent(event);
                continue;
            } 

            EventMetaData md = event.findMetaData(system);

            if (md == null) { //Haven't seen record before
                md = new EventMetaData();
                md.setNumTimesRead(md.getNumTimesRead() + 1);
                md.setStatus("read");
                md.setSystem(system);
                event.getEventMetaData().add(md);
                Calendar cal = Calendar.getInstance();
                md.setLastUpdated(cal);
                md.setInsertDate(cal);
            } else { //Update existing record
                md.setNumTimesRead(md.getNumTimesRead() + 1);
                md.setStatus("read");
                md.setSystem(system);
                md.setLastUpdated(Calendar.getInstance());
            }

            eventsDAO.updateEvent(event);
        }
    }

    public static OpenHDSEvent shallowCopyEvent(OpenHDSEvent event) {

        OpenHDSEvent shallowEvent = new OpenHDSEvent();
        shallowEvent.setActionType(event.getActionType());
        shallowEvent.setEntityType(event.getEntityType());
        shallowEvent.setEventData(event.getEventData());
        shallowEvent.setInsertDate(event.getInsertDate());
        shallowEvent.setUuid(event.getUuid());
        shallowEvent.setEventMetaData(null);

        return shallowEvent;
    }
}