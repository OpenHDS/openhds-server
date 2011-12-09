package org.openhds.specialstudy.web;

import java.lang.Long;
import java.lang.String;
import javax.validation.Valid;
import org.openhds.specialstudy.domain.Individual;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect IndividualController_Roo_Controller {
    
    @RequestMapping(value = "/individual/form", method = RequestMethod.GET)
    public String IndividualController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("individual", new Individual());
        return "individual/create";
    }
    
    @RequestMapping(value = "/individual/{id}", method = RequestMethod.GET)
    public String IndividualController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("individual", Individual.findIndividual(id));
        return "individual/show";
    }
    
    @RequestMapping(value = "/individual", method = RequestMethod.GET)
    public String IndividualController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("individuals", Individual.findIndividualEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Individual.countIndividuals() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("individuals", Individual.findAllIndividuals());
        }
        return "individual/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String IndividualController.update(@Valid Individual individual, BindingResult result, ModelMap modelMap) {
        if (individual == null) throw new IllegalArgumentException("A individual is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("individual", individual);
            return "individual/update";
        }
        individual.merge();
        return "redirect:/individual/" + individual.getId();
    }
    
    @RequestMapping(value = "/individual/{id}/form", method = RequestMethod.GET)
    public String IndividualController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("individual", Individual.findIndividual(id));
        return "individual/update";
    }
    
    @RequestMapping(value = "/individual/{id}", method = RequestMethod.DELETE)
    public String IndividualController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Individual.findIndividual(id).remove();
        return "redirect:/individual?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
}
