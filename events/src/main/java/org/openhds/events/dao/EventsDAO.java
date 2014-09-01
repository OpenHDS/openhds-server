package org.openhds.events.dao;

import org.openhds.events.domain.OpenHDSEvent;

public interface EventsDAO {

    void updateEvent(OpenHDSEvent event);

    void addEvent(OpenHDSEvent event);
    
    void findById(String eventId);
}
