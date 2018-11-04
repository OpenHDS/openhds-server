package org.openhds.webservice.resources;


import org.openhds.controller.service.LocationHierarchyLevelService;
import org.openhds.domain.model.wrappers.LocationHierarchyLevels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/locationhierarchylevels")
public class LocationHierarchyLevelResource {
	private LocationHierarchyLevelService locationHierarchyLevelService;

	@Autowired
	public LocationHierarchyLevelResource(LocationHierarchyLevelService locationHierarchyLevelService) {
		this.locationHierarchyLevelService = locationHierarchyLevelService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LocationHierarchyLevels getAllLevels() {
		LocationHierarchyLevels locationHierarchyLevels = new LocationHierarchyLevels();
		locationHierarchyLevels.setLocationHierarchies(locationHierarchyLevelService.getAllLevels());
		return locationHierarchyLevels;
	}


}
