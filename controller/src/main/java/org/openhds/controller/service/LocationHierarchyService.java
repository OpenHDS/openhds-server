package org.openhds.controller.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;
import org.openhds.domain.model.PrivilegeConstants;

public interface LocationHierarchyService {

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Location evaluateLocation(Location entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	LocationHierarchy evaluateLocationHierarchy(LocationHierarchy entityItem) throws ConstraintViolations;
		
	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	LocationHierarchy checkLocationHierarchy(LocationHierarchy persistedItem, LocationHierarchy entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	LocationHierarchy deleteLocationHierarchy(LocationHierarchy entityItem) throws ConstraintViolations; 
	
	@Authorized({PrivilegeConstants.DELETE_ENTITY})
	Location deleteLocation(Location entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchy getHierarchyRoot();
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchyLevel getLevel(int level);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkValidParentLocation(String parent);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkValidChildLocation(String name);

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkValidParentLevel(String level);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkValidLocationEntry(String locName);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkDuplicateLocationEntry(String locName, String locLvlName);
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean checkDuplicateLocationHierarchyExtId(String extId);
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getLocationExtIds(String term);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getLocationNames(String term);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getValidLocationsInHierarchy(String locationHierarchyItem);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<LocationHierarchyLevel> getAllLevels();
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchyLevel determineParentLevel(LocationHierarchyLevel level);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchy getHierarchyItemHighestLevel(); 
			
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchyLevel getHeighestLevel();
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchyLevel getLowestLevel();
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	Location generateId(Location entityItem) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    Location findLocationById(String locationId);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	LocationHierarchy findLocationHierarchyById(String locationHierarchyId, String msg) throws Exception;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    List<Location> getAllLocations();

	@Authorized({PrivilegeConstants.CREATE_ENTITY})
    void createLocation(Location location) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<LocationHierarchy> getAllLocationHierarchies();

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Location> getAllLocationsInRange(int i, int pageSize);

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
    long getTotalLocationCount();

	@Authorized({PrivilegeConstants.EDIT_ENTITY})
	void updateLocation(Location location) throws ConstraintViolations;
}

