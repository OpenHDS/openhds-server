package org.openhds.service;

import java.util.List;

import org.openhds.annotations.Authorized;
import org.openhds.domain.LocationHierarchyLevel;
import org.openhds.domain.PrivilegeConstants;

public interface LocationHierarchyLevelService {



	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<LocationHierarchyLevel> getAllLevels();
		

}

