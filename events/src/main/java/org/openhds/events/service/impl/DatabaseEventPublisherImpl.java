package org.openhds.events.service.impl;

import org.openhds.events.dao.EventsDAO;
import org.openhds.events.domain.OpenHDSEvent;
import org.openhds.events.service.EventPublisher;

public class DatabaseEventPublisherImpl implements EventPublisher {

    private EventsDAO eventsDAO;

    @Override
    public void publishEvent(OpenHDSEvent event) {
        eventsDAO.addEvent(event);
    }
}
