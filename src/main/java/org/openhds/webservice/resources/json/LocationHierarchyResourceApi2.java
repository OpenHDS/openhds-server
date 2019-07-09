package org.openhds.webservice.resources.json;
import java.util.ArrayList;
import java.util.List;

import org.openhds.service.LocationHierarchyService;
import org.openhds.domain.LocationHierarchy;
import org.openhds.domain.LocationHierarchyLevel;
import org.openhds.domain.wrappers.LocationHierarchies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/locationhierarchies2")
public class LocationHierarchyResourceApi2 {

	private LocationHierarchyService locationHierarchyService;

	@Autowired
	public LocationHierarchyResourceApi2(LocationHierarchyService locationHierarchyService) {
		this.locationHierarchyService = locationHierarchyService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LocationHierarchies getEntireLocationHierarchy() {
		List<LocationHierarchy> allLocationHierarcies = locationHierarchyService.getAllLocationHierarchies();
		List<LocationHierarchy> copies = new ArrayList<LocationHierarchy>();
		
		for (LocationHierarchy lh : allLocationHierarcies) {
			LocationHierarchy copy = new LocationHierarchy();
			copy.setUuid(lh.getUuid());
			copy.setExtId(lh.getExtId());

			LocationHierarchyLevel level = new LocationHierarchyLevel();
			level.setName(lh.getLevel().getName());
			level.setKeyIdentifier(lh.getLevel().getKeyIdentifier());
			copy.setLevel(level);
			copy.setName(lh.getName());

			LocationHierarchy parent = new LocationHierarchy();
			parent.setParent(lh.getParent().getParent());
			parent.setExtId(lh.getParent().getExtId());
			copy.setParent(parent);

			copies.add(copy);
		}

		LocationHierarchies locationHierarcies = new LocationHierarchies();
		locationHierarcies.setLocationHierarchies(copies);
		
		return locationHierarcies;
	}

}