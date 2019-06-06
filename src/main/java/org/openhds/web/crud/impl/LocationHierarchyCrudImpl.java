package org.openhds.web.crud.impl;

import javax.faces.context.FacesContext;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.LocationHierarchy;
import org.openhds.service.LocationHierarchyService;

public class LocationHierarchyCrudImpl extends EntityCrudImpl<LocationHierarchy, String> {
	
	LocationHierarchyService service;
	
	public LocationHierarchyCrudImpl(Class<LocationHierarchy> entityClass) {
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
    		service.evaluateLocationHierarchy(entityItem);	
	        return super.create();		
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
    @Override
    public String edit() {
    	
    	LocationHierarchy persistedItem = (LocationHierarchy)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
    	try {
    		service.checkLocationHierarchy(persistedItem, entityItem);	
	        return super.edit();		
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
    @Override
    public String delete() {
    	
    	LocationHierarchy persistedItem = (LocationHierarchy)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
    	try {
    		service.deleteLocationHierarchy(persistedItem);	
	        return super.delete();		
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
   	
	public LocationHierarchyService getService() {
		return service;
	}

	public void setService(LocationHierarchyService service) {
		this.service = service;
	}
}
