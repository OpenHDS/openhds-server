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
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.MembershipCrudImpl;
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
public class MembershipTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	 @Autowired
	 @Qualifier("membershipCrud")
	 MembershipCrudImpl membershipCrud;
	 	 	 
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
	 SocialGroup socialGroup;
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 fieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", "FWEK1D");
		 individual = genericDao.findByProperty(Individual.class, "extId", "NBAS1I", false);
		 socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", "MBI1", false);
	 }
	 
	 @Test
	 public void testMembershipCreate() {
		 
	     Membership membership = new Membership();
	     membership.setSocialGroup(socialGroup);
	     membership.setCollectedBy(fieldWorker);
	     membership.setStartType(siteProperties.getBirthCode());
	     membership.setEndType(siteProperties.getNotApplicableCode());
	     membership.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
	     membership.setIndividual(individual);
	     membership.setbIsToA("3");
	     
	     membershipCrud.setItem(membership);
	     membershipCrud.create();
	     
	     Individual savedIndividual = genericDao.findByProperty(Individual.class, "extId", individual.getExtId(), false);
		 assertNotNull(savedIndividual);
	 }
	 
	 @Test
	 public void testMembershipDeath() {
		 
		 Individual indiv = genericDao.findByProperty(Individual.class, "extId", "CBLA1H", false);
		 
		 Membership membership = new Membership();
	     membership.setSocialGroup(socialGroup);
	     membership.setCollectedBy(fieldWorker);
	     membership.setStartType(siteProperties.getBirthCode());
	     membership.setEndType(siteProperties.getNotApplicableCode());
	     membership.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1980));
	     membership.setIndividual(indiv);
	     membership.setbIsToA("3");
	     
	     membershipCrud.setItem(membership);
	     assertNull(membershipCrud.create());
	     
	     assertTrue(jsfServiceMock.getErrors().size() > 0);
	 }
}
