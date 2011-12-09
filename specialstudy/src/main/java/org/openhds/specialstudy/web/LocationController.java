package org.openhds.specialstudy.web;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.apache.cxf.jaxrs.client.WebClient;
import org.openhds.specialstudy.domain.Location;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "location", automaticallyMaintainView = true, update = true, formBackingObject = Location.class)
@RequestMapping("/location/**")
@Controller
public class LocationController {
	
	@Autowired
	WebClient webClient;
		
    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public String create(@Valid Location location, BindingResult result, ModelMap modelMap) {
    	
    	Response response;
    	webClient.reset();
		
    	try {
    		response = webClient.path("/location/{id}", location.getExtId().trim()).get();
    	} catch (Exception e) {
    		ObjectError oe = new ObjectError("location", "Unable to connect to the OpenHDS web service.");
        	result.addError(oe);
            modelMap.addAttribute("location", location);
            return "location/create";
    	}
    		
    	if (response.getStatus() != 200) {
        	ObjectError oe = new ObjectError("location", "The request could not be fullfilled. Ensure that the location id is valid.");
        	result.addError(oe);
            modelMap.addAttribute("location", location);
            return "location/create";
        }
        location.persist();
        return "redirect:/location/" + location.getId();
    }
}
