package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.PregnancyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.IndividualCrudImpl;
import org.openhds.web.crud.impl.LocationCrudImpl;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.openhds.web.crud.impl.PregnancyOutcomeCrudImpl;
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
public class PregnancyOutcomeTest extends AbstractTransactionalJUnit4SpringContextTests {
		 
	 @Autowired
	 @Qualifier("pregnancyOutcomeCrud")
	 PregnancyOutcomeCrudImpl pregnancyOutcomeCrud;
	 
	 @Autowired
	 @Qualifier("individualCrud")
	 IndividualCrudImpl individualCrud;
	 
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
	 @Qualifier("pregnancyService")
	 PregnancyService pregnancyService;
	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 SitePropertiesService siteProperties;
	 
	 @Autowired
	 JsfService jsfService;
	 	 	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 Individual mother;
	 Individual unknownIndiv;
	 FieldWorker fieldWorker;
	 SocialGroup socialGroup;
	 Location location;
	 LocationHierarchy item;
	 Visit visit;
	 Residency residency;
	 JsfServiceMock jsfServiceMock;
	 
     @Before
     public void setUp() {
        
    	 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 mother = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 unknownIndiv = genericDao.findByProperty(Individual.class, "extId", "UNK", false);
		 socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", "MBI1", false);
		 
		 		 
		 createLocationHierarchy();
		 createLocation();
		 
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
		 
		 createResidency();
     }
     
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 
	 @Test
	 public void testPregnancyOutcomeCreate() {
		 assertNotNull(mother);
		 assertNotNull(fieldWorker);
		 assertNotNull(visit);
		 assertNotNull(residency);
		 	 
		 Individual child = new Individual();
		 child.setDob(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 child.setExtId("NBAS3I");
		 child.setFirstName("Firstname");
		 child.setLastName("LastName");
		 child.setGender(siteProperties.getFemaleCode());
		 child.setMother(mother);
		 child.setFather(unknownIndiv);
		 child.setCollectedBy(fieldWorker);
		 
		 List<Outcome> outcomes = new ArrayList<Outcome>();
		 Outcome outcome = new Outcome();
		 outcome.setChild(child);
		 outcome.setType(siteProperties.getMiscarriageCode());
		 
		 outcomes.add(outcome);
		 
		 PregnancyOutcome pregOutcome = new PregnancyOutcome();
		 pregOutcome.setCollectedBy(fieldWorker);
		 pregOutcome.setMother(mother);
		 pregOutcome.setFather(unknownIndiv);
		 pregOutcome.setOutcomeDate(calendarUtil.getCalendar(Calendar.JANUARY, 2, 1990));
		 pregOutcome.addOutcome(outcome);
		 pregOutcome.setVisit(visit);

		 pregnancyOutcomeCrud.setItem(pregOutcome);
		 pregnancyOutcomeCrud.create();
		 
		 PregnancyOutcome savedPregnancyOutcome = genericDao.findByProperty(PregnancyOutcome.class, "mother", mother, false);
		 assertNotNull(savedPregnancyOutcome);	
	 }
	 
	 @Test
	 public void testInvalidMotherAgeForPregnancyOutcome() {
		 assertNotNull(mother);
		 assertNotNull(fieldWorker);
		 assertNotNull(visit);
		 assertNotNull(residency);
		 	 
		 Individual child = new Individual();
		 child.setDob(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 child.setExtId("NBAS3I");
		 child.setFirstName("Firstname");
		 child.setLastName("LastName");
		 child.setGender(siteProperties.getFemaleCode());
		 child.setMother(mother);
		 child.setFather(unknownIndiv);
		 child.setCollectedBy(fieldWorker);
		 
		 List<Outcome> outcomes = new ArrayList<Outcome>();
		 Outcome outcome = new Outcome();
		 outcome.setChild(child);
		 outcome.setType(siteProperties.getMiscarriageCode());
		 
		 outcomes.add(outcome);
		 
		 PregnancyOutcome pregOutcome = new PregnancyOutcome();
		 pregOutcome.setCollectedBy(fieldWorker);
		 pregOutcome.setMother(mother);
		 pregOutcome.setFather(unknownIndiv);
		 pregOutcome.setOutcomeDate(calendarUtil.getCalendar(Calendar.JANUARY, 2, 1962));
		 pregOutcome.addOutcome(outcome);
		 pregOutcome.setVisit(visit);

		 pregnancyOutcomeCrud.setItem(pregOutcome);
		 pregnancyOutcomeCrud.create();
		 
		 PregnancyOutcome savedPregnancyOutcome = genericDao.findByProperty(PregnancyOutcome.class, "mother", mother, false);
		 assertNull(savedPregnancyOutcome);			 
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 private void createResidency() {
		 residency = genericDao.findByProperty(Residency.class, "uuid", "residency_uuid2");
	 }
	 
	 private void createLocationHierarchy() {
		
	 }
	 
	 private void createLocation() {
		
	 }
}

