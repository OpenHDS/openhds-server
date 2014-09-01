package org.openhds.events.service;

import org.openhds.events.domain.OpenHDSEvent;

public interface EventGateway {

    void publishEvent(OpenHDSEvent event);
}
