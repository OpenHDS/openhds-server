package org.openhds.specialstudy.web;

import java.lang.Long;
import java.lang.String;
import javax.validation.Valid;
import org.openhds.specialstudy.domain.Visit;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect VisitController_Roo_Controller {
    
    @RequestMapping(value = "/visit/form", method = RequestMethod.GET)
    public String VisitController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("visit", new Visit());
        return "visit/create";
    }
    
    @RequestMapping(value = "/visit/{id}", method = RequestMethod.GET)
    public String VisitController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("visit", Visit.findVisit(id));
        return "visit/show";
    }
    
    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    public String VisitController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("visits", Visit.findVisitEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Visit.countVisits() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("visits", Visit.findAllVisits());
        }
        return "visit/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String VisitController.update(@Valid Visit visit, BindingResult result, ModelMap modelMap) {
        if (visit == null) throw new IllegalArgumentException("A visit is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("visit", visit);
            return "visit/update";
        }
        visit.merge();
        return "redirect:/visit/" + visit.getId();
    }
    
    @RequestMapping(value = "/visit/{id}/form", method = RequestMethod.GET)
    public String VisitController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("visit", Visit.findVisit(id));
        return "visit/update";
    }
    
    @RequestMapping(value = "/visit/{id}", method = RequestMethod.DELETE)
    public String VisitController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Visit.findVisit(id).remove();
        return "redirect:/visit?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
}
