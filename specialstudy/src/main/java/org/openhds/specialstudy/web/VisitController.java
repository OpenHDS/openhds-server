package org.openhds.specialstudy.web;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.apache.cxf.jaxrs.client.WebClient;
import org.openhds.specialstudy.domain.Visit;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "visit", automaticallyMaintainView = true, update = true, formBackingObject = Visit.class)
@RequestMapping("/visit/**")
@Controller
public class VisitController {
	
	@Autowired
	WebClient webClient;
		
    @RequestMapping(value = "/visit", method = RequestMethod.POST)
    public String create(@Valid Visit visit, BindingResult result, ModelMap modelMap) {
    	
    	Response response;
		webClient.reset();
    	
    	try {
    		response = webClient.path("/visit/{id}", visit.getExtId().trim()).get();
    	} catch (Exception e) {
    		ObjectError oe = new ObjectError("visit", "Unable to connect to the OpenHDS web service.");
        	result.addError(oe);
            modelMap.addAttribute("visit", visit);
            return "visit/create";
    	}
    		
    	if (response.getStatus() != 200) {
        	ObjectError oe = new ObjectError("visit", "The request could not be fullfilled. Ensure that the visit id is valid.");
        	result.addError(oe);
            modelMap.addAttribute("visit", visit);
            return "visit/create";
        }
        visit.persist(); 
        return "redirect:/visit/" + visit.getId();
    }
}
