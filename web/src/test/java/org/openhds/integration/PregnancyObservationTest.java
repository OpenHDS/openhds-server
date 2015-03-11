package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.PregnancyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.IndividualCrudImpl;
import org.openhds.web.crud.impl.PregnancyObservationCrudImpl;
import org.openhds.web.crud.impl.PregnancyOutcomeCrudImpl;
import org.openhds.web.crud.impl.ResidencyCrudImpl;
import org.openhds.web.service.JsfService;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class PregnancyObservationTest extends AbstractTransactionalJUnit4SpringContextTests  {
		 
	 @Autowired
	 @Qualifier("pregnancyObservationCrud")
	 PregnancyObservationCrudImpl pregnancyObservationCrud;
	 
	 @Autowired
	 @Qualifier("pregnancyOutcomeCrud")
	 PregnancyOutcomeCrudImpl pregnancyOutcomeCrud;
	 
	 @Autowired
	 @Qualifier("residencyCrud")
	 ResidencyCrudImpl residencyCrud;	 
	 
	 @Autowired
	 @Qualifier("individualCrud")
	 IndividualCrudImpl individualCrud;	 
	 
	 @Autowired
	 @Qualifier("pregnancyService")
	 PregnancyService pregnancyService;
	 
	 @Autowired
	 SitePropertiesService siteProperties;	 
	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 FieldBuilder fieldBuilder;
	 
	 @Autowired
	 JsfService jsfService;
	 	 	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 Individual mother;
	 FieldWorker fieldWorker;
	 Visit visit;
	 JsfServiceMock jsfServiceMock;
	 
     @Before
     public void setUp() {
        
    	 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 mother = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
     }
     
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 
	 @Test
	 public void testPregnancyObservationCreate() {
		 assertNotNull(mother);
		 assertNotNull(fieldWorker);
		 assertNotNull(visit);
		 
		 PregnancyObservation pregObservation = new PregnancyObservation();
		 pregObservation.setCollectedBy(fieldWorker);
		 pregObservation.setExpectedDeliveryDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 pregObservation.setMother(mother);
		 pregObservation.setVisit(visit);
		 pregObservation.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 
		 pregnancyObservationCrud.setItem(pregObservation);
		 pregnancyObservationCrud.create();
		 
		 PregnancyObservation savedPregnancyObservation = genericDao.findByProperty(PregnancyObservation.class, "mother", mother, false);
		 assertNotNull(savedPregnancyObservation); 	 
	 }
	 	 
	 @Test
	 public void testPregnancyOutcomeInvalidAge() {
		 assertNotNull(mother);
		 assertNotNull(fieldWorker);
		 assertNotNull(visit);
		 
		 //Mother birthdate is 1959-12-19, so we try to provoke an error by setting the delivery date too near the DoB
		 PregnancyObservation pregObservation = new PregnancyObservation();
		 pregObservation.setCollectedBy(fieldWorker);
		 pregObservation.setExpectedDeliveryDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1960));
		 pregObservation.setMother(mother);
		 pregObservation.setVisit(visit);
		 pregObservation.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 
		 pregnancyObservationCrud.setItem(pregObservation);
		 //This should result in an ConstraintViolation in PregnancyObservationCrudImpl::create()
		 //org.openhds.controller.exception.ConstraintViolations: The Mother specified is younger than the minimum age required to have a Pregnancy Observation.
		 pregnancyObservationCrud.create();
		 
		 PregnancyObservation savedPregnancyObservation = genericDao.findByProperty(PregnancyObservation.class, "mother", mother, false);
		 assertNull(savedPregnancyObservation); 
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
}
