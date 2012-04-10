package org.openhds.integration;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class LocationHierarchyTest extends AbstractTransactionalJUnit4SpringContextTests {
		 	 
	 @Autowired
	 @Qualifier("locationHierarchyCrud")
	 LocationHierarchyCrudImpl locationHierarchyCrud;
	 
	 @Autowired
	 @Qualifier("locationHierarchyService")
	 LocationHierarchyService locationHierarchyService;
	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 @Test
	 public void testLocationHierarchyCreate() {
		 
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
    	 LocationHierarchy locH1 = new LocationHierarchy();
    	
    	 locH1.setParent(new LocationHierarchy());
    	 locH1.setName(locationHierarchyService.getLevel(1).getName());
    	 locH1.setExtId("MOR");
    	
    	 locationHierarchyCrud.setItem(locH1);
    	 locationHierarchyCrud.create();
    	 
    	 LocationHierarchy savedLocH1 = genericDao.findByProperty(LocationHierarchy.class, "extId", locH1.getExtId());
	     assertNotNull(savedLocH1);
    	 
	     LocationHierarchy locH2 = new LocationHierarchy();
	     locH2.setParent(locH1);
	     locH2.setName(locationHierarchyService.getLevel(2).getName());
	     locH2.setExtId("IFA");
   	    
 	     locationHierarchyCrud.setItem(locH2);
	     locationHierarchyCrud.create();
	     
    	 LocationHierarchy savedLocH2 = genericDao.findByProperty(LocationHierarchy.class, "extId", locH2.getExtId());
	     assertNotNull(savedLocH2);
	  
	     LocationHierarchy locH3 = new LocationHierarchy();
	     locH3.setParent(locH2);
	     locH3.setName(locationHierarchyService.getLevel(3).getName());
	     locH3.setExtId("MBI");
   	    
	     locationHierarchyCrud.setItem(locH3);
 	     locationHierarchyCrud.create();
 	     
    	 LocationHierarchy savedLocH3 = genericDao.findByProperty(LocationHierarchy.class, "extId", locH3.getExtId());
	     assertNotNull(savedLocH3);
	 }
}
