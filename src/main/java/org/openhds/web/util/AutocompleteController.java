package org.openhds.web.util;

import java.util.List;
import org.openhds.service.FieldWorkerService;
import org.openhds.service.IndividualService;
import org.openhds.service.LocationHierarchyService;
import org.openhds.service.SocialGroupService;
import org.openhds.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AutocompleteController {
	
	LocationHierarchyService locationService;
	FieldWorkerService fieldWorkerService;
	IndividualService individualService;
	VisitService visitService;
	SocialGroupService socialGroupService;
	
	@Autowired
	public AutocompleteController(LocationHierarchyService locationService, FieldWorkerService fieldWorkerService, 
			IndividualService individualService, VisitService visitService, SocialGroupService socialGroupService) {
		this.locationService = locationService;
		this.fieldWorkerService = fieldWorkerService;
		this.individualService = individualService;
		this.visitService = visitService;
		this.socialGroupService = socialGroupService;
	}
	
	@RequestMapping(value="/locationName.autocomplete")
	public @ResponseBody List<String> getLocationIds(@RequestParam("term") String term) {	
		return locationService.getLocationNames(term);
	}
	
	@RequestMapping(value="/location.autocomplete")
	public @ResponseBody List<String> getLocationLevels(@RequestParam("term") String term) {	
		return locationService.getLocationExtIds(term);
	}
	
	@RequestMapping(value="/collectedBy.autocomplete")
	public @ResponseBody List<String> getCollectedByIds(@RequestParam("term") String term) {	
		return fieldWorkerService.getFieldWorkerExtIds(term);
	}
	
	@RequestMapping(value="/individual.autocomplete")
	public @ResponseBody List<String> getIndividualIds(@RequestParam("term") String term) {	
		return individualService.getIndividualExtIds(term);
	}
	
	@RequestMapping(value="/visit.autocomplete")
	public @ResponseBody List<String> getVisitIds(@RequestParam("term") String term) {	
		return visitService.getVisitExtIds(term);
	}
	
	@RequestMapping(value="/socialGroup.autocomplete")
	public @ResponseBody List<String> getSocialGroupIds(@RequestParam("term") String term) {	
		return socialGroupService.getSocialGroupExtIds(term);
	}
}
