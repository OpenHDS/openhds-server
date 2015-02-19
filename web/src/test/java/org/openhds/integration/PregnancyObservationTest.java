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
import org.openhds.domain.model.OutMigration;
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
		 
		 
	 }
}
