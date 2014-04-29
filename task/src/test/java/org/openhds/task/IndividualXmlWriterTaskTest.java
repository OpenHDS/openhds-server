package org.openhds.task;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
//import java.util.Arrays;
import java.util.Calendar;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhds.controller.service.IndividualService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContext;

public class IndividualXmlWriterTaskTest extends AbstractXmlWriterTest {

    @Mock
    private AsyncTaskService asyncTaskService;

    @Mock
    private IndividualService individualService;

    @Mock
    private CalendarUtil calendarUtil;

    @Mock
    SecurityContext securityContext;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldWriteXml() {
        File individualFile = new File("individual.xml");
        try {
            IndividualXmlWriterTask task = new IndividualXmlWriterTask(asyncTaskService, individualService);
            when(individualService.getTotalIndividualCount()).thenReturn(1L);
           // when(individualService.getAllIndividualsInRange(0, 100)).thenReturn(Arrays.asList(createIndividual()));
            when(calendarUtil.formatDate(any(Calendar.class))).thenReturn("02-09-1987");

            task.writeXml(new TaskContext(individualFile, securityContext));

            @SuppressWarnings("unused")
			ClassPathResource expected = new ClassPathResource("xml/individuals.xml");

          //  compareXmlDocuments(expected.getFile(), individualFile);
        } catch (Exception e) {
            fail();
        } finally {
            if (individualFile.exists()) {
                individualFile.delete();
            }
        }
    }



	@SuppressWarnings("unused")
	private Individual createIndividual() {
        Individual individual = new Individual();
        Calendar dob = Calendar.getInstance();
        dob.set(Calendar.MONTH, Calendar.SEPTEMBER);
        dob.set(Calendar.DATE, 2);
        dob.set(Calendar.YEAR, 1987);
        individual.setDob(dob);

        individual.setExtId("MBI01001");
        individual.setFather(individualWithExtId("UNK"));
        individual.setFirstName("Brian");
        individual.setGender("M");
        individual.setLastName("Harold");
        individual.setMother(individualWithExtId("UNK"));

        return individual;
    }

    private Individual individualWithExtId(String string) {
        Individual indiv = new Individual();
        indiv.setExtId(string);
        return indiv;
    }
}
