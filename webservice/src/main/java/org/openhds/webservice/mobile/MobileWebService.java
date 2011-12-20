package org.openhds.webservice.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.openhds.dao.service.GenericDao;

/**
 * Web Services that are directly related to Mobile
 */
public class MobileWebService {
	
	GenericDao genericDao;
	
    @Context
    HttpServletRequest request;	
    
    @Path("/form-instance")
    @POST
    public Response addFormInstance(FormInstance instance) {
    	for(ValidationMessage msg : instance.getValidationMessages()) {
    		genericDao.create(msg);
    	}
    	genericDao.create(instance);
    	return Response.ok().build();
    }
    
	@Path(value="/validation")
	@POST 
	@Produces("application/json")
	public Response validationWithWs(String jsonListOfIds) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> listOfIds = null;
		try {
			listOfIds = (ArrayList<String>)mapper.readValue(jsonListOfIds, ArrayList.class);
		} catch (Exception e1) {}
		ArrayList<Map<String, Object>> errorsObj = new ArrayList<Map<String, Object>>();
		for(String id : listOfIds) {
			FormInstance errors = genericDao.findByProperty(FormInstance.class, "formInstanceId", id);
			if (errors == null) {
				addUnprocessed(id, errorsObj);
			} else {
				addErrors(errors, errorsObj);
			}
		}
		
		String response = null;
		try {
			response = mapper.writeValueAsString(errorsObj);
		} catch(Exception e) { }
		
		return Response.ok().entity(response).build();
	}

	private void addUnprocessed(String id, ArrayList<Map<String, Object>> errorsObj) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("id", id);
		obj.put("processed", "false");
		errorsObj.add(obj);
	}

	private void addErrors(FormInstance errors, ArrayList<Map<String, Object>> errorsObj) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("id", errors.getFormInstanceId());
		obj.put("processed", "true");
		if (errors.getValidationMessages().size() == 0) {
			obj.put("valid", Boolean.valueOf("true"));
		} else {
			obj.put("valid", Boolean.valueOf("false"));
			ArrayList<String> messages = new ArrayList<String>();
			
			for(ValidationMessage validationMessage : errors.getValidationMessages()) {
				messages.add(validationMessage.getMessage());
			}
			obj.put("formFailureMessages", messages);
			obj.put("fieldFailureMessages", new ArrayList<String>());
		}
		errorsObj.add(obj);
	}

	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
}
