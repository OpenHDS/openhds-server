package org.openhds.task.support;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhds.task.TaskContext;
import org.openhds.task.XmlWriterTask;
import org.openhds.task.service.AsyncTaskService;

public class TaskExecutorImplTest {

    @Mock
    FileResolver fileResolver;

    @Mock
    AsyncTaskService asyncTaskService;

    @Mock
    private XmlWriterTask writeTask;

    private TaskExecutorImpl executor;

    @Before
    public void setUp() {
        initMocks(this);
        executor = new TaskExecutorImpl(asyncTaskService, fileResolver);
    }

    @Test
    public void shouldRunIndividualXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.INDIVIDUAL_TASK_NAME)).thenReturn(true);

        executor.setIndividualTaskWriter(writeTask);
        executor.executeIndividualXmlWriterTask();

        verify(fileResolver).resolveIndividualXmlFile();
        verify(writeTask).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldNotRunIndividualXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.INDIVIDUAL_TASK_NAME)).thenReturn(false);

        executor.setIndividualTaskWriter(writeTask);
        executor.executeIndividualXmlWriterTask();

        verify(writeTask, times(0)).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldRunLocationXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.LOCATION_TASK_NAME)).thenReturn(true);

        executor.setLocationTaskWriter(writeTask);
        executor.executeLocationXmlWriterTask();

        verify(fileResolver).resolveLocationXmlFile();
        verify(writeTask).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldNotRunLocationXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.LOCATION_TASK_NAME)).thenReturn(false);

        executor.setLocationTaskWriter(writeTask);
        executor.executeLocationXmlWriterTask();

        verify(writeTask, times(0)).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldRunSocialGroupXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.SOCIALGROUP_TASK_NAME)).thenReturn(true);

        executor.setSocialGroupTaskWriter(writeTask);
        executor.executeSocialGroupXmlWriterTask();

        verify(fileResolver).resolvesocialGroupXmlFile();
        verify(writeTask).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldNotRunSocialGroupXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.SOCIALGROUP_TASK_NAME)).thenReturn(false);

        executor.setSocialGroupTaskWriter(writeTask);
        executor.executeSocialGroupXmlWriterTask();

        verify(writeTask, times(0)).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldRunRelationshipXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.RELATIONSHIP_TASK_NAME)).thenReturn(true);

        executor.setRelationshipTaskWriter(writeTask);
        executor.executeRelationshipXmlWriterTask();

        verify(fileResolver).resolveRelationshipXmlFile();
        verify(writeTask).writeXml(any(TaskContext.class));
    }

    @Test
    public void shouldNotRunRelationshipXmlTask() {
        when(asyncTaskService.taskShouldRun(AsyncTaskService.RELATIONSHIP_TASK_NAME)).thenReturn(false);

        executor.setRelationshipTaskWriter(writeTask);
        executor.executeRelationshipXmlWriterTask();

        verify(writeTask, times(0)).writeXml(any(TaskContext.class));
    }
}
