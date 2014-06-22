package org.openhds.errorhandling.service.impl;

import org.openhds.errorhandling.service.ErrorHandlingPropertiesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandlingPropertiesServiceImpl implements ErrorHandlingPropertiesService {

    @Value("${email.sendOnCreate}")
    private String sendOnCreate;
    @Value("${email.sendOnAnyUpdate}")
    private String sendOnAnyUpdate;
    @Value("${email.sendOnResolve}")
    private String sendOnResolve;

    @Value("${email.createMessageSubject}")
    private String createMessageSubject;
    @Value("${email.createMessageBody}")
    private String createMessageBody;
    @Value("${email.updateMessageSubject}")
    private String updateMessageSubject;
    @Value("${email.updateMessageBody}")
    private String updateMessageBody;
    @Value("${email.resolveMessageSubject}")
    private String resolveMessageSubject;
    @Value("${email.resolveMessageBody}")
    private String resolveMessageBody;
    @Value("${email.sendTo}")
    private String sendTo;

    @Override
    public String isSendOnCreate() {
        return sendOnCreate;
    }
    
    @Override
    public void setSendOnCreate(String sendOnCreate) {
        this.sendOnCreate = sendOnCreate;
    }
    public String isSendOnAnyUpdate() {
        return sendOnAnyUpdate;
    }

    @Override
    public String isSendOnResolve() {
        return sendOnResolve;
    }
    
    @Override
    public void setSendOnResolve(String sendOnResolve) {
        this.sendOnResolve = sendOnResolve;
    }
    
    @Override
    public String getCreateMessageSubject() {
        return createMessageSubject;
    }
    
    @Override
    public void setCreateMessageSubject(String createMessageSubject) {
        this.createMessageSubject = createMessageSubject;
    }
    
    @Override
    public String getCreateMessageBody() {
        return createMessageBody;
    }
    
    @Override
    public void setCreateMessageBody(String createMessageBody) {
        this.createMessageBody = createMessageBody;
    }

    @Override
    public String getResolveMessageSubject() {
        return resolveMessageSubject;
    }
    
    @Override
    public void setResolveMessageSubject(String resolveMessageSubject) {
        this.resolveMessageSubject = resolveMessageSubject;
    }
    
    @Override
    public String getResolveMessageBody() {
        return resolveMessageBody;
    }
    
    @Override
    public void setResolveMessageBody(String resolveMessageBody) {
        this.resolveMessageBody = resolveMessageBody;
    }

    @Override
    public String getSendTo() {
        return sendTo;
    }

    @Override
    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }


}
