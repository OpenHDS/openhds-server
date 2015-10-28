package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.integration.util.WebContextLoader;
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

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader=WebContextLoader.class, locations={"/testContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
public class RelationshipResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private SitePropertiesService siteProperties;	

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
	public void testGetAllRelationships() throws Exception {	
		mockMvc.perform(get("/relationships").session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/relationships").exists());	
	}

	@Test
	public void testInsertRelationship() throws Exception {
		
		String expectedDate = "01-01-2015";
		
		final String RELATIONSHIP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<relationship>"
					+ "<individualA>"
						+ "<extId>NBAS1I</extId>"
					+ "</individualA>"		
					+ "<individualB>"
						+ "<extId>BJOH1J</extId>"
					+ "</individualB>"						
					+ "<aIsToB>3</aIsToB>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<endType>NA</endType>"
				+ "</relationship>";

		/* Expected result: RESULT: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 		<relationship>
			 * 		<individualA>
			 * 			<memberships/>
			 * 			<residencies/>
			 * 			<extId>NBAS1I</extId>
			 * 		</individualA>
			 * 		<individualB>
			 * 			<memberships/>
			 * 			<residencies/>
			 * 			<extId>BJOH1J</extId>
			 * 		</individualB>
			 * 		<startDate>01-01-2015</startDate>
			 * 		<aIsToB>3</aIsToB>
		 * 		</relationship>
		 */
		
		if(siteProperties != null && siteProperties.getEthiopianCalendar()){
			expectedDate = "23-04-2007";
		}
		
		mockMvc.perform(post("/relationships").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(RELATIONSHIP_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/relationship").nodeCount(1))
				.andExpect(xpath("/relationship/individualA/extId").string("NBAS1I"))
				.andExpect(xpath("/relationship/individualB/extId").string("BJOH1J"))
				.andExpect(xpath("/relationship/startDate").string(expectedDate))
				.andExpect(xpath("/relationship/aIsToB").string("3"));
	}
	
	@Test
	public void testInsertInvalidRelationship() throws Exception {
		
		final String RELATIONSHIP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<relationship>"
					+ "<individualA>"
						+ "<extId>NBAS1I</extId>"
					+ "</individualA>"		
					+ "<individualB>"
						+ "<extId>NBAS1I</extId>"
					+ "</individualB>"						
					+ "<aIsToB>2</aIsToB>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<startDate>2512-01-01</startDate>"
					+ "<endType>NA</endType>"
				+ "</relationship>";

		/* Expected result: RESULT: RESULT: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>A relationship must only be between a man and woman.</errors>
		 * 		<errors>No two related individuals can be the same.</errors>
		 * 		<errors>Start date should be in the past</errors>
		 * 	</failure>
		 */
		mockMvc.perform(post("/relationships").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(RELATIONSHIP_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(3));
//				.andExpect(xpath("/").node(Matchers.hasXPath("/failure/errors", StringContains.containsString("A relationship must only be between a man and woman"))))
//				.andExpect(xpath("/").node(Matchers.hasXPath("/failure/errors", StringContains.containsString("No two related individuals can be the same"))))
//				.andExpect(xpath("/").node(Matchers.hasXPath("/failure/errors", StringContains.containsString("Start date should be in the past"))))
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
