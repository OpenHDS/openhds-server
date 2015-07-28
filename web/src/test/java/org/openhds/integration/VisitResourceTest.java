package org.openhds.integration;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.controller.service.SiteConfigService;
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
@DatabaseSetup(value = "/visitResourceDb.xml", type = DatabaseOperation.REFRESH)
public class VisitResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockHttpSession session;

	private MockMvc mockMvc;
	 
	 @Autowired
	private VisitCrudImpl visitCrud; 
	 
	 @Autowired
	private SiteConfigService siteConfigService;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();

		session = getMockHttpSession("admin", "test");
	}

	@Test
	public void testGetAllVisits() throws Exception {	
		mockMvc.perform(get("/visits").session(session).accept(MediaType.APPLICATION_XML))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/visits").nodeCount(1))
		.andExpect(xpath("/visits/visit").nodeCount(2))
		.andExpect(xpath("/visits/visit[1]/collectedBy/extId").string("FWEK1D"))
		.andExpect(xpath("/visits/visit[1]/extId").string("VLOCMBI11J"))
		.andExpect(xpath("/visits/visit[2]/extId").string("GND000001000"));	
	}

	@Test
	public void testPostVisit() throws Exception {
		final String VISIT_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<visit>"
					+ "<extId>ISE000012000</extId>"
					+ "<visitLocation>"
						+ "<extId>NJA000001</extId>"
					+ "</visitLocation>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<roundNumber>1</roundNumber>"
					+ "<visitDate>2015-01-01</visitDate>"
				+ "</visit>";

		/* Expected result:
		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			<visit>
				<collectedBy>
					<extId>FWEK1D</extId>
				</collectedBy>
				<extId>ISE000012000</extId>
				<roundNumber>1</roundNumber>
				<visitDate>01-01-2015</visitDate>
				<visitLocation>
					<extId>NJA000001</extId>
				</visitLocation>
			</visit>
		 */
		
		 //If visitLevel is set to location, post should be successfull
		 if(siteConfigService.getVisitAt().equalsIgnoreCase("location")){
				mockMvc.perform(post("/visits").session(session)
						.contentType(MediaType.APPLICATION_XML)
						.body(VISIT_POST_XML.getBytes()))
						.andExpect(status().isCreated())
						.andExpect(content().mimeType(MediaType.APPLICATION_XML))
						.andExpect(xpath("/visit/collectedBy/extId").string("FWEK1D"))
						.andExpect(xpath("/visit/extId").string("ISE000012000"))
						.andExpect(xpath("/visit/roundNumber").string("1"))
						.andExpect(xpath("/visit/visitDate").string("01-01-2015"))
						.andExpect(xpath("/visit/visitLocation/extId").string("NJA000001"));
		 }else{
				mockMvc.perform(post("/visits").session(session)
						.contentType(MediaType.APPLICATION_XML)
						.body(VISIT_POST_XML.getBytes()))
						.andExpect(status().isBadRequest())
						.andExpect(content().mimeType(MediaType.APPLICATION_XML))
						.andExpect(xpath("/failure").nodeCount(1))
						.andExpect(xpath("failure/errors").string("ISE0000120 is not of the required length as specified in the IdScheme. It must be 12 characters long.")); 
		 }

	}
	
	
	@Test
	public void testPostInvalidVisit() throws Exception {
		final String VISIT_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<visit>"
					+ "<extId>MBA00000001</extId>"
					+ "<visitLocation>"
						+ "<extId>NJA0000010000</extId>"
					+ "</visitLocation>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<roundNumber>1</roundNumber>"
					+ "<visitDate>2015-01-01</visitDate>"
				+ "</visit>";
		
		/*
		 * Expected return value:
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?><failure><errors>Invalid Location Id</errors></failure>
		 * */
		
		mockMvc.perform(post("/visits").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(VISIT_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("failure/errors").string("Invalid Location Id"));
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
