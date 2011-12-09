package org.openhds.specialstudy.web;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.apache.cxf.jaxrs.client.WebClient;
import org.openhds.specialstudy.domain.Individual;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "individual", automaticallyMaintainView = true, update = true, formBackingObject = Individual.class)
@RequestMapping("/individual/**")
@Controller
public class IndividualController {
	
	@Autowired
	WebClient webClient;
	
    @RequestMapping(value = "/individual", method = RequestMethod.POST)
    public String create(@Valid Individual individual, BindingResult result, ModelMap modelMap) {
    	
    	Response response;
		webClient.reset();
    	
    	try {
    		response = webClient.path("/individual/{id}", individual.getExtId().trim()).get();
    	} catch (Exception e) {
    		ObjectError oe = new ObjectError("individual", "Unable to connect to the OpenHDS web service.");
        	result.addError(oe);
            modelMap.addAttribute("individual", individual);
            return "individual/create";
    	}
    		
    	if (response.getStatus() != 200) {
        	ObjectError oe = new ObjectError("individual", "The request could not be fullfilled. Ensure that the individual id is valid.");
        	result.addError(oe);
            modelMap.addAttribute("individual", individual);
            return "individual/create";
        }
        individual.persist(); 
        return "redirect:/individual/" + individual.getId();
    }
}
