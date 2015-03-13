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


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader=WebContextLoader.class, locations={"/testContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
public class LocationHierarchyLevelResourceTest {

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
	public void testGetAllLocationHierarchyLevels() throws Exception {
		
		/* Expected Result: 
		 * 
		 * 	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * 	<locationHierarchyLevels>
		 * 		<hierarchyLevel><keyIdentifier>1</keyIdentifier><name>LGA</name><uuid>HierarchyLevel1</uuid></hierarchyLevel>
		 * 		<hierarchyLevel><keyIdentifier>2</keyIdentifier><name>Ward</name><uuid>HierarchyLevel2</uuid></hierarchyLevel>
		 * 		<hierarchyLevel><keyIdentifier>3</keyIdentifier><name>Village</name><uuid>HierarchyLevel3</uuid></hierarchyLevel>
		 * 	</locationHierarchyLevels>
		 * */
		mockMvc.perform(get("/locationhierarchylevels").session(session))
			.andExpect(status().isOk())
			.andExpect(content().mimeType(MediaType.APPLICATION_XML))
			.andExpect(xpath("/locationHierarchyLevels").nodeCount(1))
			.andExpect(xpath("/locationHierarchyLevels/hierarchyLevel").nodeCount(3))
			.andExpect(xpath("/locationHierarchyLevels/hierarchyLevel/name[contains(text(),'LGA')]").exists())
			.andExpect(xpath("/locationHierarchyLevels/hierarchyLevel/name[contains(text(),'Ward')]").exists())
			.andExpect(xpath("/locationHierarchyLevels/hierarchyLevel/name[contains(text(),'Village')]").exists());
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
