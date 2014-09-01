package org.openhds.events.dao;

import java.sql.SQLException;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.dao.service.GenericDao;
import org.openhds.events.domain.OpenHDSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsDAOImpl implements EventsDAO {

    private static final Logger logger = LoggerFactory.getLogger(EventsDAOImpl.class);
    private EntityService entityService;
    private GenericDao genericDao;

    @Override
    public void updateEvent(OpenHDSEvent event) {
            try {
                entityService.save(event);
            } catch (ConstraintViolations | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    @Override
    public void addEvent(OpenHDSEvent event) {
        try {
            entityService.create(event);
        } catch (IllegalArgumentException | ConstraintViolations | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void findById(String eventId) {
        genericDao.findByProperty(null, null, null);
    }

}
