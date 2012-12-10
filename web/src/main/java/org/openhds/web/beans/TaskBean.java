package org.openhds.web.beans;

import java.util.List;

import org.openhds.domain.model.AsyncTask;
import org.openhds.task.service.AsyncTaskService;
import org.openhds.task.support.TaskExecutor;

public class TaskBean {

    private static final String TASK_VIEW = "task";
    private TaskExecutor taskExecutor;
    private AsyncTaskService asyncTaskService;
    
    private Integer roundNumber;

    public TaskBean(TaskExecutor taskExecutor, AsyncTaskService asyncTaskService) {
        this.taskExecutor = taskExecutor;
        this.asyncTaskService = asyncTaskService;
    }

    public String startIndividualTask() {
        taskExecutor.executeIndividualXmlWriterTask();
        return TASK_VIEW;
    }

    public String startLocationTask() {
        taskExecutor.executeLocationXmlWriterTask();
        return TASK_VIEW;
    }

    public String startRelationshipTask() {
        taskExecutor.executeRelationshipXmlWriterTask();
        return TASK_VIEW;
    }

    public String startSocialGroupTask() {
        taskExecutor.executeSocialGroupXmlWriterTask();
        return TASK_VIEW;
    }
    
    public String startVisitTask() {
        taskExecutor.executeVisitWriterTask(roundNumber);
        return TASK_VIEW;
    }

    public List<AsyncTask> getTasks() {
        return asyncTaskService.findAllAsyncTask();
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

}
