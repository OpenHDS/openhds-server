package org.openhds.web.crud.impl;

import javax.faces.context.FacesContext;
import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Location;
import org.openhds.service.LocationHierarchyService;
import org.openhds.service.SitePropertiesService;
import org.springframework.binding.message.MessageContext;

public class LocationCrudImpl extends EntityCrudImpl<Location, String> {

    SitePropertiesService siteProperties;
	LocationHierarchyService service;

	public LocationCrudImpl(Class<Location> entityClass) {
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
            service.createLocation(entityItem);
            return onCreateComplete();
        } catch (ConstraintViolations e) {
            jsfService.addError(e.getMessage());
        } catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
        return null;
    }
    
    @Override
    public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:locationEdit";
    	}
    	return outcome;
    }
    
    @Override
    public String delete() {
    	
    	Location persistedItem = (Location)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
    	try {
    		service.deleteLocation(persistedItem);
    		super.delete();
    	} catch (ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}	
    	return null;
    }
    
    @Override
    public boolean commit(MessageContext messageContext) {
        try {
            service.createLocation(entityItem);
            return true;
        } catch (ConstraintViolations e) {
            webFlowService.createMessage(messageContext, e.getMessage());
        }
        return false;
    }
        
    public SitePropertiesService getLocationLevels() {
        return siteProperties;
    }

    public LocationHierarchyService getService() {
        return service;
    }

    public void setService(LocationHierarchyService service) {
        this.service = service;
    }
    
    public SitePropertiesService getSiteProperties() {
		return siteProperties;
	}

	public void setSiteProperties(SitePropertiesService siteProperties) {
		this.siteProperties = siteProperties;
	}
}
