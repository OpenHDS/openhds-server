package org.openhds.specialstudy.web;

import java.lang.Long;
import java.lang.String;
import javax.validation.Valid;
import org.openhds.specialstudy.domain.Location;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect LocationController_Roo_Controller {
    
    @RequestMapping(value = "/location/form", method = RequestMethod.GET)
    public String LocationController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("location", new Location());
        return "location/create";
    }
    
    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    public String LocationController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("location", Location.findLocation(id));
        return "location/show";
    }
    
    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public String LocationController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("locations", Location.findLocationEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Location.countLocations() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("locations", Location.findAllLocations());
        }
        return "location/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String LocationController.update(@Valid Location location, BindingResult result, ModelMap modelMap) {
        if (location == null) throw new IllegalArgumentException("A location is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("location", location);
            return "location/update";
        }
        location.merge();
        return "redirect:/location/" + location.getId();
    }
    
    @RequestMapping(value = "/location/{id}/form", method = RequestMethod.GET)
    public String LocationController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("location", Location.findLocation(id));
        return "location/update";
    }
    
    @RequestMapping(value = "/location/{id}", method = RequestMethod.DELETE)
    public String LocationController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Location.findLocation(id).remove();
        return "redirect:/location?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
}
