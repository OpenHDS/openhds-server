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
@DatabaseSetup(value = "/locationHierarchyResourceDb.xml", type = DatabaseOperation.REFRESH)
public class LocationHierarchyResourceTest {

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
	public void testGetAllLocationHierarchies() throws Exception {
		
		/* Expected Result: 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<locationHierarchies>
		 * 		<hierarchy>
		 * 			<extId>TAN</extId><level><keyIdentifier>0</keyIdentifier><name>LGA</name></level><name>TAN</name><parent><extId>HIERARCHY_ROOT</extId></parent>
		 * 		</hierarchy>
		 * 		<hierarchy>
		 * 			<extId>IFA</extId><level><keyIdentifier>0</keyIdentifier><name>Ward</name></level><name>IFA</name><parent><extId>TAN</extId></parent>
		 * 		</hierarchy>
		 * 		<hierarchy>
		 * 			<extId>IFB</extId><level><keyIdentifier>0</keyIdentifier><name>Village</name></level><name>IFB</name><parent><extId>IFA</extId></parent>
		 * 		</hierarchy>
		 * 	</locationHierarchies>
		 * */
		mockMvc.perform(get("/locationhierarchies").session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/locationHierarchies").nodeCount(1))
		.andExpect(xpath("/locationHierarchies/hierarchy").nodeCount(3))
		.andExpect(xpath("/locationHierarchies/hierarchy/extId[contains(text(),'TAN')]").exists())
		.andExpect(xpath("/locationHierarchies/hierarchy/extId[contains(text(),'IFA')]").exists())
		.andExpect(xpath("/locationHierarchies/hierarchy/extId[contains(text(),'IFB')]").exists());
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
