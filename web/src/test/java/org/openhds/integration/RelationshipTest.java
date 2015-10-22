package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.junit.After;
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
	 Individual indivDead;

	 @Before
	 public void setUp() {
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
	     indivA = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
	     indivB = genericDao.findByProperty(Individual.class, "extId", "BJOH1J", false);
	     indivDead = genericDao.findByProperty(Individual.class, "extId", "CBLA1H", false);
	     
	     assertNotNull(fieldWorker);
	     assertNotNull(indivA);
	     assertNotNull(indivB);
	     assertNotNull(indivDead);
	 }
	 
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 	 
	 @Test
	 public void testRelationshipCreate() {
		 Relationship relationship = new Relationship();
		 relationship.setaIsToB("1");
		 relationship.setCollectedBy(fieldWorker);
		 relationship.setEndType(siteProperties.getNotApplicableCode());
		 relationship.setIndividualA(indivA);
		 relationship.setIndividualB(indivB);
		 relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
	   
		 relationshipCrud.setItem(relationship);
		 relationshipCrud.create();
		 
		 Relationship savedRelationship = genericDao.findByProperty(Relationship.class, "individualA", indivA, false);
		 assertNotNull(savedRelationship);
	 }
	 
	 @Test
	 public void testDuplicateRelationship() {
		 Relationship relationship1 = new Relationship();
		 relationship1.setaIsToB("2");
		 relationship1.setCollectedBy(fieldWorker);
		 relationship1.setEndType(siteProperties.getNotApplicableCode());
		 relationship1.setIndividualA(indivA);
		 relationship1.setIndividualB(indivB);
		 relationship1.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
	   
		 relationshipCrud.setItem(relationship1);
		 relationshipCrud.create();
		 
		 Relationship relationship2 = new Relationship();
		 relationship2.setaIsToB("2");
		 relationship2.setCollectedBy(fieldWorker);
		 relationship2.setEndType(siteProperties.getNotApplicableCode());
		 relationship2.setIndividualA(indivA);
		 relationship2.setIndividualB(indivB);
		 relationship2.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
	   
		 relationshipCrud.setItem(relationship2);
		 //This should not go through due to ConstraintViolation:
		 //org.openhds.controller.exception.ConstraintViolations: An Individual cannot have multiple relationships with the same person.
		 relationshipCrud.create();
		 
		 Relationship savedRelationship = genericDao.findByProperty(Relationship.class, "individualA", indivA, false);
		 assertNotNull(savedRelationship);		 
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
		 
		 for(String s: jsfServiceMock.getErrors())
			 System.out.println("CV: " + s);
	 }
	 
	 @Test
	 public void testDeathInRelationship() {
		 Relationship relationship = new Relationship();
		 relationship.setaIsToB("2");
		 relationship.setCollectedBy(fieldWorker);
		 relationship.setEndType(siteProperties.getNotApplicableCode());
		 relationship.setIndividualA(indivDead);
		 relationship.setIndividualB(indivB);
		 relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
	   
		 relationshipCrud.setItem(relationship);
		 //This should trigger a ConstraintViolation:
		 //A Relationship cannot be created for an Individual who has a Death event.
		 relationshipCrud.create();
		 
		 Relationship savedRelationship = genericDao.findByProperty(Relationship.class, "individualA", indivDead, false);
		 assertNull(savedRelationship);
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 
}
