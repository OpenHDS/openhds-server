package org.openhds.integration;

import static org.junit.Assert.*;
import java.util.Calendar;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.RelationshipCrudImpl;
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
public class RelationshipTest extends AbstractTransactionalJUnit4SpringContextTests {
		 
	 @Autowired
	 @Qualifier("relationshipCrud")
	 RelationshipCrudImpl relationshipCrud;
	 	 	 
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
	 
	 JsfServiceMock jsfServiceMock;
	 FieldWorker fieldWorker;
	 Individual indivA; 
	 Individual indivB;

	 @Before
	 public void setUp() {
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
	     indivA = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
	     indivB = genericDao.findByProperty(Individual.class, "extId", "BJOH1J", false);
	 }
	 	 
	 @Test
	 public void testRelationshipCreate() {
		 	     
	   
	 }
	 
	 @Test
	 public void testDuplicateRelationship() {
	
	 }
	 
	 @Test
	 public void testDeathInRelationship() {
		 
		
	 }
	 
	 
}
