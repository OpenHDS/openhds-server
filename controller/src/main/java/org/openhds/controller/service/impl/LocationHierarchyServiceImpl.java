package org.openhds.controller.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.idgeneration.Generator;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;

@SuppressWarnings("unchecked")
public class LocationHierarchyServiceImpl implements LocationHierarchyService {

	private GenericDao genericDao;
	private Generator locationGenerator;
	private Generator locationHierarchyGenerator;
    private EntityService entityService;
	
	public LocationHierarchyServiceImpl(GenericDao genericDao, EntityService entityService, Generator locationGenerator, Generator locationHierarchyGenerator) {
		this.genericDao = genericDao;
		this.entityService = entityService;
		this.locationGenerator = locationGenerator;
		this.locationHierarchyGenerator = locationHierarchyGenerator;
	}

	public LocationHierarchy evaluateLocationHierarchy(LocationHierarchy entityItem) throws ConstraintViolations {
		
		LocationHierarchy item = null;
		
		// when creating a Location Hierarchy item, the Parent may or may not be specified
		// if the Parent is specified, proceed with checking the normal constraints
		if (entityItem.getParent().getExtId() != null) { 
	    	if (!checkValidParentLocation(entityItem.getParent().getName())) 
	    		throw new ConstraintViolations("The Parent Location name does not exist.");	
	    	else if (!checkValidChildLocation(entityItem.getName())) 
	    		throw new ConstraintViolations("The Child Location Name already exists.");	
	    	else if (!checkValidParentLevel(entityItem.getParent().getName())) 
	    		throw new ConstraintViolations("The Parent Location specified " +
	    		"exceeds the deepest level in the hierarchy.");
	    	else if (!checkDuplicateLocationHierarchyExtId(entityItem.getExtId()))
	    		throw new ConstraintViolations("The Location Hierarchy Id specified already exists.");
	    	
	    	// get the Parent
	    	item = genericDao.findByProperty(LocationHierarchy.class, "name", entityItem.getParent().getName());
		}
    	    		
		// set the external id on the hierarchy item
		generateId(entityItem);
		
		// the Parent item is the root so this Hierarchy item may or may not be at the highest level
		if (item == null) {
			
			// set the level to the highest specified
			entityItem.setLevel(getHeighestLevel());
			
			// check to see if the root has been assigned
			if (!checkRootAssigned())	
				entityItem.setParent(genericDao.findByProperty(LocationHierarchy.class, "extId", "HIERARCHY_ROOT"));
			else
				throw new ConstraintViolations("The Location Hierarchy Root has already been set. To make a new one you must delete the old root.");
		}
		
		// if the Parent item is not the root, then we can determine the level of the Hierarchy item from the Parent
		else 
			entityItem.setLevel(determineParentLevel(item.getLevel()));
    	
		return item;
	}
	
	public LocationHierarchy checkLocationHierarchy(LocationHierarchy persistedItem, LocationHierarchy entityItem) throws ConstraintViolations {
		
		// when editing the Hierarchy, particular constraints must be enforced
		if (getHierarchyItemHighestLevel().equals(persistedItem))
			throw new ConstraintViolations("The root Location Hierarchy item cannot be modified.");	
    	if (!checkValidParentLocation(entityItem.getParent().getName())) 
    		throw new ConstraintViolations("The Parent Location name does not exist.");	
    	if (!checkValidParentLevel(entityItem.getParent().getName())) 
    		throw new ConstraintViolations("The Parent Location specified " +
    		"exceeds the deepest level in the hierarchy.");
    	if (!entityItem.getName().equals(persistedItem.getName())) {
    		 if (!checkValidChildLocation(entityItem.getName())) 
    	    		throw new ConstraintViolations("The Child Location Name already exists.");	
    	}
    	
    	// get the Parent
    	LocationHierarchy item = genericDao.findByProperty(LocationHierarchy.class, "name", entityItem.getParent().getName());
    	
    	if (item == null) 
			entityItem.setLevel(getHeighestLevel());
		else 
			entityItem.setLevel(determineParentLevel(item.getLevel()));
    	
    	if (!checkInconsistentLevels(persistedItem, entityItem)) 
    		throw new ConstraintViolations("The Hierarchy item cannot be modified since " +
    				"it's currently being used in a Location and would result in " +
    				"a level mismatch. Either remove the Location(s) using this " +
    				"Hierarchy item or specify a valid Parent level that is of the " +
    				"same level being used in the Location.");
       	
		return item;
	}
	
