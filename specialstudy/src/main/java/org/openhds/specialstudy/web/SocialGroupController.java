package org.openhds.specialstudy.web;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.apache.cxf.jaxrs.client.WebClient;
import org.openhds.specialstudy.domain.SocialGroup;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "socialgroup", automaticallyMaintainView = true, update = true, formBackingObject = SocialGroup.class)
@RequestMapping("/socialgroup/**")
@Controller
public class SocialGroupController {
	
	@Autowired
	WebClient webClient;
			
	@RequestMapping(value = "/socialgroup", method = RequestMethod.POST)
    public String create(@Valid SocialGroup socialGroup, BindingResult result, ModelMap modelMap) {
		
		Response response;
		webClient.reset();
		
		try {
			response = webClient.path("/socialgroup/{id}", socialGroup.getExtId().trim()).get();
		} catch (Exception e) {
			ObjectError oe = new ObjectError("socialgroup", "Unable to connect to the OpenHDS web service.");
        	result.addError(oe);
            modelMap.addAttribute("socialgroup", socialGroup);
            return "socialgroup/create";
		}
			
        if (response.getStatus() != 200) {
        	ObjectError oe = new ObjectError("socialgroup", "The request could not be fullfilled. Ensure that the socialgroup id is valid.");
        	result.addError(oe);
            modelMap.addAttribute("socialgroup", socialGroup);
            return "socialgroup/create";
        }
        socialGroup.persist(); 
        return "redirect:/socialgroup/" + socialGroup.getId();
    }
	
	 @RequestMapping(value = "/socialgroup/form", method = RequestMethod.GET)
	    public String createForm(ModelMap modelMap, SessionStatus sessionStatus) {
	        sessionStatus.setComplete();
	        modelMap.addAttribute("socialGroup", new SocialGroup());
	        return "socialgroup/create";
	    }
	       
	    @RequestMapping(method = RequestMethod.PUT)
	    public String update(@Valid SocialGroup socialGroup, BindingResult result, ModelMap modelMap) {
	        SocialGroup sg = SocialGroup.findSocialGroup(socialGroup.getId());
	        socialGroup.setExtId(sg.getExtId());
	        socialGroup.merge();
	        return "redirect:/socialgroup/" + socialGroup.getId();
	    }
	    
	    @RequestMapping(value = "/socialgroup/{id}/form", method = RequestMethod.GET)
	    public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
	        if (id == null) throw new IllegalArgumentException("An Identifier is required");
	        modelMap.addAttribute("socialGroup", SocialGroup.findSocialGroup(id));
	        return "socialgroup/update";
	    }
	    
	    @RequestMapping(value = "/socialgroup/list", method = RequestMethod.GET)
	    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap, SessionStatus sessionStatus) {
	    	sessionStatus.setComplete();
	    	if (page != null || size != null) {
	            int sizeNo = size == null ? 10 : size.intValue();
	            modelMap.addAttribute("socialgroups", SocialGroup.findSocialGroupEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
	            float nrOfPages = (float) SocialGroup.countSocialGroups() / sizeNo;
	            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	        } else {
	            modelMap.addAttribute("socialgroups", SocialGroup.findAllSocialGroups());
	        }
	        return "socialgroup/list";
	    }
	    
	    @RequestMapping(value = "/socialgroup/{id}/list", method = RequestMethod.GET)
	    public String list(@PathVariable("id") String id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
	    	modelMap.addAttribute("fromFilteredList", true);
	    	modelMap.addAttribute("extId", id);
	    	if (page != null || size != null) {
	            int sizeNo = size == null ? 10 : size.intValue();
	            modelMap.addAttribute("socialgroups", SocialGroup.findSocialGroupEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
	            float nrOfPages = (float) SocialGroup.countSocialGroups() / sizeNo;
	            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	        } else {
	            modelMap.addAttribute("socialgroups", SocialGroup.findAllSocialGroupsByHouseholdId(id));
	        }
	        return "socialgroup/list";
	    }
	    
	    @RequestMapping(value = "/socialgroup/{id}", method = RequestMethod.GET)
	    public String show(@PathVariable("id") Long id, ModelMap modelMap) {
	        if (id == null) throw new IllegalArgumentException("An Identifier is required");
	        modelMap.addAttribute("socialGroup", SocialGroup.findSocialGroup(id));
	        return "socialgroup/show";
	    }
	    
	    @RequestMapping(value = "/socialgroup/{id}", method = RequestMethod.DELETE)
	    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
	        if (id == null) throw new IllegalArgumentException("An Identifier is required");
	        SocialGroup.findSocialGroup(id).remove();
	        return "redirect:/socialgroup/list";
	    }
}
