package org.openhds.events.service.impl;

import java.util.List;

import org.openhds.events.domain.OpenHDSEvent;
import org.openhds.events.service.EventGateway;
import org.openhds.events.service.EventPublisher;

public class EventGatewayImpl implements EventGateway{

    private List<EventPublisher> publishers;

    @Override
    public void publishEvent(OpenHDSEvent event) {
        for (EventPublisher publisher : publishers) {
            publisher.publishEvent(event);
        }
    }

}
