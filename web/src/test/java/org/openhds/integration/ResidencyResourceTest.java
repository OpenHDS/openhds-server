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
public class ResidencyResourceTest {

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
	public void testPostResidency() throws Exception {
		final String POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<residency>"
					+ "<individual>"
						+ "<extId>CBLA1H</extId>" //NBAS1I = ind. with open residency
					+ "</individual>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<startType>ENU</startType>"	
					+ "<location>"
						+ "<extId>NJA000001</extId>"
					+ "</location>"		
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"								
				+ "</residency>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<residency>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<individual>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>CBLA1H</extId>
		 * 		</individual>
		 * 		<location>
		 * 			<extId>NJA000001</extId>
		 * 			<locationLevel/>
		 * 		</location>
		 * 		<startDate>01-01-2015</startDate>
		 * 		<startType>ENU</startType>
		 * 	</residency>
		 */
		mockMvc.perform(post("/residencyimg").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/residency").nodeCount(1))
				.andExpect(xpath("/residency/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/residency/location/extId").string("NJA000001"))
				.andExpect(xpath("/residency/startDate").string("01-01-2015"))
				.andExpect(xpath("/residency/individual/extId").string("CBLA1H"))
				.andExpect(xpath("/residency/startType").string("ENU"));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidResidency() throws Exception {
		final String INVALID_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<residency>"
					+ "<individual>"
						+ "<extId>NBAS1I</extId>"
					+ "</individual>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<startType>ENU</startType>"	
					+ "<location>"
						+ "<extId>NJA000001</extId>"
					+ "</location>"		
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"								
				+ "</residency>";
		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The individual already has an open residency. You must close the current residency for this Individual before a new Residency can be created.</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/residencyimg").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("close")));		
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidResidency2() throws Exception {
		final String INVALID_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<residency>"
					+ "<individual>"
						+ "<extId>NBAS1I</extId>"
					+ "</individual>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<startType>ENU</startType>"	
					+ "<location>"
						+ "<extId>NJA000001</extId>"
					+ "</location>"		
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"								
				+ "</residency>";
		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>
		 * 			The individual already has an open residency. 
		 * 			You must close the current residency for this Individual before a new Residency can be created.
		 * 		</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/residencyimg").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("close")));	
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
