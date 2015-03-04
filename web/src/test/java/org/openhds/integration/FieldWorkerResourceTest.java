package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.integration.util.WebContextLoader;
import org.openhds.web.crud.impl.VisitCrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader=WebContextLoader.class, locations={"/testContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
@DatabaseSetup(value = "/fieldworkerResourceDb.xml", type = DatabaseOperation.REFRESH)
public class FieldWorkerResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockHttpSession session;

	private MockMvc mockMvc;
	 
	 @Autowired
	private VisitCrudImpl visitCrud; 

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();

		session = getMockHttpSession("admin", "test");
	}

	@Test
	public void testGetAllFieldWorkers() throws Exception {	
		mockMvc.perform(get("/fieldworkers").session(session).accept(MediaType.APPLICATION_XML))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/fieldworkers/fieldworker").nodeCount(3))
		.andExpect(xpath("/fieldworkers/fieldworker[3]/extId").string("FWNF5"))
		.andExpect(xpath("/fieldworkers/fieldworker[3]/passwordHash").string("invalid-pw-hash"));	
		
		/*	Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 		<fieldworkers>
		 * 			<fieldworker>
		 * 				<uuid>UnknownFieldWorker</uuid>
		 * 				<extId>UNK</extId>
		 * 				<firstName>Unknown</firstName>
		 * 				<lastName>FieldWorker</lastName>
		 * 				<passwordHash>invalid-password-hash</passwordHash>
		 * 			</fieldworker>
		 * 			<fieldworker>
		 * 				<uuid>FieldWorker1</uuid>
		 * 				<extId>FWEK1D</extId>
		 * 				<firstName>Editha</firstName>
		 * 				<lastName>Kaweza</lastName>
		 * 				<passwordHash>invalid-password-hash</passwordHash>
		 * 				</fieldworker>
		 * 			<fieldworker>
		 * 				<uuid>new_fieldworker_uuid</uuid>
		 * 				<extId>FWNF5</extId>
		 * 				<firstName>Field</firstName>
		 * 				<lastName>Worker</lastName>
		 * 				<passwordHash>invalid-pw-hash</passwordHash>
		 * 			</fieldworker>
		 * 		</fieldworkers>
		 * */
	}

	private MockHttpSession getMockHttpSession(String username, String password) throws Exception {
		return (MockHttpSession)mockMvc.perform(post("/loginProcess")
				.param("j_username", username)
				.param("j_password", password)
				).andReturn()
				.getRequest()
				.getSession();	
	}
}
