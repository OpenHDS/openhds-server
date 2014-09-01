package org.openhds.events.service;

import org.openhds.events.domain.OpenHDSEvent;

public interface EventPublisher {

    void publishEvent(OpenHDSEvent event);
}
