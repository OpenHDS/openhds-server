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
public class IndividualResourceTest {

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
	public void testGetAllIndividuals() throws Exception {	
		mockMvc.perform(get("/individuals").session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/individuals").exists());	
	}

	@Test
	public void testGetIndividualByExtId() throws Exception {
		
		String expectedDate = "19-12-1965";
		
		/* Expected return value:
		 * 
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 		<individual>
		 * 			<memberships/>
		 * 			<residencies/>
		 * 			<dob>19-12-1965</dob>
		 * 			<extId>BJOH1J</extId>
		 * 			<father>
		 * 				<memberships/>
		 * 				<residencies/>
		 * 				<extId>UNK</extId>
		 * 			</father>
		 * 			<firstName>Bob</firstName>
		 * 			<gender>M</gender>
		 * 			<lastName>Johnson</lastName>
		 * 			<middleName></middleName>
		 * 			<mother>
		 * 				<memberships/>
		 * 				<residencies/>
		 * 				<extId>UNK</extId>
		 * 			</mother>
		 * 		</individual>
		 * */
		
		String individualExtId = "BJOH1J";
		
		if(siteProperties != null && siteProperties.getEthiopianCalendar()){
			expectedDate = "10-04-1958";
		}

		mockMvc.perform(get("/individuals/{extId}", individualExtId).session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/individual").nodeCount(1))
		.andExpect(xpath("/individual/dob").string(expectedDate))
		.andExpect(xpath("/individual/gender").string("M"))
		.andExpect(xpath("/individual/firstName").string("Bob"))
		.andExpect(xpath("/individual/middleName").string(""))
		.andExpect(xpath("/individual/lastName").string("Johnson"))
		.andExpect(xpath("/individual/father/extId").string("UNK"))
		.andExpect(xpath("/individual/mother/extId").string("UNK"));
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
