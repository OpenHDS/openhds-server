package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class MembershipResourceTest {

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
	public void testPostMembership() throws Exception {
		final String MEMBERSHIP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<membership>"
					+ "<extId>ISE000012000</extId>"
					+ "<socialGroup>"
						+ "<extId>MBI1</extId>"
					+ "</socialGroup>"
					+ "<individual>"
						+ "<extId>NBAS1I</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<startType>IMG</startType>"
					+ "<endType>NA</endType>"
					+ "<bIsToA>1</bIsToA>"
				+ "</membership>";

		/* Expected result: 
		 * 		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 		<membership>
		 * 			<collectedBy>
		 * 				<extId>FWEK1D</extId>
		 * 			</collectedBy>
		 * 			<endType>NA</endType>
		 * 			<individual>
		 * 				<memberships/>
		 * 				<residencies/>
		 * 				<extId>NBAS1I</extId>
		 * 			</individual>
		 * 			<socialGroup>
		 * 				<extId>MBI1</extId>
		 * 				<groupHead>
		 * 					<memberships/>
		 * 					<residencies/>
		 * 					<extId>NBAS1I</extId>
		 * 				</groupHead>
		 * 				<groupName>Bassey Family</groupName>
		 * 			</socialGroup>
		 * 			<startDate>01-01-2015</startDate>
		 * 			<startType>IMG</startType>
		 * 			<bIsToA>1</bIsToA>
		 * 		</membership>
		 */
		mockMvc.perform(post("/memberships").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(MEMBERSHIP_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/membership").nodeCount(1))
				.andExpect(xpath("/membership/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/membership/individual").nodeCount(1))
				.andExpect(xpath("/membership/socialGroup/extId").string("MBI1"))
				.andExpect(xpath("/membership/socialGroup/groupHead/extId").string("NBAS1I"))
				.andExpect(xpath("/membership/startType").string("IMG"))
				.andExpect(xpath("/membership/bIsToA").string("1"));
	}
	
	
	@Test
	public void testPostInvalidMembership() throws Exception {
		final String MEMBERSHIP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<membership>"
					+ "<extId>ISE000012000</extId>"
					+ "<socialGroup>"
						+ "<extId>MBI1</extId>"
					+ "</socialGroup>"
					+ "<individual>"
						+ "<extId>NBAS1I2</extId>"
					+ "</individual>"						
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
					+ "<startDate>2015-01-01</startDate>"
					+ "<startType>IMG</startType>"
					+ "<endType>NA</endType>"
					+ "<bIsToA>0</bIsToA>"
				+ "</membership>";
		
		/*
		 * Expected return value:
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>Membership Individual id not valid</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/memberships").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(MEMBERSHIP_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("/failure/errors").nodeCount(1))
				.andExpect(xpath("failure/errors").string("Membership Individual id not valid"));	
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
