package org.openhds.task;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.model.Location;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContext;

public class LocationXmlWriterTaskTest extends AbstractXmlWriterTest {
    @Mock
    private AsyncTaskService asyncTaskService;

    @Mock
    private LocationHierarchyService locationService;

    @Mock
    private CalendarUtil calendarUtil;

    @Mock
    SecurityContext securityContext;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    @Ignore
    public void shouldWriteXml() {
        File locationFile = new File("location.xml");
        try {
            LocationXmlWriterTask task = new LocationXmlWriterTask(asyncTaskService, calendarUtil, locationService);
            when(locationService.getTotalLocationCount()).thenReturn(1L);
            when(locationService.getAllLocationsInRange(0, 100)).thenReturn(Arrays.asList(createLocation()));
            when(calendarUtil.formatDate(any(Calendar.class))).thenReturn("02-09-1987");

            task.writeXml(new TaskContext(locationFile, securityContext));

            ClassPathResource expected = new ClassPathResource("xml/locations.xml");

            compareXmlDocuments(expected.getFile(), locationFile);
        } catch (Exception e) {
            fail();
        } finally {
            if (locationFile.exists()) {
                locationFile.delete();
            }
        }
    }

    private Location createLocation() {
        Location location = new Location();
        location.setExtId("MBI01");

        return location;
    }

}
