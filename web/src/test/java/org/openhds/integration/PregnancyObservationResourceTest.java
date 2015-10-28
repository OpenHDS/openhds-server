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
public class PregnancyObservationResourceTest {

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
	@DirtiesContext
	public void testPostPregnancyObservation() throws Exception {
		
		String expectedDate = "01-01-2015";
		String expectedDeliveryDate = "2015-08-01";
		
		
		final String PREGNANCY_OBSERVATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyobservation>"
					+ "<mother>"
						+ "<extId>NBAS1I</extId>" //BHAR1K = male
					+ "</mother>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<expectedDeliveryDate>2015-08-01</expectedDeliveryDate>"
					+ "<recordedDate>2015-01-01</recordedDate>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</pregnancyobservation>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<pregnancyobservation>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<expectedDeliveryDate>01-08-2015</expectedDeliveryDate>
		 * 		<mother>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>NBAS1I</extId>
		 * 		</mother>
		 * 		<recordedDate>01-01-2015</recordedDate>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</pregnancyobservation>
		 */
		
		if(siteProperties != null && siteProperties.getEthiopianCalendar()){
			expectedDate = "23-04-2007";
			expectedDeliveryDate = "25-11-2007";
		}
		
		mockMvc.perform(post("/pregnancyobservations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(PREGNANCY_OBSERVATION_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/pregnancyobservation").nodeCount(1))
				.andExpect(xpath("/pregnancyobservation/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/pregnancyobservation/mother").nodeCount(1))
				.andExpect(xpath("/pregnancyobservation/mother/extId").string("NBAS1I"))
				.andExpect(xpath("/pregnancyobservation/recordedDate").string(expectedDate))
				.andExpect(xpath("/pregnancyobservation/visit/extId").string("VLOCMBI11J"))
				.andExpect(xpath("/pregnancyobservation/expectedDeliveryDate").string(expectedDeliveryDate));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidPregnancyObservation() throws Exception {
		final String INVALID_PREGNANCY_OBSERVATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyobservation>"
					+ "<mother>"
						+ "<extId>BHAR1K</extId>"
					+ "</mother>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<expectedDeliveryDate>2015-08-01</expectedDeliveryDate>"
					+ "<recordedDate>2015-01-01</recordedDate>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</pregnancyobservation>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The mother specified is not female gender</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/pregnancyobservations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_PREGNANCY_OBSERVATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("female")));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidPregnancyObservation2() throws Exception {
		final String INVALID_PREGNANCY_OBSERVATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyobservation>"
					+ "<mother>"
						+ "<extId>NBAS1I</extId>"
					+ "</mother>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<expectedDeliveryDate>1960-01-01</expectedDeliveryDate>"
					+ "<recordedDate>2015-01-01</recordedDate>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
				+ "</pregnancyobservation>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The Mother specified is younger than the minimum age required to have a Pregnancy Observation.</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/pregnancyobservations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_PREGNANCY_OBSERVATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("minimum")));	
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
