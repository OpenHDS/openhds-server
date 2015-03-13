package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.runner.RunWith;
import org.openhds.integration.util.WebContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
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


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader=WebContextLoader.class, locations={"/testContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
public class InmigrationResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockHttpSession session;

	private MockMvc mockMvc;
	 
	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();

		session = getMockHttpSession("admin", "test");
	}

	@Test
	@DirtiesContext
	public void testPostInmigration() throws Exception {
		final String INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<inmigration>"
					+ "<individual>"
						+ "<extId>BJOH1J</extId>"
					+ "</individual>"	
					+ "<origin>SOMEWHERE</origin>"	
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<reason>Fishing</reason>"
					+ "<recordedDate>2015-01-01</recordedDate>"										
				+ "</inmigration>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<inmigration>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<individual>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>BJOH1J</extId>
		 * 		</individual>
		 * 		<migType>INTERNAL_INMIGRATION</migType>
		 * 		<origin>SOMEWHERE</origin>
		 * 		<reason>Fishing</reason>
		 * 		<recordedDate>01-01-2015</recordedDate>
		 * 		<residency/>
		 * 		<unknownIndividual>false</unknownIndividual>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</inmigration>
		 */
		mockMvc.perform(post("/inmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/inmigration").nodeCount(1))
				.andExpect(xpath("/inmigration/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/inmigration/individual").nodeCount(1))
				.andExpect(xpath("/inmigration/individual/extId").string("BJOH1J"))
				.andExpect(xpath("/inmigration/migType").string("INTERNAL_INMIGRATION"))
				.andExpect(xpath("/inmigration/origin").string("SOMEWHERE"))
				.andExpect(xpath("/inmigration/unknownIndividual").string("false"))				
				.andExpect(xpath("/inmigration/visit/extId").string("VLOCMBI11J"));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigration() throws Exception {
		final String INVALID_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<inmigration>"
				+ "<individual>"
					+ "<extId>NBAS1I</extId>"
				+ "</individual>"	
				+ "<origin>SOMEWHERE</origin>"	
				+ "<visit>"
					+ "<extId>VLOCMBI11J</extId>"
				+ "</visit>"							
				+ "<collectedBy>"
					+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"
				+ "<reason>Fishing</reason>"
				+ "<recordedDate>2015-01-01</recordedDate>"										
			+ "</inmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The individual already has an open residency. 
		 * 		You must close the current residency for this Individual before a new Residency can be created.
		 * 		</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/inmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("open residency")));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigrationUnknownIndividual() throws Exception {
		final String INVALID_INDIVIDUAL_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<inmigration>"
				+ "<individual>"
					+ "<extId>NBAS12</extId>"
				+ "</individual>"	
				+ "<origin>SOMEWHERE</origin>"	
				+ "<visit>"
					+ "<extId>VLOCMBI11J</extId>"
				+ "</visit>"							
				+ "<collectedBy>"
					+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"
				+ "<reason>Fishing</reason>"
				+ "<recordedDate>2015-01-01</recordedDate>"										
			+ "</inmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Invalid individual id</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/inmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_INDIVIDUAL_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Invalid individual id")));	
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
