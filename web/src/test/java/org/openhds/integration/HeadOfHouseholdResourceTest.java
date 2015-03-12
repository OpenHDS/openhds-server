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
public class HeadOfHouseholdResourceTest {

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
	public void testPostHeadOfHousehold() throws Exception {
		final String HOH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death_of_hh>"
					+ "<oldHoh>"
						+ "<extId>NBAS1I</extId>"
					+ "</oldHoh>"	
					+ "<newHoh>"
						+ "<extId>BJOH1J</extId>"
					+ "</newHoh>"	
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathPlace>UNK</deathPlace>"
					+ "<deathCause>NA</deathCause>"
					+ "<deathDate>2015-01-01</deathDate>"					
					+ "<death>"
						+ "<individual>"
							+ "<extId>NBAS1I</extId>"
						+ "</individual>"
						+ "<deathPlace>UNK</deathPlace>"
						+ "<deathCause>NA</deathCause>"
						+ "<deathDate>2015-01-01</deathDate>"				
						+ "<visitDeath>"
							+ "<extId>VLOCMBI11J</extId>"
						+ "</visitDeath>"
						+ "<ageAtDeath>99</ageAtDeath>"
						+ "<collectedBy>"
							+ "<extId>FWEK1D</extId>"
						+ "</collectedBy>"						
					+ "</death>"
					+ "<socialGroup>"
						+ "<extId>MBI1</extId>"
					+ "</socialGroup>"						
				+ "</death_of_hh>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<death_of_hh>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<death>
		 * 			<collectedBy>	
		 * 				<extId>FWEK1D</extId>
		 * 			</collectedBy>
		 * 			<ageAtDeath>20102</ageAtDeath>
		 * 			<deathCause>NA</deathCause>
		 * 			<deathDate>01-01-2015</deathDate>
		 * 			<deathPlace>UNK</deathPlace>
		 * 			<individual>
		 * 				<memberships/>
		 * 				<residencies/>
		 * 				<extId>NBAS1I</extId>
		 * 			</individual>
		 * 			<visitDeath>
		 * 				<extId>VLOCMBI11J</extId>
		 * 			</visitDeath>
		 * 		</death>
		 * 		<memberships/>
		 * 		<newHoh>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>BJOH1J</extId>
		 * 		</newHoh>
		 * 		<oldHoh>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>NBAS1I</extId>
		 * 		</oldHoh>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</death_of_hh>
		 */
		mockMvc.perform(post("/hoh").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(HOH_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/death_of_hh").nodeCount(1))
				.andExpect(xpath("/death_of_hh/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/death_of_hh/newHoh").nodeCount(1))
				.andExpect(xpath("/death_of_hh/newHoh/extId").string("BJOH1J"))
				.andExpect(xpath("/death_of_hh/oldHoh").nodeCount(1))
				.andExpect(xpath("/death_of_hh/oldHoh/extId").string("NBAS1I"))				
				.andExpect(xpath("/death_of_hh/visit/extId").string("VLOCMBI11J"));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidHeadOfHousehold() throws Exception {
		final String INVALID_HOH_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<death_of_hh>"
					+ "<oldHoh>"
						+ "<extId>BJOH1J</extId>"
					+ "</oldHoh>"	
					+ "<newHoh>"
						+ "<extId>NBAS1I</extId>"
					+ "</newHoh>"	
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<deathPlace>UNK</deathPlace>"
					+ "<deathCause>NA</deathCause>"
					+ "<deathDate>2015-01-01</deathDate>"					
					+ "<death>"
						+ "<individual>"
							+ "<extId>BJOH1J</extId>"
						+ "</individual>"
						+ "<deathPlace>UNK</deathPlace>"
						+ "<deathCause>NA</deathCause>"
						+ "<deathDate>2015-01-01</deathDate>"				
						+ "<visitDeath>"
							+ "<extId>VLOCMBI11J</extId>"
						+ "</visitDeath>"
						+ "<ageAtDeath>99</ageAtDeath>"
						+ "<collectedBy>"
							+ "<extId>FWEK1D</extId>"
						+ "</collectedBy>"						
					+ "</death>"
					+ "<socialGroup>"
						+ "<extId>MBI1</extId>"
					+ "</socialGroup>"						
				+ "</death_of_hh>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Specified old HoH-extId is not the current HoH!</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/hoh").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_HOH_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("current")));	
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
