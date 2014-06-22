package org.openhds.errorhandling.endpoint;

import org.openhds.errorhandling.domain.ErrorLog;

public interface ErrorServiceEndPoint {

    void logError(ErrorLog errorLog);

    void updateError(ErrorLog errorLog);
}
