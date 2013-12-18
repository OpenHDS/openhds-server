package org.openhds.task.service;

import java.util.List;

import org.openhds.domain.model.AsyncTask;

public interface AsyncTaskService {

    public static final String INDIVIDUAL_TASK_NAME = "Individual Task";
    public static final String LOCATION_TASK_NAME = "Location Task";
    public static final String RELATIONSHIP_TASK_NAME = "Relationship Task";
    public static final String SOCIALGROUP_TASK_NAME = "Social Group Task";
    public static final String VISIT_TASK_NAME = "Visit Task";
    public static final String FORM_TASK_NAME = "Form Task";

    boolean taskShouldRun(String taskName);

    void beginNewTaskSession();

    void startTask(String taskName);

    void clearSession();

    void updateTaskProgress(String taskName, long itemsWritten);

    void finishTask(String taskName, long itemsWritten, String md5);

    void closeTaskSession();
    
    List<AsyncTask> findAllAsyncTask();
}
