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
		 	     
	     Relationship relationship = new Relationship();
	     relationship.setIndividualA(indivA);
	     relationship.setIndividualB(indivB);
	     relationship.setaIsToB("1");
	     relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
	     relationship.setEndType(siteProperties.getNotApplicableCode());
	     relationship.setCollectedBy(fieldWorker);
		
	     relationshipCrud.setItem(relationship);	     	     
	     relationshipCrud.create();

	     Relationship savedRelationship = genericDao.findByProperty(Relationship.class, "individualA", indivA, false);
	     assertNotNull(savedRelationship);
	     	     
	     relationship = new Relationship();
	     relationship.setIndividualA(indivB);
	     relationship.setIndividualB(indivA);
	     relationship.setaIsToB("1");
	     relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
	     relationship.setEndType(siteProperties.getNotApplicableCode());
	     relationship.setCollectedBy(fieldWorker);
	     
	     relationshipCrud.setItem(relationship);
	     assertNull(relationshipCrud.create());
	     
	     // errors, an individual cannot have multiple relationships with the same person
	     assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 @Test
	 public void testDuplicateRelationship() {
		 
		 Relationship relationship = new Relationship();
	     relationship.setIndividualA(indivA);
	     relationship.setIndividualB(indivA);
	     relationship.setaIsToB("1");
	     relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
	     relationship.setEndType(siteProperties.getNotApplicableCode());
	     relationship.setCollectedBy(fieldWorker);
		 
	     relationshipCrud.setItem(relationship);
	     assertNull(relationshipCrud.create());
	     
		 // errors, no two indivs can be the same, relationship must be for man and woman only
	     assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 @Test
	 public void testDeathInRelationship() {
		 
		 Individual indiv = genericDao.findByProperty(Individual.class, "extId", "CBLA1H", false);
		 
		 Relationship relationship = new Relationship();
	     relationship.setIndividualA(indiv);
	     relationship.setIndividualB(indivB);
	     relationship.setaIsToB("1");
	     relationship.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
	     relationship.setEndType(siteProperties.getNotApplicableCode());
	     relationship.setCollectedBy(fieldWorker);
		 
	     relationshipCrud.setItem(relationship);
	     assertNull(relationshipCrud.create());
	     
		 // errors, individual cannot have a death event
	     assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 
}
