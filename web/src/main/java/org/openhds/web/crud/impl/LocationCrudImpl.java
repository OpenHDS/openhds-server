package org.openhds.web.crud.impl;

import javax.faces.context.FacesContext;
import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Location;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.binding.message.MessageContext;

public class LocationCrudImpl extends EntityCrudImpl<Location, String> {

    SitePropertiesService siteProperties;
	LocationHierarchyService service;

	public LocationCrudImpl(Class<Location> entityClass) {
        super(entityClass);
    }
    
    @Override
    public String create() {

        try {
            service.evaluateLocation(entityItem);
            super.create();
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
    	
    	Location persistedItem = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
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
            service.evaluateLocation(entityItem);
            return super.commit(messageContext);
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
