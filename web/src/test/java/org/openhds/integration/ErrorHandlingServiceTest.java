package org.openhds.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhds.errorhandling.domain.ErrorLog;
import org.openhds.errorhandling.service.ErrorHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("/testContext.xml")
public class ErrorHandlingServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    private ErrorHandlingService errorService;
    
    @Test
    public void testDatabaseEndPoint() {
        /**
         * Placeholder
         */
    }
}