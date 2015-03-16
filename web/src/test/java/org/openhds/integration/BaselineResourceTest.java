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
public class BaselineResourceTest {

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
	public void testPostBaseline() throws Exception {
		final String BASELINE_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<inmigration>"
					+ "<individual>"
						+ "<extId>BJOH2J5GH5GH</extId>"
						+ "<firstName>BASELINE</firstName>"	
						+ "<lastName>BASELINE</lastName>"	
						+ "<gender>M</gender>"	
						+ "<dob>2015-01-01</dob>"	
						+ "<dobAspect>1</dobAspect>"	
						+ "<mother>"
							+ "<extId>UNK</extId>"
						+ "</mother>"
						+ "<father>"
							+ "<extId>UNK</extId>"
						+ "</father>"							
					+ "</individual>"	
					+ "<origin>BASELINE</origin>"	
					+ "<reason>BASELINE</reason>"
					+ "<recordedDate>2015-01-01</recordedDate>"					
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"		
					+ "<migType>BASELINE</migType>"							
				+ "</inmigration>";

		/* Expected result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<inmigration>
		 * 		<collectedBy><extId>FWEK1D</extId></collectedBy>
		 * 		<individual>
		 * 			<memberships/><residencies/><extId>BJOH2J5GH5GH</extId>
		 * 		</individual>
		 * 		<migType>BASELINE</migType>
		 * 		<origin>ENU</origin>
		 * 		<reason>ENU</reason>
		 * 		<recordedDate>01-01-2015</recordedDate>
		 * 		<residency/>
		 * 		<unknownIndividual>false</unknownIndividual>
		 * 		<visit>
		 * 			<extId>VLOCMBI11J</extId>
		 * 		</visit>
		 * 	</inmigration>
		 */
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(BASELINE_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/inmigration").nodeCount(1))
				.andExpect(xpath("/inmigration/migType").string("BASELINE"))
				.andExpect(xpath("/inmigration/origin").string("ENU"))
				.andExpect(xpath("/inmigration/reason").string("ENU"))
				.andExpect(xpath("/inmigration/unknownIndividual").string("false"));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidBaselineInvalidIndividualExtId() throws Exception {
		final String INVALID_BASELINE_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<inmigration>"
				+ "<individual>"
					+ "<extId>BJOH2J</extId>"
					+ "<firstName>BASELINE</firstName>"	
					+ "<lastName>BASELINE</lastName>"	
					+ "<gender>M</gender>"	
					+ "<dob>2015-01-01</dob>"	
					+ "<dobAspect>1</dobAspect>"					
					+ "<mother>"
						+ "<extId>UNK</extId>"
					+ "</mother>"
					+ "<father>"
						+ "<extId>UNK</extId>"
					+ "</father>"							
				+ "</individual>"	
				+ "<origin>BASELINE</origin>"	
				+ "<reason>BASELINE</reason>"
				+ "<recordedDate>2015-01-01</recordedDate>"					
				+ "<visit>"
					+ "<extId>VLOCMBI11J</extId>"
				+ "</visit>"							
				+ "<collectedBy>"
					+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"		
				+ "<migType>BASELINE</migType>"							
			+ "</inmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>...IdScheme...</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_BASELINE_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("IdScheme")));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigrationInvalidMotherExtId() throws Exception {
		final String INVALID_INDIVIDUAL_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<inmigration>"
				+ "<individual>"
					+ "<extId>BJOH2J5GH5GH</extId>"
					+ "<firstName>BASELINE</firstName>"	
					+ "<lastName>BASELINE</lastName>"	
					+ "<gender>M</gender>"	
					+ "<dob>2015-01-01</dob>"	
					+ "<dobAspect>1</dobAspect>"					
					+ "<mother>"
						+ "<extId>BJOH2J5GH5</extId>"
					+ "</mother>"
					+ "<father>"
						+ "<extId>UNK</extId>"
					+ "</father>"							
				+ "</individual>"	
				+ "<origin>BASELINE</origin>"	
				+ "<reason>BASELINE</reason>"
				+ "<recordedDate>2015-01-01</recordedDate>"					
				+ "<visit>"
					+ "<extId>VLOCMBI11J</extId>"
				+ "</visit>"							
				+ "<collectedBy>"
					+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"		
				+ "<migType>BASELINE</migType>"							
			+ "</inmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Invalid mother id</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_INDIVIDUAL_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Invalid mother id")));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigrationInvalidFatherExtId() throws Exception {
		final String INVALID_INDIVIDUAL_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<inmigration>"
				+ "<individual>"
					+ "<extId>BJOH2J5GH5GH</extId>"
					+ "<firstName>BASELINE</firstName>"	
					+ "<lastName>BASELINE</lastName>"	
					+ "<gender>M</gender>"	
					+ "<dob>2015-01-01</dob>"	
					+ "<dobAspect>1</dobAspect>"					
					+ "<mother>"
						+ "<extId>UNK</extId>"
					+ "</mother>"
					+ "<father>"
						+ "<extId>1234</extId>"
					+ "</father>"							
				+ "</individual>"	
				+ "<origin>BASELINE</origin>"	
				+ "<reason>BASELINE</reason>"
				+ "<recordedDate>2015-01-01</recordedDate>"					
				+ "<visit>"
					+ "<extId>VLOCMBI11J</extId>"
				+ "</visit>"							
				+ "<collectedBy>"
					+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"		
				+ "<migType>BASELINE</migType>"							
			+ "</inmigration>";

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Invalid father id</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_INDIVIDUAL_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Invalid father id")));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigrationInvalidBaselineMigrationType() throws Exception {
		
		final String INVALID_MIGRATIONTYPE_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<inmigration>"
					+ "<individual>"
						+ "<extId>BJOH2J5GH5GH</extId>"
						+ "<firstName>BASELINE</firstName>"	
						+ "<lastName>BASELINE</lastName>"	
						+ "<gender>M</gender>"	
						+ "<dob>2015-01-01</dob>"	
						+ "<dobAspect>1</dobAspect>"					
						+ "<mother>"
							+ "<extId>UNK</extId>"
						+ "</mother>"
						+ "<father>"
							+ "<extId>UNK</extId>"
						+ "</father>"							
					+ "</individual>"	
					+ "<origin>BASELINE</origin>"	
					+ "<reason>BASELINE</reason>"
					+ "<recordedDate>2015-01-01</recordedDate>"					
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"		
					+ "<migType>UNK</migType>"							
				+ "</inmigration>";		

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Invalid Migration Type</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_MIGRATIONTYPE_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("Invalid Migration Type")));	
	}
	
	@DirtiesContext
	@Test
	public void testPostInvalidInmigrationIncompleteIndividualInformation() throws Exception {
		
		final String INVALID_INDIVIDUAL_INFORMATION_INMIGRATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<inmigration>"
					+ "<individual>"
						+ "<extId>BJOH2J5GH5GH</extId>"
						+ "<firstName>BASELINE</firstName>"	
						+ "<lastName>BASELINE</lastName>"	
//						+ "<gender>M</gender>"	
//						+ "<dob>2015-01-01</dob>"	
//						+ "<dobAspect>1</dobAspect>"					
						+ "<mother>"
							+ "<extId>UNK</extId>"
						+ "</mother>"
						+ "<father>"
							+ "<extId>UNK</extId>"
						+ "</father>"							
					+ "</individual>"	
					+ "<origin>BASELINE</origin>"	
					+ "<reason>BASELINE</reason>"
					+ "<recordedDate>2015-01-01</recordedDate>"					
					+ "<visit>"
						+ "<extId>VLOCMBI11J</extId>"
					+ "</visit>"							
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"		
					+ "<migType>BASELINE</migType>"							
				+ "</inmigration>";		

		/*
		 * Expected return value: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Dob cannot be null.</errors>
		 * 		<errors>Please specify a gender.</errors>
		 * 		<errors>dobAspect cannot be empty!</errors>
		 * 	</failure>
		 **/
		
		mockMvc.perform(post("/baseline").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(INVALID_INDIVIDUAL_INFORMATION_INMIGRATION_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(3))
				.andExpect(xpath("/failure/errors[contains(text(),'Dob cannot be null')]").exists())
				.andExpect(xpath("/failure/errors[contains(text(),'Please specify a gender')]").exists())
				.andExpect(xpath("/failure/errors[contains(text(),'dobAspect cannot be empty')]").exists());	
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
