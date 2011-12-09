package org.openhds.specialstudy.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.specialstudy.domain.IndividualDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect IndividualIntegrationTest_Roo_IntegrationTest {
    
    declare @type: IndividualIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: IndividualIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    @Autowired
    private IndividualDataOnDemand IndividualIntegrationTest.dod;
    
    @Test
    public void IndividualIntegrationTest.testCountIndividuals() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        long count = org.openhds.specialstudy.domain.Individual.countIndividuals();
        org.junit.Assert.assertTrue("Counter for 'Individual' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void IndividualIntegrationTest.testFindIndividual() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        java.lang.Long id = dod.getRandomIndividual().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Individual obj = org.openhds.specialstudy.domain.Individual.findIndividual(id);
        org.junit.Assert.assertNotNull("Find method for 'Individual' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Individual' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void IndividualIntegrationTest.testFindAllIndividuals() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        long count = org.openhds.specialstudy.domain.Individual.countIndividuals();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Individual', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.openhds.specialstudy.domain.Individual> result = org.openhds.specialstudy.domain.Individual.findAllIndividuals();
        org.junit.Assert.assertNotNull("Find all method for 'Individual' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Individual' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void IndividualIntegrationTest.testFindIndividualEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        long count = org.openhds.specialstudy.domain.Individual.countIndividuals();
        if (count > 20) count = 20;
        java.util.List<org.openhds.specialstudy.domain.Individual> result = org.openhds.specialstudy.domain.Individual.findIndividualEntries(0, (int)count);
        org.junit.Assert.assertNotNull("Find entries method for 'Individual' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Individual' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    @Transactional
    public void IndividualIntegrationTest.testFlush() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        java.lang.Long id = dod.getRandomIndividual().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Individual obj = org.openhds.specialstudy.domain.Individual.findIndividual(id);
        org.junit.Assert.assertNotNull("Find method for 'Individual' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyIndividual(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Individual' failed to increment on flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void IndividualIntegrationTest.testMerge() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        java.lang.Long id = dod.getRandomIndividual().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Individual obj = org.openhds.specialstudy.domain.Individual.findIndividual(id);
        org.junit.Assert.assertNotNull("Find method for 'Individual' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyIndividual(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.merge();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Individual' failed to increment on merge and flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void IndividualIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        org.openhds.specialstudy.domain.Individual obj = dod.getNewTransientIndividual(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Individual' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Individual' identifier to no longer be null", obj.getId());
    }
    
    @Test
    @Transactional
    public void IndividualIntegrationTest.testRemove() {
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to initialize correctly", dod.getRandomIndividual());
        java.lang.Long id = dod.getRandomIndividual().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Individual' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.Individual obj = org.openhds.specialstudy.domain.Individual.findIndividual(id);
        org.junit.Assert.assertNotNull("Find method for 'Individual' illegally returned null for id '" + id + "'", obj);
        obj.remove();
        org.junit.Assert.assertNull("Failed to remove 'Individual' with identifier '" + id + "'", org.openhds.specialstudy.domain.Individual.findIndividual(id));
    }
    
}
