package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.Form;

public interface FormService {

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    void evaluateForm(Form form) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Form> getAllActiveForms();
    
    @Authorized({PrivilegeConstants.VIEW_ENTITY})
    long getTotalFormCount(); 
}