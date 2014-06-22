package org.openhds.errorhandling.endpoint.impl;

import java.text.MessageFormat;
import java.util.List;

import org.openhds.errorhandling.constants.ErrorConstants;
import org.openhds.errorhandling.domain.Error;
import org.openhds.errorhandling.domain.ErrorLog;
import org.openhds.errorhandling.endpoint.ErrorServiceEndPoint;
import org.openhds.errorhandling.service.ErrorHandlingPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailEndPoint implements ErrorServiceEndPoint {

    private static final String CREATE = "ERROR_CREATED";

    private static final String RESOLVE = "ERROR_RESOLVED";

    @Autowired
    private JavaMailSender mailService;

    @Autowired
    private ErrorHandlingPropertiesService errorProperties;

    @Override
    public void logError(ErrorLog errorLog) {
        if (errorProperties.isSendOnCreate().equalsIgnoreCase("true")) {
            sendEmail(EmailEndPoint.CREATE, errorLog);
        }
    }

    /**
     * Currently there is no support for sending e-mails on updated errors
     * This should be easy to add, but some consideration should go into what
     * information to display, whether the delta changes are shown, etc
     */
    @Override
    public void updateError(ErrorLog errorLog) {
        if (errorProperties.isSendOnResolve().equalsIgnoreCase("true") &&
                errorLog.getResolutionStatus().equals(ErrorConstants.RESOLVED_ERROR_STATUS)) {
            sendEmail(EmailEndPoint.RESOLVE, errorLog);
        }
        return;
    }

    private void sendEmail(String type, ErrorLog errorLog) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(errorProperties.getSendTo());
        switch (type) {
        case EmailEndPoint.CREATE:
            emailMessage.setText(MessageFormat.format(errorProperties.getCreateMessageBody(), marshalErrors(errorLog.getErrors()), errorLog.getFieldWorker().getExtId(), errorLog.getUuid()));
            emailMessage.setSubject(MessageFormat.format(errorProperties.getCreateMessageSubject(), errorLog.getEntityType())); break;
        case EmailEndPoint.RESOLVE:
            emailMessage.setText(MessageFormat.format(errorProperties.getResolveMessageBody(), errorLog.getUuid(), errorLog.getFieldWorker().getExtId(), errorLog.getAssignedTo()));
            emailMessage.setSubject(MessageFormat.format(errorProperties.getResolveMessageSubject(), errorLog.getEntityType())); break;
        }

        mailService.send(emailMessage);
    }

    private String marshalErrors(List<Error> errors) {
        StringBuilder errorString = new StringBuilder();
        for (Error error : errors) {
            errorString.append(error.getErrorMessage() + "\n");
        }
        return errorString.toString();
    }
}
