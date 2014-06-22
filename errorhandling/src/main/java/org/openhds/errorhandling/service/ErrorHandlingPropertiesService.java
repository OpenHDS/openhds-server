package org.openhds.errorhandling.service;

public interface ErrorHandlingPropertiesService {

    String getCreateMessageSubject();

    void setCreateMessageSubject(String createMessageSubject);

    String isSendOnCreate();

    void setSendOnCreate(String sendOnCreate);

    String isSendOnResolve();

    void setSendOnResolve(String sendOnResolve);

    String getCreateMessageBody();

    void setCreateMessageBody(String createMessageBody);

    String getResolveMessageSubject();

    void setResolveMessageSubject(String resolveMessageSubject);

    String getResolveMessageBody();

    void setResolveMessageBody(String resolveMessageBody);

    String getSendTo();

    void setSendTo(String sendTo);

}
