package org.openhds.integration;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Round;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.RoundCrudImpl;
import org.openhds.web.service.JsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class RoundTest extends AbstractTransactionalJUnit4SpringContextTests {

	 @Autowired
	 RoundCrudImpl roundCrud;
	 
	 @Autowired
	 GenericDao genericDao;
	 
	 @Autowired
	 @Mock
	 JsfService jsfService;
	 
	 @Autowired
	 CalendarUtil calendarUtil;
	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;	 
	 
	 @Autowired
	 SessionFactory sessionFactory;	 
	 
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
		 if(jsfServiceMock.getErrors().size() > 0)
			 jsfServiceMock.resetErrors();
	 }
	 
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 
	 @DirtiesContext
	 @Test
	 public void testCreateRound() { 
		 Round round = new Round();
		 round.setRoundNumber(new Integer(0));
		 round.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
		 round.setEndDate(calendarUtil.getCalendar(Calendar.APRIL, 4, 1995));
		 round.setRemarks("Round test");
		 
		 roundCrud.setItem(round);
		 roundCrud.create();
		 
		 Round r = genericDao.findByProperty(Round.class, "uuid" , round.getUuid());
		 
		 for(String s : jsfServiceMock.getErrors())
			 System.out.println("ABRAKADABRA (testCreateRound): " + s);
		 		 
		 assertNotNull(r);
		 assertTrue(jsfServiceMock.getErrors().size() == 0);
	 }
	 
	 @DirtiesContext
	 @Test	 
	 public void testCreateInvalidRound() {
		 Round round = new Round();
		 round.setRoundNumber(new Integer(0));
		 round.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
		 round.setEndDate(calendarUtil.getCalendar(Calendar.JANUARY, 1, 1995));
		 round.setRemarks("Round test");
		 
		 roundCrud.setItem(round);
		 roundCrud.create();
		 
		 Round r = genericDao.findByProperty(Round.class, "uuid" , round.getUuid());
		 
		 /* Expected result:
		  * The end date cannot be before the start date
		  */
		 		 
		 assertNull(r);
		 assertTrue(jsfServiceMock.getErrors().size() == 1);
		 assertEquals(jsfServiceMock.getErrors().get(0), "The end date cannot be before the start date");
	 }
	 
	 @DirtiesContext
	 @Test	 
	 public void testCreateDuplicateRound() {
		 Round round = new Round();
		 round.setRoundNumber(new Integer(1));
		 round.setStartDate(calendarUtil.getCalendar(Calendar.JANUARY, 4, 1995));
		 round.setEndDate(calendarUtil.getCalendar(Calendar.APRIL, 4, 1995));
		 round.setRemarks("Round test");
		 
		 roundCrud.setItem(round);
		 roundCrud.create();
		 
		 Round r = genericDao.findByProperty(Round.class, "uuid" , round.getUuid());
		 
		 /* Expected result:
		  * A round already exists with that round number. Please enter in a unique round number.
		  */
		 
		 assertNull(r);
		 assertTrue(jsfServiceMock.getErrors().size() == 1);
	 }
}
