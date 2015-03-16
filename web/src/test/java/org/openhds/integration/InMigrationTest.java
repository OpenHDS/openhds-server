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
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.MigrationType;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.InMigrationCrudImpl;
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
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InMigrationTest {
	
	 @Autowired
	 @Qualifier("inMigrationCrud")
	 InMigrationCrudImpl inmigrationCrud;
	 	 	 
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
		 individual = genericDao.findByProperty(Individual.class, "extId", "BJOH1J", false);
		 visit = genericDao.findByProperty(Visit.class, "extId", "VLOCMBI11J");
	 }
	 
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 
	 @Test
	 public void testInMigrationCreate() {
		 assertNotNull(fieldWorker);
		 assertNotNull(individual);
		 assertNotNull(visit);
		 
		 InMigration inmig = new InMigration();
		 inmig.setIndividual(individual);
		 inmig.setCollectedBy(fieldWorker);
		 inmig.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 inmig.setMigTypeInternal();
		 inmig.setOrigin("place");
		 inmig.setReason("reason");
		 inmig.setUnknownIndividual(false);
		 inmig.setVisit(visit);
		 
		 inmigrationCrud.setItem(inmig);
		 inmigrationCrud.create();
	     
	     InMigration savedInMig = genericDao.findByProperty(InMigration.class, "individual", individual, false);
		 assertNotNull(savedInMig);
	 }
	 
	 @Test
	 public void testInMigrationExternalInmigrateExistingIndividual() {
		 assertNotNull(fieldWorker);
		 assertNotNull(individual);
		 assertNotNull(visit);
		 
		 InMigration inmig = new InMigration();
		 inmig.setIndividual(individual);
		 inmig.setCollectedBy(fieldWorker);
		 inmig.setRecordedDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 inmig.setMigTypeInternal();
		 inmig.setOrigin("place");
		 inmig.setReason("reason");
		 inmig.setUnknownIndividual(false);
		 inmig.setVisit(visit);
		 inmig.setMigType(MigrationType.EXTERNAL_INMIGRATION);
		 
		 inmigrationCrud.setItem(inmig);
		 inmigrationCrud.create();
	     
	     InMigration savedInMig = genericDao.findByProperty(InMigration.class, "individual", individual, false);
	     
	     assertTrue(jsfServiceMock.getErrors().size() == 1);	     
		 assertNull(savedInMig);
	 }
}

