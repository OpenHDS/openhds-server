package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.runner.RunWith;
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


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader=WebContextLoader.class, locations={"/testContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
public class SocialGroupResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockHttpSession session;

	private MockMvc mockMvc;
	 
	 @Autowired
	private VisitCrudImpl visitCrud; 

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();

		session = getMockHttpSession("admin", "test");
	}

	@Test
	public void testGetSocialGroups() throws Exception {	
		mockMvc.perform(get("/socialgroups").session(session).accept(MediaType.APPLICATION_XML))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/socialGroups").nodeCount(1))
		.andExpect(xpath("/socialGroups/socialgroup").nodeCount(1))
		.andExpect(xpath("/socialGroups/socialgroup/extId").string("MBI1"))
		.andExpect(xpath("/socialGroups/socialgroup/groupHead/extId").string("NBAS1I"))
		.andExpect(xpath("/socialGroups/socialgroup/groupName").string("Bassey Family"));	
		
		/* Expected response:
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 		<socialGroups>
		 * 			<socialgroup>
		 * 				<extId>MBI1</extId>
		 * 				<groupHead>
		 * 					<memberships/>
		 * 					<residencies/>
		 * 					<extId>NBAS1I</extId>
		 * 				</groupHead>
		 * 				<groupName>Bassey Family</groupName>
		 * 		</socialgroup>
		 * </socialGroups>
		 * 
		 * */
	}

	@Test
	public void testPostRound() throws Exception {
		final String SOCIALGROUP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<socialgroup>"
					+ "<extId>ISE0</extId>"
					+ "<groupName>NEW SOCIAL GROUP</groupName>"
					+ "<groupHead>"
						+ "<extId>BHAR1K</extId>"
					+"</groupHead>"
					+ "<groupType>FAM</groupType>"
					+ "<memberships />"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
				+ "</socialgroup>";

		/* Expected result:
			<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
				<socialgroup>
					<extId>ISE0</extId>
					<groupHead>
						<memberships/>
						<residencies/>
						<extId>BHAR1K</extId>
					</groupHead>
					<groupName>NEW SOCIAL GROUP</groupName>
				</socialgroup>
		 */
		
		mockMvc.perform(post("/socialgroups").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(SOCIALGROUP_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/socialgroup").nodeCount(1))
				.andExpect(xpath("/socialgroup/extId").string("ISE0"))
				.andExpect(xpath("/socialgroup/groupHead/extId").string("BHAR1K"))
				.andExpect(xpath("/socialgroup/groupName").string("NEW SOCIAL GROUP"));
	}
	
	
	@Test
	public void testPostInvalidSocialGroup() throws Exception {
		final String SOCIALGROUP_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<socialgroup>"
					+ "<extId>ISE00000400</extId>"
					+ "<groupName>NEW SOCIAL GROUP</groupName>"
					+ "<groupHead>"
						+ "<extId>BHAR1K</extId>"
					+"</groupHead>"
					+ "<groupType>FAM</groupType>"
					+ "<memberships />"
					+ "<collectedBy>"
						+ "<extId>FWEK1D</extId>"
					+ "</collectedBy>"
				+ "</socialgroup>";
		
		/*
		 * Expected return value:
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>ISE00000400 is not of the required length as specified in the IdScheme. It must be 4 characters long.</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/socialgroups").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(SOCIALGROUP_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("failure/errors").string(StringContains.containsString("is not of the required length as specified in the IdScheme.")));
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
