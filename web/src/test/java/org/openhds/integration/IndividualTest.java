package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.web.crud.impl.IndividualCrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class IndividualTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	 @Autowired
	 @Qualifier("individualCrud")
	 IndividualCrudImpl individualCrud;
	 	 	 
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
	 
	 @Test
	 public void testIndividualCreate() {
		 
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 FieldWorker fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
	     Individual unknownIndiv = genericDao.findByProperty(Individual.class, "extId", "UNK", false);
		 
		 Individual mother = new Individual();
	     mother.setFirstName("First");
	     mother.setLastName("Last");
	     mother.setGender(siteProperties.getFemaleCode());
	     mother.setDobAspect("1");
	     mother.setMother(unknownIndiv);
	     mother.setFather(unknownIndiv);
	     mother.setExtId("NJA000001001");
	     mother.setDob(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
	     mother.setCollectedBy(fieldWorker);
	     individualCrud.setItem(mother);
	     individualCrud.create();
	     
	     Individual savedIndividual = genericDao.findByProperty(Individual.class, "extId", mother.getExtId(), false);
		 assertNotNull(savedIndividual);
	 }
}
