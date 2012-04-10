package org.openhds.integration;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.LocationCrudImpl;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.openhds.web.service.JsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class LocationTest extends AbstractTransactionalJUnit4SpringContextTests {
		 	 
	 @Autowired
	 @Qualifier("locationCrud")
	 LocationCrudImpl locationCrud;
	 
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
	 SitePropertiesService siteProperties;
	 
	 @Autowired
	 JsfService jsfService;
	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 LocationHierarchy locH2;
	 LocationHierarchy item;
	 FieldWorker fieldWorker;
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 createLocationHierarchy();
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
	 }
	 
	 @Test
	 public void testLocationCreate() {		
		 
		 Location location = new Location();
		 location.setLocationName("locationName");
		 location.setLocationType("RUR");
		 location.setLocationLevel(item);
		 location.setCollectedBy(fieldWorker);
		 locationCrud.setItem(location);
	     locationCrud.create();
	     
	     Location savedLocation = genericDao.findByProperty(Location.class, "extId", location.getExtId());
	     assertNotNull(savedLocation);
	 }
	 
	 @Test
	 public void testInvalidLocationCreate() {	
		 
		 Location location = new Location();
		 location.setLocationName("locationName");
		 location.setLocationType("RUR");
		 location.setLocationLevel(locH2);
		 location.setCollectedBy(fieldWorker);
		 locationCrud.setItem(location);
	     
		 assertNull(locationCrud.create());
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 private void createLocationHierarchy() {
		 
    	 LocationHierarchy locH1 = new LocationHierarchy();
    	
    	 locH1.setParent(new LocationHierarchy());
    	 locH1.setName(locationHierarchyService.getLevel(1).getName());
    	 locH1.setExtId("MOR");
    	
    	 locationHierarchyCrud.setItem(locH1);
    	 locationHierarchyCrud.create();
	   
	     locH2 = new LocationHierarchy();
	     locH2.setParent(locH1);
	     locH2.setName(locationHierarchyService.getLevel(2).getName());
	     locH2.setExtId("IFA");
   	    
 	     locationHierarchyCrud.setItem(locH2);
	     locationHierarchyCrud.create();
	  
	     item = new LocationHierarchy();
	     item.setParent(locH2);
	     item.setName(locationHierarchyService.getLevel(3).getName());
	     item.setExtId("MBI");
   	    
	     locationHierarchyCrud.setItem(item);
 	     locationHierarchyCrud.create();
	 }
}
