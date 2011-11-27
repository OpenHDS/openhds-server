package org.openhds.dao.service.impl;

import java.util.Properties;
import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.LocationLevelsSetter;
import org.openhds.domain.model.LocationHierarchyLevel;

public class LocationLevelsSetterImpl implements LocationLevelsSetter {
	
	GenericDao genericDao;
	
	public void setLocationLevels(Properties p) {
		
    	for (Object o : p.keySet()) {		
    		Integer i = Integer.parseInt(o.toString());
    		String s = p.get(o).toString();		
    		if (!s.equals("")) {
        		LocationHierarchyLevel item = genericDao.findByProperty(LocationHierarchyLevel.class, "keyIdentifier", i);
        		if (item == null) {
        			LocationHierarchyLevel entity = new LocationHierarchyLevel();
        			entity.setKeyIdentifier(i);
        			entity.setName(s);
            		genericDao.create(entity);
        		}
        		else {
        			item.setName(s);
        			genericDao.update(item);
        		}
    		}    			
    	}
    }
	
	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
}