	/**
	 * To delete a Location Hierarchy item, there must be no dependent Locations.
	 * Deleting a Location Hierarchy is only a safe operation if no Locations are 
	 * using it. 
	 */
	public LocationHierarchy deleteLocationHierarchy(LocationHierarchy entityItem) throws ConstraintViolations {

		List<LocationHierarchy> hierarchyList = genericDao.findAll(LocationHierarchy.class, false);
		
		// item is at the lowest level so we need to check for any dependent locations
		if (entityItem.getLevel().equals(getLowestLevel())) {
			
			List<Location> locs = genericDao.findListByProperty(Location.class, "locationLevel", entityItem, true);
			if (locs.size() > 0)
				throw new ConstraintViolations("Unable to delete this Location Hierarchy item " +
						"because it is dependent on a Location. In order to delete a Location " +
						"Hierarchy item, the dependent Location must first be deleted.");
		}
		// the item is not at the lowest level so we need to check for any arbitrary location
		else {
			// determine if the entityItem is a parent of any other existing hierarchy items
			for (LocationHierarchy item : hierarchyList) {
				if (item.getParent() != null) {
					if (item.getParent().getName().equals(entityItem.getName()))
						throw new ConstraintViolations("Unable to delete this Location Hierarchy item " +
								"because there are existing items that are children of the one " +
								"being deleted. You must remove the children first.");
				}	
			}	
		}
		return entityItem;
	}
	
	public Location deleteLocation(Location entityItem) throws ConstraintViolations {
		
		if (entityItem.getResidencies().size() > 0)
			throw new ConstraintViolations("Unable to delete this Location " +
					"because there are Residencies that are dependent on this entity.");
		
		return entityItem;
	}
		
	public Location evaluateLocation(Location entityItem) throws ConstraintViolations {
		if (!checkValidLocationEntry(entityItem.getLocationLevel().getExtId())) 
    		throw new ConstraintViolations("The " + getLowestLevel().getName() + 
    		" specified is not the lowest level in the Location Hierarchy.");
		
		LocationHierarchy item = genericDao.findByProperty(LocationHierarchy.class, "extId", entityItem.getLocationLevel().getExtId());
		entityItem.setLocationLevel(item);
		
		
		return entityItem;
	}	
	
	public Location generateId(Location entityItem) throws ConstraintViolations {
		entityItem.setExtId(locationGenerator.generateId(entityItem));
		return entityItem;
	}
	
	public LocationHierarchy generateId(LocationHierarchy entityItem) throws ConstraintViolations {
		entityItem.setExtId(locationHierarchyGenerator.generateId(entityItem));
		return entityItem;
	}
	
	/**
	 * Checks if a Child Location name already exists
	 */
	public boolean checkValidChildLocation(String name) {
		
		LocationHierarchy item = genericDao.findByProperty(LocationHierarchy.class, "name", name);
		
		if (item == null)
			return true;
		
		return false;
	}
	
	/**
	 * Checks if a Parent Location Name is valid.
	 * A Parent Location Name is valid if the Hierarchy hasn't been specified,
	 * which means it would be treated as the root. If the Hierarchy has been
	 * specified and Parent Location Name exists, then it is valid.
	 */
	public boolean checkValidParentLocation(String parent) {
		
		LocationHierarchy item = null;
		
		// Must check that the location exists
		item = genericDao.findByProperty(LocationHierarchy.class, "name", parent);
				
		if (genericDao.getTotalCount(LocationHierarchy.class) == 0) {
			if (item == null && parent == "")
				return true;
		}
		else
			if (item != null)
				return true;

		return false;
	}
	
	/**
	 * Checks that the lowest Hierarchy level has not been exceeded.
	 */
	public boolean checkValidParentLevel(String parent) {
		
		LocationHierarchy item = genericDao.findByProperty(LocationHierarchy.class, "name", parent);
		
		if (item != null && item.getLevel().equals(getLowestLevel())) 
			return false;
		
		return true;
	}
	
	/**
	 * Checks if a Location name is valid from the Location form.
	 */
	public boolean checkValidLocationEntry(String locExtId) {
			
		List<LocationHierarchy> list = genericDao.findListByProperty(LocationHierarchy.class, "level", getLowestLevel(), false);
		
		Iterator<LocationHierarchy> itr = list.iterator();
		
		while(itr.hasNext()) {
			LocationHierarchy item = itr.next();
			if (item.getExtId().equals(locExtId))
				return true;	
		}	
		return false;
	}
	
