package org.openhds.errorhandling.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.dao.service.GenericDao.RangeProperty;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.errorhandling.domain.ErrorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogDAOImpl implements ErrorLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(ErrorLogDAOImpl.class);
    private EntityService entityService;
    private GenericDao genericDao;

    public ErrorLogDAOImpl(EntityService entityService, GenericDao genericDao) {
        this.entityService = entityService;
        this.genericDao = genericDao;
    }

    public ErrorLog createErrorLog(ErrorLog error) {
        try {
            error.setInsertDate(Calendar.getInstance());
            entityService.create(error);
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to insert error log into DB: " + e.getMessage());
        } catch (ConstraintViolations e) {
            logger.warn("Unable to insert error log into DB: " + e.getMessage());
        } catch (SQLException e) {
            logger.warn("Unable to insert error log into DB: " + e.getMessage());
        }

        return error;
    }

    @Override
    public ErrorLog findById(String uuid) {
        return genericDao.findByProperty(ErrorLog.class, "uuid", uuid);
    }

    @Override
    public List<ErrorLog> findByResolutionStatus(String resolutionStatus) {
        return genericDao.findListByProperty(ErrorLog.class, "resolutionStatus", resolutionStatus);
    }

    @Override
    public List<ErrorLog> findByAssignedTo(String assignedTo) {
        return genericDao.findListByProperty(ErrorLog.class, "assignedTo", assignedTo);
    }

    @Override
    public List<ErrorLog> findByFieldWorker(FieldWorker fieldWorker) {
        return genericDao.findListByProperty(ErrorLog.class, "fieldWorker", fieldWorker);
    }

    @Override
    public List<ErrorLog> findAllByFilters(RangeProperty range, ValueProperty... properties) {
        if (range == null) {
            return genericDao.findListByMultiProperty(ErrorLog.class, properties);
        }
        return genericDao.findListByMultiPropertyAndRange(ErrorLog.class, range, properties);
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public ErrorLog updateErrorLog(ErrorLog error) {
        try {
            entityService.save(error);
        } catch (ConstraintViolations e) {
            logger.warn("Unable to update error log: " + e.getMessage());
        } catch (SQLException e) {
            logger.warn("Unable to update error log: " + e.getMessage());

        }

        return error;
    }
}
