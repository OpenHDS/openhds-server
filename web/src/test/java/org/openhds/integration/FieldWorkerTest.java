package org.openhds.integration;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.FieldWorker;
import org.openhds.web.crud.impl.FieldWorkerCrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class FieldWorkerTest extends AbstractTransactionalJUnit4SpringContextTests {
		 
	 @Autowired
	 @Qualifier("fieldWorkerCrud")
	 FieldWorkerCrudImpl fieldWorkerCrud;
	 	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 @Autowired
	 GenericDao genericDao;
	 	 	 	 
	 @Autowired
	 @Qualifier("currentUser")
	 CurrentUser currentUser;
	 
	 @Test
	 public void testFieldWorkerCreate() {
		 
		 currentUser.setProxyUser("admin", "test", new String[] {"VIEW_ENTITY", "CREATE_ENTITY"});
		 
	     FieldWorker worker = new FieldWorker();
	     worker.setExtId("FWBD1");
	     worker.setFirstName("Bob");
	     worker.setLastName("Dow");
	     fieldWorkerCrud.setItem(worker);
	     fieldWorkerCrud.create();
	     
	     FieldWorker savedFieldWorker = genericDao.findByProperty(FieldWorker.class, "extId", worker.getExtId());
	     assertNotNull(savedFieldWorker);
	 }
}
