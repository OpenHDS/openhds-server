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
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.OutMigrationCrudImpl;
import org.openhds.web.service.JsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class OutMigrationTest {
	
	 @Autowired
	 @Qualifier("outMigrationCrud")
	 OutMigrationCrudImpl outmigrationCrud;
	 	 	 
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
	 Visit visit;
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 individual = genericDao.findByProperty(Individual.class, "extId", "BHAR1K", false);
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
	 }
	 
	 @Test
	 @DirtiesContext
	 public void testOutMigrationCreate() {
		 assertNotNull(fieldWorker);
		 assertNotNull(individual);
		 assertNotNull(visit);
		 
		 OutMigration outMigration = new OutMigration();
		 outMigration.setIndividual(individual);
		 outMigration.setCollectedBy(fieldWorker);
		 outMigration.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 outMigration.setVisit(visit);
		 outMigration.setReason("reason");
		 
		 outmigrationCrud.setItem(outMigration);
		 outmigrationCrud.create();
		 
		 OutMigration savedOutMig = genericDao.findByProperty(OutMigration.class, "individual", individual, false);
		 assertNotNull(savedOutMig); 
	 }
	 
	 @Test
	 @DirtiesContext
	 public void testOutMigrationCreateDuplicate() {
		 assertNotNull(fieldWorker);
		 assertNotNull(individual);
		 assertNotNull(visit);
		 
		 OutMigration outMigration = new OutMigration();
		 outMigration.setIndividual(individual);
		 outMigration.setCollectedBy(fieldWorker);
		 outMigration.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 1, 2015));
		 outMigration.setVisit(visit);
		 outMigration.setReason("reason");
		 
		 outmigrationCrud.setItem(outMigration);
		 outmigrationCrud.create();
		 
		 OutMigration duplicateOutMigration = new OutMigration();
		 duplicateOutMigration.setIndividual(individual);
		 duplicateOutMigration.setCollectedBy(fieldWorker);
		 duplicateOutMigration.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 2015));
		 duplicateOutMigration.setVisit(visit);
		 duplicateOutMigration.setReason("reason");
		 
		 outmigrationCrud.setItem(duplicateOutMigration);
		 String saveResult = outmigrationCrud.create();

		 assertNull(saveResult); 
		 assertTrue(jsfServiceMock.getErrors().size() == 1);
	 }
}

