package org.openhds.specialstudy.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.specialstudy.domain.VisitDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect VisitIntegrationTest_Roo_IntegrationTest {
    
    declare @type: VisitIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: VisitIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    @Autowired
    private VisitDataOnDemand VisitIntegrationTest.dod;
    
    @Test
    public void VisitIntegrationTest.testCountVisits() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = org.openhds.specialstudy.domain.Visit.countVisits();
        org.junit.Assert.assertTrue("Counter for 'Visit' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void VisitIntegrationTest.testFindVisit() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        java.lang.Long id = dod.getRandomVisit().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Visit obj = org.openhds.specialstudy.domain.Visit.findVisit(id);
        org.junit.Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Visit' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void VisitIntegrationTest.testFindAllVisits() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = org.openhds.specialstudy.domain.Visit.countVisits();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Visit', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.openhds.specialstudy.domain.Visit> result = org.openhds.specialstudy.domain.Visit.findAllVisits();
        org.junit.Assert.assertNotNull("Find all method for 'Visit' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Visit' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void VisitIntegrationTest.testFindVisitEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = org.openhds.specialstudy.domain.Visit.countVisits();
        if (count > 20) count = 20;
        java.util.List<org.openhds.specialstudy.domain.Visit> result = org.openhds.specialstudy.domain.Visit.findVisitEntries(0, (int)count);
        org.junit.Assert.assertNotNull("Find entries method for 'Visit' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Visit' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    @Transactional
    public void VisitIntegrationTest.testFlush() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        java.lang.Long id = dod.getRandomVisit().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Visit obj = org.openhds.specialstudy.domain.Visit.findVisit(id);
        org.junit.Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyVisit(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Visit' failed to increment on flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void VisitIntegrationTest.testMerge() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        java.lang.Long id = dod.getRandomVisit().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Visit obj = org.openhds.specialstudy.domain.Visit.findVisit(id);
        org.junit.Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyVisit(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.merge();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Visit' failed to increment on merge and flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void VisitIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        org.openhds.specialstudy.domain.Visit obj = dod.getNewTransientVisit(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Visit' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Visit' identifier to no longer be null", obj.getId());
    }
    
    @Test
    @Transactional
    public void VisitIntegrationTest.testRemove() {
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        java.lang.Long id = dod.getRandomVisit().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Visit obj = org.openhds.specialstudy.domain.Visit.findVisit(id);
        org.junit.Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        obj.remove();
        org.junit.Assert.assertNull("Failed to remove 'Visit' with identifier '" + id + "'", org.openhds.specialstudy.domain.Visit.findVisit(id));
    }
    
}
