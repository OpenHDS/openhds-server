package org.openhds.controller.idgeneration;

import java.util.HashMap;
import java.util.List;

import org.openhds.controller.util.OpenHDSResult;
import org.openhds.domain.service.SitePropertiesService;

public class IdValidator {
	
	private static final String BAD_ID_FORMAT = "Bad Id Format: An Id can only contain alphanumeric characters";
    private static final String INVALID_CHECKCHAR_LOCATION = "Check character doesn't match the provided Location id";
    private static final String INVALID_CHECKCHAR_VISIT = "Check character doesn't match the provided Visit id";
    private static final String INVALID_CHECKCHAR_SOCIALGROUP = "Check character doesn't match the provided Social Group id";
    private static final String INVALID_CHECKCHAR_INDIVIDUAL = "Check character doesn't match the provided Individual id";
    private static final String INVALID_CHECKCHAR_FIELDWORKER = "Check character doesn't match the provided Field Worker id";

    LuhnValidator luhnValidator;
    SitePropertiesService siteProperties;
    IdSchemeResource resource;
    
    public IdValidator(LuhnValidator luhnValidator, SitePropertiesService siteProperties, IdSchemeResource resource) {
    	this.luhnValidator = luhnValidator;
    	this.siteProperties = siteProperties;
    	this.resource = resource;
    }

	public OpenHDSResult evaluateCheckDigits(HashMap<String, List<String>> map) {	
		OpenHDSResult result = new OpenHDSResult();
		
    	try {	
	    	for (String item : map.keySet()) {
	    		if (item == "Location") {
	    			if (checkIdSchemeForCheckDigit(item) && (!validateId(map.get(item)))) {
	    				result.setSuccess(false);
	    				result.setFailureReason(INVALID_CHECKCHAR_LOCATION);
	        			return result;
	    			}
	    		}
	    		if (item == "FieldWorker") {
		    		if (checkIdSchemeForCheckDigit(item) && !validateId(map.get(item))) {
		    			result.setSuccess(false);
		    			result.setFailureReason(INVALID_CHECKCHAR_FIELDWORKER);
		    			return result;
		    		}
	    		}
	    		if (item == "Visit") {
	    			if (checkIdSchemeForCheckDigit(item) && !validateId(map.get(item))) {
		     			result.setSuccess(false);
		     			result.setFailureReason(INVALID_CHECKCHAR_VISIT);
		     			return result;
	    			}
	    		}
	    		if (item == "SocialGroup") {
	    			if (checkIdSchemeForCheckDigit(item) && !validateId(map.get(item))) {
	    				result.setSuccess(false);
	    				result.setFailureReason(INVALID_CHECKCHAR_SOCIALGROUP);
	    				return result;
	    			}
	    		}
	    		if (item == "Individual") {
	    			if (checkIdSchemeForCheckDigit(item) && !validateId(map.get(item)))
	    				result.setSuccess(false);
	    				result.setFailureReason(INVALID_CHECKCHAR_INDIVIDUAL);
		     			return result;
	    		}
	    	}
    	} catch (Exception e) {
    		result.setSuccess(false);
    		result.setFailureReason(BAD_ID_FORMAT);
    		return result;
    	}
    	result.setSuccess(true);
        return result;
    }
    
    public boolean validateId(List<String> ids) {    	
    	for (String item : ids) {
    		
    		if (item.equals(siteProperties.getUnknownIdentifier()))
    			continue;
    		
    		if (!luhnValidator.validateCheckCharacter(item))
        		return false;
    	}	
    	return true;
    }
    
    private boolean checkIdSchemeForCheckDigit(String name) {
    	return resource.getIdSchemeByName(name).isCheckDigit();
    }
}
