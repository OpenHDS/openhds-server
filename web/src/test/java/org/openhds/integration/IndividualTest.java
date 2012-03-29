package org.openhds.integration;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.CurrentUser;
import org.openhds.domain.model.Individual;
import org.openhds.web.crud.impl.EntityCrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.easymock.EasyMock.createMock;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class IndividualTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	 @Autowired
	 @Qualifier("individualCrud")
	 EntityCrudImpl<Individual, String> individualCrud;
	 
	 @Autowired
	 SessionFactory sessionFactory;
	 
	 CurrentUser currentUser = createMock(CurrentUser.class);
	 
	 @Test
	 public void testIndividualCreate() {

	 }
	
	

}
