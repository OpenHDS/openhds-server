package org.openhds.web.crud.impl;

import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.service.refactor.FieldWorkerCrudService;
import org.openhds.domain.FieldWorker;
import org.springframework.beans.factory.annotation.Autowired;

public class FieldWorkerCrudImpl extends EntityCrudImpl<FieldWorker, String> {

	@Autowired
	private FieldWorkerCrudService fieldWorkerCrudService;

	public FieldWorkerCrudImpl(Class<FieldWorker> entityClass) {
        super(entityClass);
    }
	
	@Override
	public String createSetup() {
        reset(false, true);
        showListing=false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }


    @Override
    public String create() {

    	try {
    		//fieldWorkerService.evaluateFieldWorker(entityItem);
    	 // service.makeTheHash()
    		fieldWorkerCrudService.generateId(entityItem);
    		fieldWorkerCrudService.generatePasswordHash(entityItem);
    		fieldWorkerCrudService.isEligibleForCreation(entityItem, new ConstraintViolations());
	        return super.create();
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	} catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
    @Override
    public String edit() {

    	try {
    		//Only try to update password if at least one of the password fields is filled. Otherwise we'll just update name
    		if(entityItem.getPassword().trim().length() > 0 || entityItem.getConfirmPassword().trim().length() > 0){
    			fieldWorkerCrudService.generatePasswordHash(entityItem);
    		}
	        return super.edit();
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	} catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }    
    
	public FieldWorkerCrudService getFieldWorkerService() {
		return fieldWorkerCrudService;
	}

	public void setFieldWorkerService(FieldWorkerCrudService fieldWorkerService) {
		this.fieldWorkerCrudService = fieldWorkerService;
	}
}

