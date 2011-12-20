package org.openhds.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.FieldWorkerService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.controller.service.VisitService;
import org.openhds.domain.model.PrivilegeConstants;

@Produces("application/xml")
public class SpecialStudyWebServiceImpl {
	
	IndividualService individualService;
	FieldWorkerService fieldWorkerService;
	LocationHierarchyService locationService;
	VisitService visitService;
	SocialGroupService socialGroupService;
	CurrentUser currentUser;
		
    private static final String INVALID_SOCIAL_GROUP = "No Social Group Record Found";
    private static final String INVALID_LOCATION_ID = "Invalid Location Id";
    private static final String INVALID_VISIT_ID = "Invalid Visit Id";
    private static final String INDIVIDUAL_ID_NOT_FOUND = "Invalid Individual Id";
    private static final String PROXY_USER = "SpecialStudyWebService";
    private static final String PROXY_USER_PASS = "SpecialPass";
    private static final String[] PROXY_USER_PRIVILEGES = {PrivilegeConstants.VIEW_ENTITY};
	
	@GET
	@Path("/individual/{id}")
	public Response validateIndividual(@PathParam("id") String id) { 	
    	setProxyUser();

		try {
			individualService.findIndivById(id, INDIVIDUAL_ID_NOT_FOUND);					
			return Response.ok().build();
			
		} catch (Exception e) {
			return Response.status(400).build();
		}
	}
	
	@GET
	@Path("/location/{id}")
	public Response validateLocation(@PathParam("id") String id) {
    	setProxyUser();
    	
		try {
			locationService.findLocationById(id, INVALID_LOCATION_ID);	
			return Response.ok().build();

		} catch (Exception e) {
			return Response.status(400).build();
		}
	}
	
	@GET
	@Path("/socialgroup/{id}")
	public Response validateSocialGroup(@PathParam("id") String id) {
    	setProxyUser();
    	
    	try {
			socialGroupService.findSocialGroupById(id, INVALID_SOCIAL_GROUP);		
			return Response.ok().build();
			
		} catch (Exception e) {
			return Response.status(400).build();
		}
	}
	
	@GET
	@Path("/visit/{id}")
	public Response validateVisit(@PathParam("id") String id) {
    	setProxyUser();
    	
    	try {
			visitService.findVisitById(id, INVALID_VISIT_ID);
			return Response.ok().build();
			
		} catch (Exception e) {
			return Response.status(400).build();
		}
	}
    
    /**
     * Set a proxy User so that clients of this web service have read only privileges
     * This allows any client to access these web services without logging in, but they will only have
     * read only privileges.
     */
    private void setProxyUser() {
    	currentUser.setProxyUser(PROXY_USER, PROXY_USER_PASS, PROXY_USER_PRIVILEGES);
    }
    
	public void setIndividualService(IndividualService individualService) {
		this.individualService = individualService;
	}

	public void setFieldWorkerService(FieldWorkerService fieldWorkerService) {
		this.fieldWorkerService = fieldWorkerService;
	}

	public void setLocationService(LocationHierarchyService locationService) {
		this.locationService = locationService;
	}

	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	public void setSocialGroupService(SocialGroupService socialGroupService) {
		this.socialGroupService = socialGroupService;
	}

	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
}
