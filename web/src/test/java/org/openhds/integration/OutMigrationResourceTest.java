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
public class OutMigrationResourceTest {

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
	public void testPostOutmigration() throws Exception {
		final String OUTMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<outmigration>"
					+ "<individual>"
						+ "<extId>BHAR1K</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<destination>UNK</destination>"
					+ "<recordedDate>2015-01-01</recordedDate>"
					+ "<reason>NA</reason>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</outmigration>";

		/* Expected result: RESULT: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<outmigration>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<destination>UNK</destination>
		 * 		<individual>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>BHAR1K</extId>
		 * 		</individual>
		 * 		<reason>NA</reason>
		 * 		<recordedDate>01-01-2015</recordedDate>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</outmigration>
		 */
		mockMvc.perform(post("/outmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(OUTMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/outmigration").nodeCount(1))
				.andExpect(xpath("/outmigration/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/outmigration/individual").nodeCount(1))
				.andExpect(xpath("/outmigration/individual/extId").string("BHAR1K"))
				.andExpect(xpath("/outmigration/reason").string("NA"))
				.andExpect(xpath("/outmigration/visit/extId").string("VLOCMBI11J"))
				.andExpect(xpath("/outmigration/destination").string("UNK"));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidOutMigration() throws Exception {
		final String OUTMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<outmigration>"
					+ "<individual>"
						+ "<extId>BHAR1K</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<destination>UNK</destination>"
					+ "<recordedDate>1800-01-01</recordedDate>"
					+ "<reason>NA</reason>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</outmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The end date cannot be before the start date</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/outmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(OUTMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("date")));			
	}
	
	
	@DirtiesContext
	@Test
	public void testPostDuplicateOutMigration() throws Exception {
		final String OUTMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<outmigration>"
					+ "<individual>"
						+ "<extId>BHAR1K</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<destination>UNK</destination>"
					+ "<recordedDate>2015-01-01</recordedDate>"
					+ "<reason>NA</reason>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</outmigration>";

		/*
		 * 	Expected return value: 
		 *	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 *	<failure>
		 *		<errors>The Individual you entered does not have an open residency. 
		 *				In order to complete an out migration, the Individual must have an open residency.
		 *		</errors>
		 *	</failure>
		 * */
		
		mockMvc.perform(post("/outmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(OUTMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML));
		
		final String DUPLICATE_OUTMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<outmigration>"
					+ "<individual>"
						+ "<extId>BHAR1K</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<destination>UNK</destination>"
					+ "<recordedDate>2015-01-04</recordedDate>"
					+ "<reason>NA</reason>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</outmigration>";
		
		mockMvc.perform(post("/outmigrations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(DUPLICATE_OUTMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("residency")));
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
