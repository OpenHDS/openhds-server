package org.openhds.controller.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Form;

public interface FormService {

    @Authorized({ PrivilegeConstants.CREATE_ENTITY })
    void evaluateForm(Form form) throws ConstraintViolations;

    @Authorized({ PrivilegeConstants.VIEW_ENTITY })
    List<Form> getAllActiveForms();
    
    @Authorized({PrivilegeConstants.VIEW_ENTITY})
    long getTotalFormCount(); 
}