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
public class PregnancyOutcomeResourceTest {

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
	public void testPostPregnancyOutcome() throws Exception {
		final String POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyoutcome>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
					+ "<childEverBorn>1</childEverBorn>"
					+ "<numberOfLiveBirths>1</numberOfLiveBirths>"
					+ "<outcomeDate>2015-01-01</outcomeDate>"
					+ "<mother>"
						+ "<extId>NBAS1I</extId>" //BHAR1K = male
					+ "</mother>"		
					+ "<father>"
						+ "<extId>UNK</extId>" //BHAR1K = male
					+ "</father>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<outcomes>"
						+ "<outcome>"
							+ "<type>LIB</type>"
							+ "<child>"
								+ "<extId>TEST</extId>"
							+ "</child>"
							+ "<childextId>TEST</childextId>"
							+ "<childMembership>"
								+ "<individual>"
									+ "<extId>TEST</extId>"
								+ "</individual>"
								+ "<socialGroup>"
									+ "<extId>MBI1</extId>"
								+ "</socialGroup>"	
								+ "<startDate>2015-01-01</startDate>"
								+ "<startType>BIR</startType>"
								+ "<endType>NA</endType>"
								+ "<bIsToA>3</bIsToA>"
							+ "</childMembership>"
						+ "</outcome>"
					+ "</outcomes>"
				+ "</pregnancyoutcome>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<pregnancyoutcome>
		 * 		<collectedBy>
		 * 			<extId>FWEK1D</extId>
		 * 		</collectedBy>
		 * 		<childEverBorn>2</childEverBorn>
		 * 		<father>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>UNK</extId>
		 * 		</father>
		 * 		<mother>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<extId>NBAS1I</extId>
		 * 		</mother>
		 * 		<numberOfLiveBirths>0</numberOfLiveBirths>
		 * 		<outcomeDate>01-01-2015</outcomeDate>
		 * 		<outcomes>
		 * 			<outcome>
		 * 				<type>LIB</type>
		 * 			</outcome>
		 * 		</outcomes>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</pregnancyoutcome>
		 */
		mockMvc.perform(post("/pregnancyoutcomes").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/pregnancyoutcome").nodeCount(1))
				.andExpect(xpath("/pregnancyoutcome/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/pregnancyoutcome/mother").nodeCount(1))
				.andExpect(xpath("/pregnancyoutcome/mother/extId").string("NBAS1I"))
				.andExpect(xpath("/pregnancyoutcome/outcomeDate").string("01-01-2015"))
				.andExpect(xpath("/pregnancyoutcome/father/extId").string("UNK"))
				.andExpect(xpath("/pregnancyoutcome/childEverBorn").string("2"));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidPregnancyOutcome() throws Exception {
		final String INVALID_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyoutcome>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
					+ "<childEverBorn>1</childEverBorn>"
					+ "<numberOfLiveBirths>1</numberOfLiveBirths>"
					+ "<outcomeDate>2015-01-01</outcomeDate>"
					+ "<mother>"
						+ "<extId>NBAS1I</extId>" //BHAR1K = male
					+ "</mother>"		
					+ "<father>"
						+ "<extId>NBAS1I</extId>" //BHAR1K = male
					+ "</father>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<outcomes>"
						+ "<outcome>"
							+ "<type>LIB</type>"
							+ "<child>"
								+ "<extId>TEST</extId>"
							+ "</child>"
							+ "<childextId>TEST</childextId>"
							+ "<childMembership>"
								+ "<individual>"
									+ "<extId>TEST</extId>"
								+ "</individual>"
								+ "<socialGroup>"
									+ "<extId>MBI1</extId>"
								+ "</socialGroup>"	
								+ "<startDate>2015-01-01</startDate>"
								+ "<startType>BIR</startType>"
								+ "<endType>NA</endType>"
								+ "<bIsToA>3</bIsToA>"
							+ "</childMembership>"
						+ "</outcome>"
					+ "</outcomes>"
				+ "</pregnancyoutcome>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>The gender specified must be Male.</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/pregnancyoutcomes").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Male")));	
	}
	
	
	@DirtiesContext
	@Test
	public void testPostInvalidPregnancyObservation2() throws Exception {
		final String INVALID_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<pregnancyoutcome>"
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"
					+ "<childEverBorn>1</childEverBorn>"
					+ "<numberOfLiveBirths>1</numberOfLiveBirths>"
					+ "<outcomeDate>2015-01-01</outcomeDate>"
					+ "<mother>"
						+ "<extId>NBAS1I</extId>"
					+ "</mother>"		
					+ "<father>"
						+ "<extId>BJOH1O</extId>"
					+ "</father>"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<outcomes>"
						+ "<outcome>"
							+ "<type>LIB</type>"
							+ "<child>"
								+ "<extId>TEST</extId>"
							+ "</child>"
							+ "<childextId>TEST</childextId>"
							+ "<childMembership>"
								+ "<individual>"
									+ "<extId>TEST</extId>"
								+ "</individual>"
								+ "<socialGroup>"
									+ "<extId>MBI1</extId>"
								+ "</socialGroup>"	
								+ "<startDate>2015-01-01</startDate>"
								+ "<startType>BIR</startType>"
								+ "<endType>NA</endType>"
								+ "<bIsToA>3</bIsToA>"
							+ "</childMembership>"
						+ "</outcome>"
					+ "</outcomes>"
				+ "</pregnancyoutcome>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Invalid father id</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/pregnancyoutcomes").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Invalid")));		
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
