package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
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
public class DeathResourceTest {

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
	public void testPostDeath() throws Exception {
		
		String expectedDate = "01-01-2015";
		
		final String DEATH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death>"
					+ "<individual>"
						+ "<extId>BJOH1J</extId>"
					+ "</individual>"
					+ "<deathPlace>Hospital</deathPlace>"
					+ "<deathCause>UNK</deathCause>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathDate>2015-01-01</deathDate>"
					+ "<visitDeath>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visitDeath>"
					+ "<ageAtDeath>1</ageAtDeath>"
				+ "</death>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<death>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<deathCause>UNK</deathCause>
		 * 		<deathDate>01-01-2015</deathDate>
		 * 		<deathPlace>Hospital</deathPlace>
		 * 		<individual>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>BJOH1J</extId>
		 * 		</individual>
		 * 		<visitDeath>
		 * 			<extId>VLOCMBI11J</extId>
		 * 			<visitDate>15-07-2010</visitDate>
		 * 		</visitDeath>
		 * 	</death>
		 */
		
		if(siteProperties != null && siteProperties.getEthiopianCalendar()){
			expectedDate = "23-04-2007";
		}
		
		mockMvc.perform(post("/deaths").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(DEATH_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/death").nodeCount(1))
				.andExpect(xpath("/death/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/death/deathCause").string("UNK"))
				.andExpect(xpath("/death/individual/extId").string("BJOH1J"))
				.andExpect(xpath("/death/deathDate").string(expectedDate))
				.andExpect(xpath("/death/deathPlace").string("Hospital"));
	}
	
	
	@Test
	public void testPostInvalidDeath() throws Exception {
		final String DEATH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death>"
					+ "<individual>"
						+ "<extId>BJOH1J0</extId>"
					+ "</individual>"
					+ "<deathPlace>Hospital</deathPlace>"
					+ "<deathCause>UNK</deathCause>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathDate>2015-01-01</deathDate>"
					+ "<visitDeath>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visitDeath>"
					+ "<ageAtDeath>1</ageAtDeath>"
				+ "</death>";
		
		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Individual external id referenced in death event is invalid</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/deaths").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(DEATH_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("invalid")));
	}
	
	@Test
	public void testPostDuplicateDeath() throws Exception {
		
		String expectedDate = "01-01-2015";
		
		final String DEATH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death>"
					+ "<individual>"
						+ "<extId>BJOH1J</extId>"
					+ "</individual>"
					+ "<deathPlace>Hospital</deathPlace>"
					+ "<deathCause>UNK</deathCause>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathDate>2015-01-01</deathDate>"
					+ "<visitDeath>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visitDeath>"
					+ "<ageAtDeath>1</ageAtDeath>"
				+ "</death>";
				
		
		if(siteProperties != null && siteProperties.getEthiopianCalendar()){
			expectedDate = "23-04-2007";
		}

		mockMvc.perform(post("/deaths").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(DEATH_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/death").nodeCount(1))
				.andExpect(xpath("/death/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/death/deathCause").string("UNK"))
				.andExpect(xpath("/death/individual/extId").string("BJOH1J"))
				.andExpect(xpath("/death/deathDate").string(expectedDate))
				.andExpect(xpath("/death/deathPlace").string("Hospital"));	
		
		final String DUPLICATE_DEATH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death>"
					+ "<individual>"
						+ "<extId>BJOH1J</extId>"
					+ "</individual>"
					+ "<deathPlace>Hospital</deathPlace>"
					+ "<deathCause>UNK</deathCause>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathDate>2015-01-01</deathDate>"
					+ "<visitDeath>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visitDeath>"
					+ "<ageAtDeath>1</ageAtDeath>"
				+ "</death>";
		
		
		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The Individual Id specified already exists.</errors>
		 * 	</failure>
		 */
		
		mockMvc.perform(post("/deaths").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(DUPLICATE_DEATH_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("already exists")));		
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
