package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.SiteConfigService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.EntityType;
import org.openhds.domain.model.Extension;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.PrimitiveType;
import org.openhds.domain.model.Visit;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.ExtensionCrudImpl;
import org.openhds.web.crud.impl.VisitCrudImpl;
import org.openhds.web.service.JsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class EAVTest {
	
	 @Autowired
	 @Qualifier("extensionCrud")
	 ExtensionCrudImpl extensionCrud;
	 
	 @Autowired
	 @Qualifier("visitCrud")
	 VisitCrudImpl visitCrud;

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
	 
	 @Autowired
	 SiteConfigService siteConfigService;
	 
	 JsfServiceMock jsfServiceMock;
	 FieldWorker fieldWorker;
	 Location location;
	
	 @Before
	 public void setUp() {
		 
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 location = genericDao.findByProperty(Location.class, "extId", "NJA000001");
	 }
	 
	 @Test
	 public void testAttributeCreate() {
		 
		 ClassExtension extension = createAttribute();
		 		 
		 ClassExtension savedAttribute = genericDao.findByProperty(ClassExtension.class, "name", extension.getName());
		 assertNotNull(savedAttribute);
	 }
	 
	 @Test
	 public void testAttributeValue() {
		 
		 Visit visit = new Visit();
		 visit.setRoundNumber(1);
		 visit.setVisitLocation(location);
		 visit.setVisitDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1990));
		 visit.setCollectedBy(fieldWorker);
		 
		 List<Extension> extensions = new ArrayList<Extension>();
		 ClassExtension classExtension = createAttribute();
		 
		 Extension extension = new Extension();
		 extension.setClassExtension(classExtension);
		 extension.setEntity(visit);
		 extension.setExtensionValue("Taps");
		 extension.setEntityExtId("NJA001");
		 extensions.add(extension);
		 
		 visit.setExtensions(extensions);
		 
		 visitCrud.setItem(visit);
		 visitCrud.create();
		 
		 Visit savedVisit = genericDao.findByProperty(Visit.class, "extId", visit.getExtId());
		 
		 //If visitLevel is set to location, create should be successfull
		 if(siteConfigService.getVisitAt().equalsIgnoreCase("location")){
			 assertNotNull(savedVisit);
		 }
		 //if visitlevel at sg, create should not have been successfull (extId too short)
		 else{
			 assertNull(savedVisit);
		 }
	 }
	 
	 private ClassExtension createAttribute() {
		 
		 ClassExtension extension = new ClassExtension();
		 extension.setName("waterSource");
		 extension.setDescription("Location water source");
		 extension.setAnswers("Bore Hole, Taps, Well, Other");
		 extension.setPrimType(PrimitiveType.MULTIPLECHOICE);
		 extension.setRoundNumber(1);
		 extension.setEntityClass(EntityType.LOCATION);
		 
		 extensionCrud.setItem(extension);
		 extensionCrud.create();
		 
		 return extension;
	 }
}
