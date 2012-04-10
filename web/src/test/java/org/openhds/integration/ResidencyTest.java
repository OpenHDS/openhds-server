package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.Residency;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.LocationCrudImpl;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.openhds.web.crud.impl.ResidencyCrudImpl;
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
public class ResidencyTest extends AbstractTransactionalJUnit4SpringContextTests {

	 @Autowired
	 @Qualifier("residencyCrud")
	 ResidencyCrudImpl residencyCrud;
	 
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
	 JsfService jsfService;
	 
	 @Autowired
	 SitePropertiesService siteProperties;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 LocationHierarchy item;
	 JsfServiceMock jsfServiceMock;
	 FieldWorker fieldWorker;
	 Individual individual;
	 Location location;
	 
	 @Before
	 public void setUp() {
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 individual = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
		 
		 createLocationHierarchy();
		 createLocation();
	 }
	 
	 @Test
	 public void testResidencyCreate() {
		 
		 Residency residency = new Residency();
		 residency.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
		 residency.setIndividual(individual);
		 residency.setLocation(location);
		 residency.setStartType(siteProperties.getBirthCode());
		 residency.setEndType(siteProperties.getNotApplicableCode());
		 residency.setCollectedBy(fieldWorker);
		 
		 residencyCrud.setItem(residency);
		 residencyCrud.create();
		 
		 Residency savedResidency = genericDao.findByProperty(Residency.class, "individual", individual);
		 assertNotNull(savedResidency);
	 }
	 
	 @Test
	 public void testOpenResidency() {
		 
		 Residency residency = new Residency();
		 residency.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
		 residency.setIndividual(individual);
		 residency.setLocation(location);
		 residency.setStartType(siteProperties.getBirthCode());
		 residency.setEndType(siteProperties.getNotApplicableCode());
		 residency.setCollectedBy(fieldWorker);
		 
		 residencyCrud.setItem(residency);
		 residencyCrud.create();
		 
		 Residency badResidency = new Residency();
		 badResidency.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1997));
		 badResidency.setIndividual(individual);
		 badResidency.setLocation(location);
		 residency.setStartType(siteProperties.getBirthCode());
		 badResidency.setEndType(siteProperties.getNotApplicableCode());
		 badResidency.setCollectedBy(fieldWorker);
		 
		 residencyCrud.setItem(badResidency);
		 assertNull(residencyCrud.create());
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
		 
	 }
	 
	 private void createLocationHierarchy() {
		 
    	 LocationHierarchy locH1 = new LocationHierarchy();
    	
    	 locH1.setParent(new LocationHierarchy());
    	 locH1.setName(locationHierarchyService.getLevel(1).getName());
    	 locH1.setExtId("MOR");
    	
    	 locationHierarchyCrud.setItem(locH1);
    	 locationHierarchyCrud.create();
	   
	     LocationHierarchy locH2 = new LocationHierarchy();
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
	 
	 private void createLocation() {
		 location = new Location();
		 location.setLocationName("locationName");
		 location.setLocationType("RUR");
		 location.setLocationLevel(item);
		 location.setCollectedBy(fieldWorker);
		 locationCrud.setItem(location);
	     locationCrud.create();
	 }
}
