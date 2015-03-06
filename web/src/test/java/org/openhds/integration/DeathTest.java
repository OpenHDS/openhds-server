package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.DeathCrudImpl;
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
public class DeathTest extends AbstractTransactionalJUnit4SpringContextTests {
	 
	 @Autowired
	 DeathCrudImpl deathCrud;
	 	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 SitePropertiesService siteProperties;
	 
	 @Autowired
	 JsfService jsfService;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 FieldWorker fieldWorker;
	 Individual individual;
	 Individual invalidIndividual;
	 Visit visit;
	 
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 individual = genericDao.findByProperty(Individual.class, "extId", "BJOH1J", false);
		 invalidIndividual = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false); // HoH
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
		 
		 assertNotNull(fieldWorker);
		 assertNotNull(individual);
		 assertNotNull(visit);
	 }
	 
	 @Test
	 public void testDeathCreate() { 	 
		 Death death = new Death();
		 death.setAgeAtDeath(1l);
		 death.setDeathCause("UNK");
		 death.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 death.setDeathPlace("Hospital");
		 death.setDeleted(false);
		 death.setCollectedBy(fieldWorker);
		 death.setIndividual(individual);
		 death.setVisitDeath(visit);
		 
		 deathCrud.setItem(death);
		 deathCrud.create();
		 
		 Death savedDeath = genericDao.findByProperty(Death.class, "individual", individual, false);
		 
		 assertNotNull(savedDeath);
		 assertTrue(jsfServiceMock.getErrors().size() == 0);
	 }
	 
	 @Test
	 public void testInvalidDeath() { 
		 Death death = new Death();
		 death.setAgeAtDeath(1l);
		 death.setDeathCause("UNK");
		 death.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 death.setDeathPlace("Hospital");
		 death.setDeleted(false);
		 death.setCollectedBy(fieldWorker);
		 death.setIndividual(invalidIndividual); //Individual is HoH
		 death.setVisitDeath(visit);
		 
		 deathCrud.setItem(death);
		 deathCrud.create();
		 
		 Death savedDeath = genericDao.findByProperty(Death.class, "individual", invalidIndividual, false);
		 
		 assertNull(savedDeath);
		 assertTrue(jsfServiceMock.getErrors().size() == 1);
	 }
	 
	 @Test
	 public void testDuplicateDeath() { 
		 Death death = new Death();
		 death.setAgeAtDeath(1l);
		 death.setDeathCause("UNK");
		 death.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 death.setDeathPlace("Hospital");
		 death.setDeleted(false);
		 death.setCollectedBy(fieldWorker);
		 death.setIndividual(individual); //Individual is HoH
		 death.setVisitDeath(visit);
		 
		 deathCrud.setItem(death);
		 String createResult = deathCrud.create();
		 
		 System.out.println("createResult: " + createResult);
		 
		 Death savedDeath = genericDao.findByProperty(Death.class, "individual", individual, false);
		 
		 assertTrue(createResult != null);
		 assertNotNull(savedDeath);
		 assertTrue(jsfServiceMock.getErrors().size() == 0);
		 
		 Death duplicateDeath = new Death();
		 duplicateDeath.setAgeAtDeath(1l);
		 duplicateDeath.setDeathCause("UNK");
		 duplicateDeath.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 duplicateDeath.setDeathPlace("Hospital");
		 duplicateDeath.setDeleted(false);
		 duplicateDeath.setCollectedBy(fieldWorker);
		 duplicateDeath.setIndividual(individual); //Individual is HoH
		 duplicateDeath.setVisitDeath(visit);
		 
		 deathCrud.setItem(duplicateDeath);
		 createResult = deathCrud.create(); // This should throw a ConstrainViolation: A Death Event for the Individual with specified ExtId already exists!
		 
		 System.out.println("createResult: " + createResult);
		 
		 assertTrue(createResult == null);
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }	 
}
