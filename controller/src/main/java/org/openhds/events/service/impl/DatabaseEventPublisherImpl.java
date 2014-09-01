package org.openhds.events.service.impl;

import org.openhds.events.dao.EventsDAO;
import org.openhds.events.domain.OpenHDSEvent;
import org.openhds.events.service.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DatabaseEventPublisherImpl implements EventPublisher {

    @Autowired
    private EventsDAO eventsDAO;

    @Override
    public void publishEvent(OpenHDSEvent event) {
        eventsDAO.addEvent(event);
    }
}
