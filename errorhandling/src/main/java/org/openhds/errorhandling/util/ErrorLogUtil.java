package org.openhds.errorhandling.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openhds.domain.model.FieldWorker;
import org.openhds.errorhandling.domain.Error;
import org.openhds.errorhandling.domain.ErrorLog;

public class ErrorLogUtil {

    public static final ErrorLog generateErrorLog(String assignedTo, String dataPayload, Calendar dateOfResolution,
            String entityType, FieldWorker fieldWorker, String resolutionStatus, List<String> errors) {
        ErrorLog errorLog = new ErrorLog();

        errorLog.setAssignedTo(assignedTo);
        errorLog.setDataPayload(dataPayload);
        errorLog.setDateOfResolution(dateOfResolution);
        errorLog.setEntityType(entityType);
        errorLog.setFieldWorker(fieldWorker);
        errorLog.setResolutionStatus(resolutionStatus);
        errorLog.setErrors(generateErrors(errors));

        return errorLog;
    }

    private static List<Error> generateErrors(List<String> errors) {
        List<Error> objectErrors = new ArrayList<Error>();

        for (String error : errors) {
            Error objectError = new Error(error);
            objectErrors.add(objectError);
        }

        return objectErrors;
    }
}
