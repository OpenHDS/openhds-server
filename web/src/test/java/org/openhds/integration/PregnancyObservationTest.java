package org.openhds.integration;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.List;
import org.hibernate.SessionFactory;
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
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.PregnancyObservationCrudImpl;
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
public class PregnancyObservationTest extends AbstractTransactionalJUnit4SpringContextTests {
		 
	 @Autowired
	 @Qualifier("pregnancyObservationCrud")
	 PregnancyObservationCrudImpl pregnancyObservationCrud;
	 
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
	 
	 @Test
	 public void testPregnancyObservationCreate() {
		 		 
		 PregnancyObservation pregnancyObservation = new PregnancyObservation();
		 pregnancyObservation.setMother(mother);
		 pregnancyObservation.setCollectedBy(fieldWorker);
		 pregnancyObservation.setVisit(visit);
		 pregnancyObservation.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 pregnancyObservation.setExpectedDeliveryDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1981));
		 
		 pregnancyObservationCrud.setItem(pregnancyObservation);
		 pregnancyObservationCrud.create();
		 
		 List<PregnancyObservation> list = pregnancyService.getPregnancyObservationByIndividual(mother);
	     assertTrue(list.size() > 0);
	     
		 pregnancyObservation = new PregnancyObservation();
		 pregnancyObservation.setMother(mother);
		 pregnancyObservation.setCollectedBy(fieldWorker);
		 pregnancyObservation.setVisit(visit);
		 pregnancyObservation.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 pregnancyObservation.setExpectedDeliveryDate(calendarUtil.getCalendar(Calendar.SEPTEMBER, 4, 1981));
	     
		 // cannot create duplicate pregnancy observations until a matching pregnancy outcome has been found
		 pregnancyObservationCrud.setItem(pregnancyObservation);
		 assertNull(pregnancyObservationCrud.create());
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 	 
	 @Test
	 public void testPregnancyOutcomeInvalidAge() {
		 
		 PregnancyObservation pregnancyObservation = new PregnancyObservation();
		 pregnancyObservation.setMother(mother);
		 pregnancyObservation.setCollectedBy(fieldWorker);
		 pregnancyObservation.setVisit(visit);
		 pregnancyObservation.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1960));
		 pregnancyObservation.setExpectedDeliveryDate(calendarUtil.getCalendar(Calendar.SEPTEMBER, 4, 1961));
	 
		 // the mother is too young to have a pregnancy observation
		 pregnancyObservationCrud.setItem(pregnancyObservation);
		 assertNull(pregnancyObservationCrud.create());
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
}
