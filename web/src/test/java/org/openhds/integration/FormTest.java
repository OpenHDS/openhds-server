package org.openhds.integration;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Form;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.integration.util.JsfServiceMock;
import org.openhds.web.crud.impl.FormCrudImpl;
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
public class FormTest extends AbstractTransactionalJUnit4SpringContextTests {
	 
	 @Autowired
	 FormCrudImpl formCrud;
	 	 	 
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
	 	 
	 JsfServiceMock jsfServiceMock;
	 
	 @Before
	 public void setUp() {
		 
		 jsfServiceMock = (JsfServiceMock)jsfService;
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 jsfServiceMock.resetErrors();
	 }
	 
	 @After
	 public void tearDown() {
		 jsfServiceMock.resetErrors();
	 }
	 
	 @DirtiesContext
	 @Test
	 public void testCreateForm() {
		 
		 Form form = new Form();
		 form.setActive("1");
		 form.setDeleted(false);
		 form.setFormName("form1_test");
		 form.setGender("M");
		 form.setCoreTable("coreTable");
		 
		 formCrud.setItem(form);
		 formCrud.create();
		 
		 Form savedForm = genericDao.findByProperty(Form.class, "formName", form.getFormName(), false);
		 
		 for(String s : jsfServiceMock.getErrors())
			 System.out.println(s);
		 
		 assertNotNull(savedForm);
		 assertTrue(jsfServiceMock.getErrors().size() == 0);
	 }
	 
	 @DirtiesContext
	 @Test
	 public void testCreateDuplicateForm() { 
		 Form form = new Form();
		 form.setActive("1");
		 form.setDeleted(false);
		 form.setFormName("form1_test");
		 form.setGender("M");
		 form.setCoreTable("coreTable");
		 
		 formCrud.setItem(form);
		 formCrud.create();
		 
		 Form savedForm = genericDao.findByProperty(Form.class, "formName", form.getFormName(), false);
		 
		 assertNotNull(savedForm);
		 assertTrue(jsfServiceMock.getErrors().size() == 0);
		 
		 Form invalidForm = new Form();
		 invalidForm.setActive("1");
		 invalidForm.setDeleted(false);
		 invalidForm.setFormName("form1_test");
		 invalidForm.setGender("M");
		 
		 formCrud.setItem(invalidForm);
		 String createResult = formCrud.create();
		 
		 /* Expected result: A form already exists with that name. Please enter in a unique name.
		  * */
		 		 		 
		 assertNull(createResult);
		 assertTrue(jsfServiceMock.getErrors().size() == 1);
	 }
}
