package org.openhds.webservice.resources.api2;


import org.openhds.controller.service.LocationHierarchyLevelService;
import org.openhds.domain.model.wrappers.LocationHierarchyLevels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/locationhierarchylevels2")
public class LocationHierarchyLevelResourceApi2 {
    private LocationHierarchyLevelService locationHierarchyLevelService;

    @Autowired
    public LocationHierarchyLevelResourceApi2(LocationHierarchyLevelService locationHierarchyLevelService) {
        this.locationHierarchyLevelService = locationHierarchyLevelService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public LocationHierarchyLevels getAllLevels() {
    	LocationHierarchyLevels locationHierarchyLevels = new LocationHierarchyLevels();
    	locationHierarchyLevels.setLocationHierarchies(locationHierarchyLevelService.getAllLevels());
        return locationHierarchyLevels;
    }
    

}
