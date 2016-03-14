package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.SessionFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.impl.HeadOfHouseholdServiceImpl;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.HeadOfHousehold;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class HeadOfHouseholdTest extends AbstractTransactionalJUnit4SpringContextTests {
	 
	 @Autowired
	 HeadOfHouseholdServiceImpl headOfHouseholdService;
	 	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 SitePropertiesService siteProperties;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 Individual oldHoh;
	 Individual newHoh;
	 Individual invalidHoh;
	 Visit visit;
	 FieldWorker fieldWorker;
	 SocialGroup socialGroup;
	 
    private LocalValidatorFactoryBean localValidatorFactory;

	 
	 @Before
	 public void setUp(){
		 
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 assertNotNull(headOfHouseholdService);
		 
	     localValidatorFactory = new LocalValidatorFactoryBean();
	     localValidatorFactory.setProviderClass(HibernateValidator.class);
	     localValidatorFactory.afterPropertiesSet();
	     
	     oldHoh = genericDao.findByProperty(Individual.class, "extId", "NBAS1I"); 
	     newHoh = genericDao.findByProperty(Individual.class, "extId", "BJOH1J");
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", "MBI1", false);
	     
		 assertNotNull(oldHoh);
		 assertNotNull(newHoh);
		 assertNotNull(visit);
		 assertNotNull(fieldWorker);
		 assertNotNull(socialGroup);
	 }
	 
	 @After
	 public void tearDown(){
		 
	 }
	 
	 @Test
	 public void testHeadOfHouseholdCreate() {
		 
		 Death death = new Death();
		 death.setAgeAtDeath(1l);
		 death.setDeathCause("UNK");
		 death.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 death.setDeathPlace("Hospital");
		 death.setDeleted(false);
		 death.setCollectedBy(fieldWorker);
		 death.setIndividual(oldHoh);
		 death.setVisitDeath(visit);
		 
		 HeadOfHousehold headOfHousehold = new HeadOfHousehold();
		 headOfHousehold.setOldHoh(oldHoh);
		 headOfHousehold.setNewHoh(newHoh);
		 headOfHousehold.setDeathPlace("Home");
		 headOfHousehold.setDeathCause("UNK");
		 headOfHousehold.setVisit(visit);
		 headOfHousehold.setCollectedBy(fieldWorker);
		 headOfHousehold.setDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 headOfHousehold.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 headOfHousehold.setSocialGroup(socialGroup);
		 headOfHousehold.setDeath(death);
		 		 
		 /*First test for constraint violations */
		 Set<ConstraintViolation<HeadOfHousehold>> constraintViolations = localValidatorFactory.validate(headOfHousehold);	 
	     assertTrue("ConstraintViolations detected", constraintViolations.size() == 0);
	     /*End test for constraint violations */
	     
	     /* Check that the current Hoh is really the individual we've created a death event for */
		 assertEquals("NBAS1I", socialGroup.getGroupHead().getExtId());
		 
		 /* Check that there isn't already a death event for the current Hoh */
		 Death savedDeath = genericDao.findByProperty(Death.class, "individual",  oldHoh);
		 assertNull(savedDeath);
		 
		 try {

			 /* Now create the HeadOfHousehold death event*/
			 HeadOfHousehold hoh = headOfHouseholdService.createHeadOfHousehold(headOfHousehold);	 
			 assertNotNull(hoh);
			 
			 /* Check that the Hoh for the SG has changed*/
			 SocialGroup sg = genericDao.findByProperty(SocialGroup.class, "extId", socialGroup.getExtId());
			 assertEquals("BJOH1J", sg.getGroupHead().getExtId());

			 /* Check that there is a death event for the oldHoh*/
			 savedDeath = genericDao.findByProperty(Death.class, "individual",  oldHoh);	 
			 assertNotNull(savedDeath);
			 
		} catch (ConstraintViolations e) {			
			for(String s : e.getViolations())
				System.out.println("Violation: " + s);
			
			assertTrue(e.getViolations().size() == 0);
		}
	 }
	 
	 @Test
	 public void testInvalidHeadOfHouseholdCreate() {
		 
		 invalidHoh = newHoh;
		 
		 Death death = new Death();
		 death.setAgeAtDeath(1l);
		 death.setDeathCause("UNK");
		 death.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 death.setDeathPlace("Hospital");
		 death.setDeleted(false);
		 death.setCollectedBy(fieldWorker);
		 death.setIndividual(invalidHoh);
		 death.setVisitDeath(visit);
		 
		 HeadOfHousehold headOfHousehold = new HeadOfHousehold();
		 headOfHousehold.setDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 headOfHousehold.setOldHoh(invalidHoh);
		 headOfHousehold.setNewHoh(newHoh);
		 headOfHousehold.setDeathPlace("Home");
		 headOfHousehold.setDeathCause("UNK");
		 headOfHousehold.setVisit(visit);
		 headOfHousehold.setCollectedBy(fieldWorker);
		 headOfHousehold.setDeathDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 headOfHousehold.setSocialGroup(socialGroup);
		 headOfHousehold.setDeath(death);
		 		 
		 Death savedDeath = null;
		 /*First test for constraint violations */
		 Set<ConstraintViolation<HeadOfHousehold>> constraintViolations = localValidatorFactory.validate(headOfHousehold);	 
		 
	     assertTrue("ConstraintViolations detected", constraintViolations.size() == 0);
	     /*End test for constraint violations */
	     
	     /* Check that the current Hoh is not the individual we're trying to create a death event for */
		 assertNotSame("BJOH1J", socialGroup.getGroupHead().getExtId());
		 
		 /* Check that there isn't already a death event for the current Hoh */
		 savedDeath = genericDao.findByProperty(Death.class, "individual",  invalidHoh);
		 assertNull(savedDeath);
		 
		 HeadOfHousehold hoh = null;
		 try {

			 /* Now try to create the HeadOfHousehold death event - it should throw a validationexception*/
			 hoh = headOfHouseholdService.createHeadOfHousehold(headOfHousehold);	 	 
			 
		} catch (ConstraintViolations e) {			
			for(String s : e.getViolations())
				System.out.println("Violation: " + s);
			
			assertNull(hoh);
			assertTrue(e.getViolations().size() > 0);
			
			 /* Check that the Hoh for the SG hasn't changed*/
			 SocialGroup sg = genericDao.findByProperty(SocialGroup.class, "extId", socialGroup.getExtId());
			 assertEquals("NBAS1I", sg.getGroupHead().getExtId());

			 /* Check that there is no death event for the oldHoh*/
			 savedDeath = genericDao.findByProperty(Death.class, "individual",  oldHoh);	 
			 assertNull(savedDeath);
		}
	 }	 
}
