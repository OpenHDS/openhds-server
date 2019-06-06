package org.openhds.web.crud.impl;

import java.sql.SQLException;
import javax.faces.context.FacesContext;
import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Location;
import org.openhds.domain.SocialGroup;
import org.openhds.service.IndividualService;
import org.openhds.service.SocialGroupService;
import org.springframework.binding.message.MessageContext;

public class SocialGroupCrudImpl extends EntityCrudImpl<SocialGroup, String> {

	SocialGroupService socialGroupService;
	IndividualService individualService;
	private Location location;
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public SocialGroupCrudImpl(Class<SocialGroup> entityClass) {
		super(entityClass);
	}

	@Override
    public String create() {
    	try {
    		socialGroupService.createSocialGroup(entityItem, location);		
    		this.setLocation(null);
	        return onCreateComplete();
    	}		
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
		} 
    	return null;
    }
	
    @Override
    public String edit() {
    	
    	SocialGroup persistedItem = (SocialGroup)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));

        try {
        	socialGroupService.checkSocialGroup(persistedItem, entityItem);
        	String outcome = super.edit();
        	
        	if (outcome != null) {
        		return "pretty:socialgroupEdit";
        	}
        	
        	return null;
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
		} catch(Exception e) {
        	jsfService.addError(e.getMessage());
		}

        return null;
    }
    
    @Override
    public String delete() {
    	SocialGroup sg = (SocialGroup)converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
        try {
        	socialGroupService.deleteSocialGroup(sg);
		} catch (SQLException e) {
			jsfService.addError("Could not delete the persistent entity");
		} catch (AuthorizationException e) {
			jsfService.addError(e.getMessage());
			return null;
		}

        return listSetup();
    }
	
    @Override
    public boolean commit(MessageContext messageContext) {
    	try {
    		socialGroupService.createSocialGroup(entityItem,location);
    		return true;
    	} catch(ConstraintViolations e) {
    		webFlowService.createMessage(messageContext, e.getMessage());
    	}
    	
    	return false;
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
        
    public boolean validateSocialGroup(MessageContext messageContext) {
    	try {
    		socialGroupService.evaluateSocialGroup(entityItem);
		} catch (ConstraintViolations e) {
			webFlowService.createMessage(messageContext, e.getMessage());
			return false;
		}    	
		
		return true;
    }
                
	public SocialGroupService getSocialGroupService() {
		return socialGroupService;
	}

	public void setSocialGroupService(SocialGroupService socialGroupService) {
		this.socialGroupService = socialGroupService;
	}
	
	public IndividualService getIndividualService() {
		return individualService;
	}

	public void setIndividualService(IndividualService individualService) {
		this.individualService = individualService;
	}
}
