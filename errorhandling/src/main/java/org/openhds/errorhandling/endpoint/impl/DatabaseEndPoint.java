package org.openhds.errorhandling.endpoint.impl;

import org.openhds.errorhandling.dao.ErrorLogDAO;
import org.openhds.errorhandling.domain.ErrorLog;
import org.openhds.errorhandling.endpoint.ErrorServiceEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseEndPoint implements ErrorServiceEndPoint {

    @Autowired
    private ErrorLogDAO errorLogDAO;

    @Override
    public void logError(ErrorLog errorLog) {
        errorLogDAO.createErrorLog(errorLog);
    }

    @Override
    public void updateError(ErrorLog errorLog) {
        errorLogDAO.updateErrorLog(errorLog);
    }

}
