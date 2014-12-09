package org.openhds.controller.service.impl;

import java.util.List;

import org.openhds.controller.service.LocationHierarchyLevelService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.LocationHierarchyLevel;

public class LocationHierarchyLevelServiceImpl implements LocationHierarchyLevelService {

	private GenericDao genericDao;
	
	public LocationHierarchyLevelServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}


    @Override
    public List<LocationHierarchyLevel> getAllLevels() {
    	return genericDao.findAll(LocationHierarchyLevel.class, false);
    }

	
 
    
  
    
  
   
        
 






 


}