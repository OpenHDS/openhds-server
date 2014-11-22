package org.openhds.web.crud.impl;

import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.FieldWorker;
import org.openhds.controller.service.FieldWorkerService;

public class FieldWorkerCrudImpl extends EntityCrudImpl<FieldWorker, String> {

	FieldWorkerService service;

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
    		service.evaluateFieldWorker(entityItem);
    	 // service.makeTheHash()
	        return super.create();
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	} catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
	public FieldWorkerService getService() {
		return service;
	}

	public void setService(FieldWorkerService service) {
		this.service = service;
	}
}

