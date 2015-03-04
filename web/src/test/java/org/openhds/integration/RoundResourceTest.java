package org.openhds.integration;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
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
public class RoundResourceTest {

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
	public void testGetLatestRound() throws Exception {	
		mockMvc.perform(get("/rounds").session(session).accept(MediaType.APPLICATION_XML))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/rounds/round").nodeCount(1))
		.andExpect(xpath("/rounds/round/uuid").string("ROUND 1"))
		.andExpect(xpath("/rounds/round/remarks").string(""))
		.andExpect(xpath("/rounds/round/roundNumber").string("1"));	
	}

	@Test
	public void testPostRound() throws Exception {
		final String ROUND_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<round>"
					+ "<roundNumber>0</roundNumber>"
					+ "<startDate>2010-06-30</startDate>"
					+ "<endDate>2010-07-31</endDate>"
					+ "<roundNumber>0</roundNumber>"
					+ "<remarks>NA</remarks>"
				+ "</round>";

		/* Expected result:
			<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			<round>
				<endDate>31-07-2010</endDate>
				<remarks>NA</remarks>
				<roundNumber>0</roundNumber>
				<startDate>30-06-2010</startDate>
			</round>
		 */
		mockMvc.perform(post("/rounds").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(ROUND_POST_XML.getBytes()))
				.andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/round").nodeCount(1))
				.andExpect(xpath("/round/endDate").string("31-07-2010"))
				.andExpect(xpath("/round/roundNumber").string("0"));
	}
	
	
	@Test
	public void testPostDuplicateRound() throws Exception {
		final String ROUND_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<round>"
					+ "<roundNumber>0</roundNumber>"
					+ "<startDate>2010-06-30</startDate>"
					+ "<endDate>2010-07-31</endDate>"
					+ "<roundNumber>1</roundNumber>"
					+ "<remarks>NA</remarks>"
				+ "</round>";
		
		/*
		 * Expected return value:
		 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<failure>
		 * 		<errors>A round already exists with that round number. Please enter in a unique round number.</errors>
		 * 	</failure>
		 * */
		
		mockMvc.perform(post("/rounds").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(ROUND_POST_XML.getBytes()))
				.andExpect(status().isBadRequest())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/failure").nodeCount(1))
				.andExpect(xpath("failure/errors").string("A round already exists with that round number. Please enter in a unique round number."));
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
