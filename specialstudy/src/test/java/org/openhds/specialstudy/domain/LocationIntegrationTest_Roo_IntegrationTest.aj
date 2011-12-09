package org.openhds.specialstudy.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.specialstudy.domain.LocationDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LocationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: LocationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: LocationIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    @Autowired
    private LocationDataOnDemand LocationIntegrationTest.dod;
    
    @Test
    public void LocationIntegrationTest.testCountLocations() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        long count = org.openhds.specialstudy.domain.Location.countLocations();
        org.junit.Assert.assertTrue("Counter for 'Location' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void LocationIntegrationTest.testFindLocation() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        java.lang.Long id = dod.getRandomLocation().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Location obj = org.openhds.specialstudy.domain.Location.findLocation(id);
        org.junit.Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Location' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void LocationIntegrationTest.testFindAllLocations() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        long count = org.openhds.specialstudy.domain.Location.countLocations();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Location', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.openhds.specialstudy.domain.Location> result = org.openhds.specialstudy.domain.Location.findAllLocations();
        org.junit.Assert.assertNotNull("Find all method for 'Location' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Location' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void LocationIntegrationTest.testFindLocationEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        long count = org.openhds.specialstudy.domain.Location.countLocations();
        if (count > 20) count = 20;
        java.util.List<org.openhds.specialstudy.domain.Location> result = org.openhds.specialstudy.domain.Location.findLocationEntries(0, (int)count);
        org.junit.Assert.assertNotNull("Find entries method for 'Location' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Location' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    @Transactional
    public void LocationIntegrationTest.testFlush() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        java.lang.Long id = dod.getRandomLocation().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Location obj = org.openhds.specialstudy.domain.Location.findLocation(id);
        org.junit.Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLocation(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Location' failed to increment on flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void LocationIntegrationTest.testMerge() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        java.lang.Long id = dod.getRandomLocation().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Location obj = org.openhds.specialstudy.domain.Location.findLocation(id);
        org.junit.Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLocation(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.merge();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Location' failed to increment on merge and flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void LocationIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        org.openhds.specialstudy.domain.Location obj = dod.getNewTransientLocation(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Location' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Location' identifier to no longer be null", obj.getId());
    }
    
    @Test
    @Transactional
    public void LocationIntegrationTest.testRemove() {
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
        java.lang.Long id = dod.getRandomLocation().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Location obj = org.openhds.specialstudy.domain.Location.findLocation(id);
        org.junit.Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
        obj.remove();
        org.junit.Assert.assertNull("Failed to remove 'Location' with identifier '" + id + "'", org.openhds.specialstudy.domain.Location.findLocation(id));
    }
    
}
