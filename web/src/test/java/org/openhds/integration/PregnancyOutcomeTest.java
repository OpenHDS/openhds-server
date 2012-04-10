package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
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
import org.openhds.domain.model.Membership;
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
	 
	 @Test
	 public void testPregnancyOutcomeCreate() {
		 		 
		 PregnancyOutcome po = new PregnancyOutcome();
		 po.setMother(mother);
		 po.setFather(unknownIndiv);
		 po.setVisit(visit);
		 po.setNumberOfLiveBirths(0);
		 po.setOutcomeDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 po.setCollectedBy(fieldWorker);
		 
		 List<Outcome> outcomes = po.getOutcomes();
		 Outcome outcome = new Outcome();
		 outcome.setType(siteProperties.getMarriageCode());
		 outcomes.add(outcome);
		 
		 po.setOutcomes(outcomes);
		 
		 pregnancyOutcomeCrud.setItem(po);
		 pregnancyOutcomeCrud.create();
		 
		 PregnancyOutcome savedPO = genericDao.findByProperty(PregnancyOutcome.class, "outcomeDate", calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
		 assertNotNull(savedPO);
	 }
	 
	 @Test
	 public void testInvalidMotherAgeForPregnancyOutcome() {
		 		 
		 PregnancyOutcome po = new PregnancyOutcome();
		 po.setMother(mother);
		 po.setFather(unknownIndiv);
		 po.setVisit(visit);
		 po.setNumberOfLiveBirths(1);
		 po.setOutcomeDate(calendarUtil.getCalendar(Calendar.SEPTEMBER, 4, 1960));
		 po.setCollectedBy(fieldWorker);
		 
		 pregnancyOutcomeCrud.setItem(po);
		 assertNull(pregnancyOutcomeCrud.create());
		 assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
	 
	 private void createResidency() {
		 
		 Residency residency = new Residency();
		 residency.setStartDate(calendarUtil.getCalendar(Calendar.DECEMBER, 19, 1959));
		 residency.setIndividual(mother);
		 residency.setLocation(location);
		 residency.setStartType(siteProperties.getBirthCode());
		 residency.setEndType(siteProperties.getNotApplicableCode());
		 residency.setCollectedBy(fieldWorker);
		 
		 Set<Residency> residencies = new HashSet<Residency>();
		 residencies.add(residency);
		 
		 mother.setAllResidencies(residencies);
		 
		 residencyCrud.setItem(residency);
		 residencyCrud.create();
		 
		 Residency savedResidency = genericDao.findByProperty(Residency.class, "individual", mother);
		 assertNotNull(savedResidency);
	 }
	 
	 private void createLocationHierarchy() {
		 
    	 LocationHierarchy locH1 = new LocationHierarchy();
    	
    	 locH1.setParent(new LocationHierarchy());
    	 locH1.setName(locationHierarchyService.getLevel(1).getName());
    	 locH1.setExtId("MOR");
    	
    	 locationHierarchyCrud.setItem(locH1);
    	 locationHierarchyCrud.create();
	   
	     LocationHierarchy locH2 = new LocationHierarchy();
	     locH2.setParent(locH1);
	     locH2.setName(locationHierarchyService.getLevel(2).getName());
	     locH2.setExtId("IFA");
   	    
 	     locationHierarchyCrud.setItem(locH2);
	     locationHierarchyCrud.create();
	  
	     item = new LocationHierarchy();
	     item.setParent(locH2);
	     item.setName(locationHierarchyService.getLevel(3).getName());
	     item.setExtId("MBI");
   	    
	     locationHierarchyCrud.setItem(item);
 	     locationHierarchyCrud.create();
	 }
	 
	 private void createLocation() {
		 location = new Location();
		 location.setLocationName("locationName");
		 location.setLocationType("RUR");
		 location.setLocationLevel(item);
		 location.setCollectedBy(fieldWorker);
		 locationCrud.setItem(location);
	     locationCrud.create();
	 }
}

