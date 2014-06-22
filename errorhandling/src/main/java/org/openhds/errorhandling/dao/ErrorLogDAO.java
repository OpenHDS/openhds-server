package org.openhds.errorhandling.dao;

import java.util.List;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.domain.model.FieldWorker;
import org.openhds.errorhandling.domain.ErrorLog;

public interface ErrorLogDAO {

    ErrorLog createErrorLog(ErrorLog error);
    
    ErrorLog updateErrorLog(ErrorLog error);
    
    List<ErrorLog> findByResolutionStatus(String resolutionStatus);
    
    List<ErrorLog> findByAssignedTo(String assignedTo);
    
    List<ErrorLog> findByFieldWorker(FieldWorker fieldWorker);
    
    List<ErrorLog> findAllByFilters(ValueProperty... properties);
    
    ErrorLog findById(String uuid);
}
