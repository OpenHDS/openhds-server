package org.openhds.specialstudy.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.specialstudy.domain.SocialGroupDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SocialGroupIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SocialGroupIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SocialGroupIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    @Autowired
    private SocialGroupDataOnDemand SocialGroupIntegrationTest.dod;
    
    @Test
    public void SocialGroupIntegrationTest.testCountSocialGroups() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        long count = org.openhds.specialstudy.domain.SocialGroup.countSocialGroups();
        org.junit.Assert.assertTrue("Counter for 'SocialGroup' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SocialGroupIntegrationTest.testFindSocialGroup() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        java.lang.Long id = dod.getRandomSocialGroup().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.SocialGroup obj = org.openhds.specialstudy.domain.SocialGroup.findSocialGroup(id);
        org.junit.Assert.assertNotNull("Find method for 'SocialGroup' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'SocialGroup' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SocialGroupIntegrationTest.testFindAllSocialGroups() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        long count = org.openhds.specialstudy.domain.SocialGroup.countSocialGroups();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'SocialGroup', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.openhds.specialstudy.domain.SocialGroup> result = org.openhds.specialstudy.domain.SocialGroup.findAllSocialGroups();
        org.junit.Assert.assertNotNull("Find all method for 'SocialGroup' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'SocialGroup' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SocialGroupIntegrationTest.testFindSocialGroupEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        long count = org.openhds.specialstudy.domain.SocialGroup.countSocialGroups();
        if (count > 20) count = 20;
        java.util.List<org.openhds.specialstudy.domain.SocialGroup> result = org.openhds.specialstudy.domain.SocialGroup.findSocialGroupEntries(0, (int)count);
        org.junit.Assert.assertNotNull("Find entries method for 'SocialGroup' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'SocialGroup' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    @Transactional
    public void SocialGroupIntegrationTest.testFlush() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        java.lang.Long id = dod.getRandomSocialGroup().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.SocialGroup obj = org.openhds.specialstudy.domain.SocialGroup.findSocialGroup(id);
        org.junit.Assert.assertNotNull("Find method for 'SocialGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySocialGroup(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'SocialGroup' failed to increment on flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void SocialGroupIntegrationTest.testMerge() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        java.lang.Long id = dod.getRandomSocialGroup().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.SocialGroup obj = org.openhds.specialstudy.domain.SocialGroup.findSocialGroup(id);
        org.junit.Assert.assertNotNull("Find method for 'SocialGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySocialGroup(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.merge();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'SocialGroup' failed to increment on merge and flush directive", obj.getVersion() > currentVersion || !modified);
    }
    
    @Test
    @Transactional
    public void SocialGroupIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        org.openhds.specialstudy.domain.SocialGroup obj = dod.getNewTransientSocialGroup(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'SocialGroup' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'SocialGroup' identifier to no longer be null", obj.getId());
    }
    
    @Test
    @Transactional
    public void SocialGroupIntegrationTest.testRemove() {
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to initialize correctly", dod.getRandomSocialGroup());
        java.lang.Long id = dod.getRandomSocialGroup().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SocialGroup' failed to provide an identifier", id);
        org.openhds.specialstudy.domain.SocialGroup obj = org.openhds.specialstudy.domain.SocialGroup.findSocialGroup(id);
        org.junit.Assert.assertNotNull("Find method for 'SocialGroup' illegally returned null for id '" + id + "'", obj);
        obj.remove();
        org.junit.Assert.assertNull("Failed to remove 'SocialGroup' with identifier '" + id + "'", org.openhds.specialstudy.domain.SocialGroup.findSocialGroup(id));
    }
    
}
