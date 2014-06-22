package org.openhds.webservice.response;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.webservice.response.constants.ResultCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebserviceResultHelper {
    
    public static final String CONSTRAINT_KEY = "constraintViolations";

    public static ResponseEntity<WebserviceResult> genericConstraintResponse(ConstraintViolations cv) {
        return constraintViolationResponse(cv, "There were " + cv.getViolations().size() + " constraint violations", "error", HttpStatus.BAD_REQUEST, ResultCodes.CONSTRAINT_VIOLATIONS_CODE);
    }
        public static ResponseEntity<WebserviceResult> constraintViolationResponse(ConstraintViolations cv, String msg, String status, HttpStatus httpResponseCode, int resultCode) {
        WebserviceResult result = new WebserviceResult();
        result.addDataElement(CONSTRAINT_KEY, cv.getViolations());
        result.setResultCode(resultCode);
        result.setStatus(status);
        result.setResultMessage(msg);
        return new ResponseEntity<WebserviceResult>(result, httpResponseCode);
	}

    public static ResponseEntity<WebserviceResult> entityNotFoundResponse(String msg, String status) {
        WebserviceResult result = new WebserviceResult();
        result.setResultCode(ResultCodes.ENTITY_NOT_FOUND_CODE);
        result.setStatus(status);
        result.setResultMessage(msg);
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.NOT_FOUND);
    }
}
