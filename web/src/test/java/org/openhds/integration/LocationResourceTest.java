package org.openhds.integration;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.xpath;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Location;
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
@DatabaseSetup(value = "/locationResourceDb.xml", type = DatabaseOperation.REFRESH)
public class LocationResourceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private GenericDao genericDao;

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
	public void testGetAllLocations() throws Exception {
		mockMvc.perform(get("/locations").session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/locations").nodeCount(1))
		.andExpect(xpath("/locations/location/collectedBy/extId").string("FWEK1D"))
		.andExpect(xpath("/locations/location/accuracy").string(""))
		.andExpect(xpath("/locations/location/altitude").string(""))
		.andExpect(xpath("/locations/location/extId").string("NJA001"))
		.andExpect(xpath("/locations/location/latitude").string(""))
		.andExpect(xpath("/locations/location/locationLevel/extId").string("HIERARCHY_ROOT"))
		.andExpect(xpath("/locations/location/locationName").string("House 3"))
		.andExpect(xpath("/locations/location/locationType").string("RUR"))
		.andExpect(xpath("/locations/location/longitude").string(""));
	}

	@Test
	public void testGetLocationByExtId() throws Exception {
		String locationExtId = "NJA001";

		mockMvc.perform(get("/locations/{extId}", locationExtId).session(session))
		.andExpect(status().isOk())
		.andExpect(content().mimeType(MediaType.APPLICATION_XML))
		.andExpect(xpath("/location/collectedBy/extId").string("FWEK1D"))
		.andExpect(xpath("/location/accuracy").string(""))
		.andExpect(xpath("/location/altitude").string(""))
		.andExpect(xpath("/location/extId").string("NJA001"))
		.andExpect(xpath("/location/latitude").string(""))
		.andExpect(xpath("/location/locationLevel/extId").string("HIERARCHY_ROOT"))
		.andExpect(xpath("/location/locationName").string("House 3"))
		.andExpect(xpath("/location/locationType").string("RUR"))
		.andExpect(xpath("/location/longitude").string(""));
	}

	@Test
	public void testPostLocation() throws Exception {
		final String LOCATION_POST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<location>"
				+ "<collectedBy>"
				+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"
				+ "<accuracy></accuracy>"
				+ "<altitude></altitude>"
				+ "<extId>MBA00000001</extId>"
				+ "<latitude></latitude>"
				+ "<locationLevel>"
				+ "<extId>IFB</extId>"
				+ "</locationLevel>"
				+ "<locationName>Test House</locationName>"
				+ "<locationType>RUR</locationType>"
				+ "<longitude></longitude>"
				+ "</location>";

		mockMvc.perform(post("/locations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(LOCATION_POST_XML.getBytes())
				).andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/location/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/location/accuracy").string(""))
				.andExpect(xpath("/location/altitude").string(""))
				.andExpect(xpath("/location/extId").string("MBA00000001"))
				.andExpect(xpath("/location/latitude").string(""))
				.andExpect(xpath("/location/locationLevel/extId").string("IFB"))
				.andExpect(xpath("/location/locationName").string("Test House"))
				.andExpect(xpath("/location/locationType").string("RUR"))
				.andExpect(xpath("/location/longitude").string(""));
	}
	
	@Test
	public void testDeleteLocationDoesNotExist() throws Exception {
		String locationExtId = "DOESNOTEXIST";

		mockMvc.perform(delete("/locations/{extId}", locationExtId).session(session))
		.andExpect(status().isGone());
	}
	
	@Test
	public void testAddDeleteRetrieveLocation() throws Exception {
		String locationExtId = "testLocation";
		
		testPostLocation();
	    Location savedLocation = genericDao.findByProperty(Location.class, "extId", locationExtId);
	    Assert.assertNotNull(savedLocation);
		
		mockMvc.perform(delete("/locations/{extId}", locationExtId).session(session))
		.andExpect(status().isOk());
		
	    savedLocation = genericDao.findByProperty(Location.class, "extId", locationExtId);
	    Assert.assertNull(savedLocation);
	}
	
	@Test
	public void testUpdateLocation() throws Exception {
		final String LOCATION_PUT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<location>"
				+ "<collectedBy>"
				+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"
				+ "<accuracy></accuracy>"
				+ "<altitude></altitude>"
				+ "<extId>testLocation</extId>"
				+ "<latitude></latitude>"
				+ "<locationLevel>"
				+ "<extId>IFB</extId>"
				+ "</locationLevel>"
				+ "<locationName>Test House</locationName>"
				+ "<locationType>RUR</locationType>"
				+ "<longitude></longitude>"
				+ "</location>";

		mockMvc.perform(put("/locations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(LOCATION_PUT_XML.getBytes())
				).andExpect(status().isOk())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/location/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/location/accuracy").string(""))
				.andExpect(xpath("/location/altitude").string(""))
				.andExpect(xpath("/location/extId").string("testLocation"))
				.andExpect(xpath("/location/latitude").string(""))
				.andExpect(xpath("/location/locationLevel/extId").string("IFB"))
				.andExpect(xpath("/location/locationName").string("Test House"))
				.andExpect(xpath("/location/locationType").string("RUR"))
				.andExpect(xpath("/location/longitude").string(""));
		
		Location location = genericDao.findByProperty(Location.class, "extId", "testLocation");
		
		assertTrue(location.getLocationName().equals("Test House"));
	}
	
	@Test
	public void testUpdateNonExistingLocation() throws Exception {
		final String LOCATION_PUT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<location>"
				+ "<collectedBy>"
				+ "<extId>FWEK1D</extId>"
				+ "</collectedBy>"
				+ "<accuracy></accuracy>"
				+ "<altitude></altitude>"
				+ "<extId>testLocation3</extId>"
				+ "<latitude></latitude>"
				+ "<locationLevel>"
				+ "<extId>IFB</extId>"
				+ "</locationLevel>"
				+ "<locationName>Test House</locationName>"
				+ "<locationType>RUR</locationType>"
				+ "<longitude></longitude>"
				+ "</location>";

		mockMvc.perform(put("/locations").session(session)
				.contentType(MediaType.APPLICATION_XML)
				.body(LOCATION_PUT_XML.getBytes())
				).andExpect(status().isCreated())
				.andExpect(content().mimeType(MediaType.APPLICATION_XML))
				.andExpect(xpath("/location/collectedBy/extId").string("FWEK1D"))
				.andExpect(xpath("/location/accuracy").string(""))
				.andExpect(xpath("/location/altitude").string(""))
				.andExpect(xpath("/location/extId").string("testLocation3"))
				.andExpect(xpath("/location/latitude").string(""))
				.andExpect(xpath("/location/locationLevel/extId").string("IFB"))
				.andExpect(xpath("/location/locationName").string("Test House"))
				.andExpect(xpath("/location/locationType").string("RUR"))
				.andExpect(xpath("/location/longitude").string(""));
		
		Location location = genericDao.findByProperty(Location.class, "extId", "testLocation3");
		
		assertTrue(location.getLocationName().equals("Test House"));
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
