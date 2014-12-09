package org.openhds.controller.service;

import java.util.List;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.LocationHierarchyLevel;
import org.openhds.domain.model.PrivilegeConstants;

public interface LocationHierarchyLevelService {



	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<LocationHierarchyLevel> getAllLevels();
		

}

