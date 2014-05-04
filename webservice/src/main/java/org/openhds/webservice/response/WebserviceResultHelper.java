package org.openhds.webservice.response;

import org.openhds.controller.exception.ConstraintViolations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebserviceResultHelper {


    public static ResponseEntity<WebserviceResult> constraintViolationResponse(ConstraintViolations cv, String msg) {
        WebserviceResult result = new WebserviceResult();
        result.addDataElement("constraintViolations", cv.getViolations());
        result.setResultCode(2);
        result.setStatus("error");
        result.setResultMessage(msg);
        return new ResponseEntity<WebserviceResult>(result, HttpStatus.BAD_REQUEST);
	}
}