	/**
	 * Checks if a duplicate Location name has been entered
	 */
	public boolean checkDuplicateLocationEntry(String locName, String locLvlName) {
		
		List<Location> list = genericDao.findListByProperty(Location.class, "locationLevel", locLvlName, true);
		
		Iterator<Location> itr = list.iterator();
		
		while(itr.hasNext()) {
			Location loc = itr.next();
			if (loc.getLocationName().equals(locName))
				return false;	
		}		
		return true;
	}
		
	/**
	 * Checks if a duplicate Location Hierarchy extId already exists
	 */
	public boolean checkDuplicateLocationHierarchyExtId(String extId) {
		
		List<LocationHierarchy> list = genericDao.findListByProperty(LocationHierarchy.class, "extId", extId, false);		
		if (list.size() > 0)
			return false;		
		return true;
	}
	
	/**
	 * Checks to see if a Hierarchy item has been assigned the root as the parent
	 */
	public boolean checkRootAssigned() {
		
		List<LocationHierarchy> list = genericDao.findAll(LocationHierarchy.class, false);
		for (LocationHierarchy item : list) {
			if (item.getParent() != null) {
				if (item.getParent().getExtId().equals("HIERARCHY_ROOT"))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a level violation will occur when attempting to edit a Location Hierarchy item.
	 * It's possible that the hierarchy item is being used as a Location and the level to become 
	 * violated. This will result in a Location referring to an invalid Location Hierarchy item.
	 * If this happens, the Location Hierarchy item cannot be modified. 
	 */
	public boolean checkInconsistentLevels(LocationHierarchy persistedItem, LocationHierarchy entityItem) {
		
		List<Location> locations = genericDao.findListByProperty(Location.class, "locationLevel", persistedItem, true);
		
		for (Location loc : locations) {
			if (!loc.getLocationLevel().getLevel().equals(entityItem.getLevel()))
				return false;
		}
			
		return true;
	}
		
	/**
	 * Returns the root of the Hierarchy. This is not returning the Hierarchy item
	 * at the highest level
	 */
	public LocationHierarchy getHierarchyRoot() {
		return genericDao.findByProperty(LocationHierarchy.class, "extId", "HIERARCHY_ROOT");
	}
	
	/**
	 * Determines the parent level of the specified level name
	 */
    public LocationHierarchyLevel determineParentLevel(LocationHierarchyLevel level) {
    	return  genericDao.findByProperty(LocationHierarchyLevel.class, "keyIdentifier", level.getKeyIdentifier()+1);
    }
    
    /**
     * Retrieves all Location extId's which contain the term provided.
     * Used in performing autocomplete.
     */
    public List<String> getLocationExtIds(String term) {
        List<String> ids = new ArrayList<String>();
        List<Location> locs = genericDao.findListByPropertyPrefix(Location.class, "extId", term, 10, true);
        for (Location loc : locs) {
            ids.add(loc.getExtId());
        }
        return ids;
    }
    
    /**
     * Retrieves all Location ids within the hierarchy that contain the term provided.
     * Used in performing autocomplete.
     */
    public List<String> getLocationNames(String term) {
        List<String> names = new ArrayList<String>();
        List<LocationHierarchy> list = genericDao.findListByPropertyPrefix(LocationHierarchy.class, "name", term, 10,
                false);
        Iterator<LocationHierarchy> itr = list.iterator();
        while (itr.hasNext()) {
            LocationHierarchy item = itr.next();
            if (!item.getExtId().equals("HIERARCHY_ROOT")) {
                if (item.getLevel().equals(getLowestLevel())
                        && item.getName().toLowerCase().contains(term.toLowerCase())
                        && !names.contains(item.getExtId()))
                    names.add(item.getExtId());
            }
        }
        return names;
    }
    
    public Location findLocationById(String locationId) {
        return genericDao.findByProperty(Location.class, "extId", locationId);
    }
    
    /**
     * Find the locationHierarchy item by id, as long as it's a leaf
     */
    public LocationHierarchy findLocationHierarchyById(String locationHierarchyId, String msg) throws Exception {
    	List<LocationHierarchy> hierarchyList = genericDao.findAll(LocationHierarchy.class, false);
    	
    	for (LocationHierarchy item : hierarchyList) {
    		if (locationHierarchyId.equals(item.getExtId()) && 
    			item.getLevel().equals(getLowestLevel()))
    			return item;
    	}
    	return null;
    }
    
    public LocationHierarchy getHierarchyItemHighestLevel() {
    	LocationHierarchyLevel highestLevel = getHeighestLevel();
    	return genericDao.findByProperty(LocationHierarchy.class, "level", highestLevel);
    }
    
    public List<LocationHierarchyLevel> getAllLevels() {
    	return genericDao.findAll(LocationHierarchyLevel.class, false);
    }
                   
    public LocationHierarchyLevel getLowestLevel() {	  	
    	List<LocationHierarchyLevel> locHLevels = genericDao.findAll(LocationHierarchyLevel.class, false);
    	int highestKey = Integer.MIN_VALUE;
    	
    	for (LocationHierarchyLevel item : locHLevels) {
    		if (item.getKeyIdentifier() > highestKey)
    			highestKey = item.getKeyIdentifier();
    	}
    	return genericDao.findByProperty(LocationHierarchyLevel.class, "keyIdentifier", highestKey);
    }
    
    public LocationHierarchyLevel getHeighestLevel() {	
       	List<LocationHierarchyLevel> locHLevels = genericDao.findAll(LocationHierarchyLevel.class, false);
    	int lowestKey = Integer.MAX_VALUE;
    	
    	for (LocationHierarchyLevel item : locHLevels) {
    		if (item.getKeyIdentifier() < lowestKey)
    			lowestKey = item.getKeyIdentifier();
    	}
    	return genericDao.findByProperty(LocationHierarchyLevel.class, "keyIdentifier", lowestKey);
    }
    
    public List<String> getValidLocationsInHierarchy(String locationHierarchyItem) {
    	
    	List<String> locations = new ArrayList<String>();
    	locations.add(locationHierarchyItem);
    	List<LocationHierarchy> hierarchyList = genericDao.findListByProperty(LocationHierarchy.class, "extId", locationHierarchyItem);
    	for (LocationHierarchy item : hierarchyList) {
    		locations = processLocationHierarchy(item, locations);
    	}
    	return locations;
    }
    
    private List<String> processLocationHierarchy(LocationHierarchy item, List<String> locationNames) {
    	
    	// base case
        if (item.getLevel().getName().equals(getLowestLevel().getName())) {
        	locationNames.add(item.getExtId());
        	return locationNames;
        }
        
        // find all location hierarchy items that are children, continue to recurse
        List<LocationHierarchy> hierarchyList = genericDao.findListByProperty(LocationHierarchy.class, "parent", item);
       
        for (LocationHierarchy locationHierarchy : hierarchyList)
            processLocationHierarchy(locationHierarchy, locationNames);

        return locationNames;
    }
        
    public LocationHierarchyLevel getLevel(int level) {
    	return genericDao.findByProperty(LocationHierarchyLevel.class, "keyIdentifier", level);
    }

	public Generator getLocationHierarchyGenerator() {
		return locationHierarchyGenerator;
	}

	public void setLocationHierarchyGenerator(Generator locationHierarchyGenerator) {
		this.locationHierarchyGenerator = locationHierarchyGenerator;
	}

    @Override
    public List<Location> getAllLocations() {
        return genericDao.findAll(Location.class, true);
    }

    @Override
    public void createLocation(Location location) throws ConstraintViolations {
        assignId(location);
        evaluateLocation(location);
        try {
            entityService.create(location);
        } catch (IllegalArgumentException e) {
        } catch (SQLException e) {
            throw new ConstraintViolations("There was a problem saving the location to the database");
        }
    }

	private void assignId(Location location) throws ConstraintViolations {
	    String id = location.getExtId() == null ? "" : location.getExtId();
	    if (id.trim().isEmpty() && locationGenerator.generated) {
	        generateId(location);
	    }
    }

    @Override
	public List<LocationHierarchy> getAllLocationHierarchies() {
		return genericDao.findAllWithoutProperty(LocationHierarchy.class, "extId", "HIERARCHY_ROOT");
	}

    @Override
    @Authorized("VIEW_ENTITY")
    public List<Location> getAllLocationsInRange(int i, int pageSize) {
        return genericDao.findPaged(Location.class, "extId", i, pageSize);
    }

    @Override
    @Authorized("VIEW_ENTITY")
    public long getTotalLocationCount() {
        return genericDao.getTotalCount(Location.class);
    }

}