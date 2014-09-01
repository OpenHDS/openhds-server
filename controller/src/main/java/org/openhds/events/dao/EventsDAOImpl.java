package org.openhds.events.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.RangeProperty;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.events.domain.EventMetaData;
import org.openhds.events.domain.OpenHDSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsDAOImpl implements EventsDAO {

    private static final Logger logger = LoggerFactory.getLogger(EventsDAOImpl.class);

    private GenericDao genericDao;

    public EventsDAOImpl(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public void updateEvent(OpenHDSEvent event) {
            genericDao.update(event);
    }

    @Override
    public void addEvent(OpenHDSEvent event) {
        List<EventMetaData> mdList = new ArrayList<EventMetaData>();
        EventMetaData md = new EventMetaData();
        md.setNumTimesRead(0);
        md.setSystem("default");
        md.setStatus("unread");
        md.setInsertDate(Calendar.getInstance());
        mdList.add(md);
        event.setEventMetaData(mdList);
        genericDao.create(event);
    }

    @Override
    public void findById(String eventId) {
        genericDao.findByProperty(null, null, null);
    }

    @Override
    public List<OpenHDSEvent> findAllEventsByFilters(RangeProperty range, ValueProperty... properties) {
        if (range == null) {
            return genericDao.findListByMultiProperty(OpenHDSEvent.class, properties);
        }
        return genericDao.findListByMultiPropertyAndRange(OpenHDSEvent.class, range, properties);
    }

    @Override
    public List<OpenHDSEvent> findAllEventsByQuery(String query, Map<String, Object> parameters) {
        return genericDao.performSQLQuery(OpenHDSEvent.class, query, parameters);
    }

}
