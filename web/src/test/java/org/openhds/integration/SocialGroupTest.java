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
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.openhds.web.crud.impl.SocialGroupCrudImpl;
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
public class SocialGroupTest extends AbstractTransactionalJUnit4SpringContextTests {
		 	 	 	 
	 @Autowired
	 @Qualifier("locationHierarchyCrud")
	 LocationHierarchyCrudImpl locationHierarchyCrud;
	 
	 @Autowired
	 @Qualifier("socialGroupCrud")
	 SocialGroupCrudImpl socialGroupCrud;
	 	 
	 @Autowired
	 @Qualifier("locationHierarchyService")
	 LocationHierarchyService locationHierarchyService;
	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 JsfService jsfService;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 SitePropertiesService siteProperties;
	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 FieldWorker fieldWorker;
	 Individual individual;
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
    	 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 createLocationHierarchy();
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 individual = genericDao.findByProperty(Individual.class, "extId", "NBAS1I");
	 }
	 	 
	 @Test
	 public void testSocialGroupCreate() {
	 
		 SocialGroup socialGroup = new SocialGroup();
		 socialGroup.setGroupHead(individual);
		 socialGroup.setGroupName("Group");
		 socialGroup.setGroupType("FAM");
		 socialGroup.setCollectedBy(fieldWorker);
		 
		 socialGroupCrud.setItem(socialGroup);
		 socialGroupCrud.create();
		 
		 SocialGroup savedSocialGroup = genericDao.findByProperty(SocialGroup.class, "extId", socialGroup.getExtId());
		 assertNotNull(savedSocialGroup);
	 }
	 
	 @Test
	 public void testSocialGroupDeath() {
		 
		 Individual indiv = genericDao.findByProperty(Individual.class, "extId", "CBLA1H", false);
		 
		 SocialGroup socialGroup = new SocialGroup();
		 socialGroup.setGroupHead(indiv);
		 socialGroup.setGroupName("Group");
		 socialGroup.setGroupType("FAM");
		 socialGroup.setCollectedBy(fieldWorker);
		 
		 socialGroupCrud.setItem(socialGroup);
		 assertNull(socialGroupCrud.create());
		 
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
	  
	     LocationHierarchy item = new LocationHierarchy();
	     item.setParent(locH2);
	     item.setName(locationHierarchyService.getLevel(3).getName());
	     item.setExtId("MBI");
   	    
	     locationHierarchyCrud.setItem(item);
 	     locationHierarchyCrud.create();
	 }
}
