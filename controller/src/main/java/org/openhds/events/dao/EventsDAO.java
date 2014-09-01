package org.openhds.events.dao;

import java.util.List;
import java.util.Map;

import org.openhds.dao.service.GenericDao.RangeProperty;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.events.domain.OpenHDSEvent;

public interface EventsDAO {

    void updateEvent(OpenHDSEvent event);

    void addEvent(OpenHDSEvent event);
    
    void findById(String eventId);

    List<OpenHDSEvent> findAllEventsByFilters(RangeProperty range, ValueProperty... properties);

    List<OpenHDSEvent> findAllEventsByQuery(String query, Map<String, Object> parameters);
}
