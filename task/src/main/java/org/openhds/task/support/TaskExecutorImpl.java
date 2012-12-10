package org.openhds.task.support;

import java.io.File;

import org.openhds.task.TaskContext;
import org.openhds.task.XmlWriterTask;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("openhdsTaskExecutor")
public class TaskExecutorImpl implements TaskExecutor {

    private FileResolver fileResolver;
    private AsyncTaskService asyncTaskService;

    private XmlWriterTask individualTaskWriter;
    private XmlWriterTask locationTaskWriter;
    private XmlWriterTask relationshipTaskWriter;
    private XmlWriterTask socialGroupTaskWriter;
    private XmlWriterTask visitTaskWriter;

    @Autowired
    public TaskExecutorImpl(AsyncTaskService asyncTaskService, FileResolver fileResolver) {
        this.asyncTaskService = asyncTaskService;
        this.fileResolver = fileResolver;
    }

    @Override
    public void executeIndividualXmlWriterTask() {
        if (asyncTaskService.taskShouldRun(AsyncTaskService.INDIVIDUAL_TASK_NAME)) {
            File individualXmlFile = fileResolver.resolveIndividualXmlFile();
            individualTaskWriter.writeXml(new TaskContext(individualXmlFile, SecurityContextHolder.getContext()));
        }
    }

    @Override
    public void executeLocationXmlWriterTask() {
        if (asyncTaskService.taskShouldRun(AsyncTaskService.LOCATION_TASK_NAME)) {
            File locationXmlFile = fileResolver.resolveLocationXmlFile();
            locationTaskWriter.writeXml(new TaskContext(locationXmlFile, SecurityContextHolder.getContext()));
        }
    }

    @Override
    public void executeRelationshipXmlWriterTask() {
        if (asyncTaskService.taskShouldRun(AsyncTaskService.RELATIONSHIP_TASK_NAME)) {
            File relationshipXmlFile = fileResolver.resolveRelationshipXmlFile();
            relationshipTaskWriter.writeXml(new TaskContext(relationshipXmlFile, SecurityContextHolder.getContext()));
        }
    }

    @Override
    public void executeSocialGroupXmlWriterTask() {
        if (asyncTaskService.taskShouldRun(AsyncTaskService.SOCIALGROUP_TASK_NAME)) {
            File socialGroupXmlFile = fileResolver.resolvesocialGroupXmlFile();
            socialGroupTaskWriter.writeXml(new TaskContext(socialGroupXmlFile, SecurityContextHolder.getContext()));
        }
    }

    @Override
    public void executeVisitWriterTask(int roundNumber) {
        if (asyncTaskService.taskShouldRun(AsyncTaskService.VISIT_TASK_NAME)) {
            File visitXmlFile = fileResolver.resolveVisitXmlFile();
            TaskContext taskContext = new TaskContext(visitXmlFile, SecurityContextHolder.getContext());
            taskContext.addExtraData("roundNumber", roundNumber + "");
            visitTaskWriter.writeXml(taskContext);
        }
    }

    @Autowired
    public void setIndividualTaskWriter(@Qualifier("individualXmlWriter") XmlWriterTask individualTaskWriter) {
        this.individualTaskWriter = individualTaskWriter;
    }

    @Autowired
    public void setLocationTaskWriter(@Qualifier("locationXmlWriter") XmlWriterTask individualTaskWriter) {
        this.locationTaskWriter = individualTaskWriter;
    }

    @Autowired
    public void setRelationshipTaskWriter(@Qualifier("relationshipXmlWriter") XmlWriterTask relationshipTaskWriter) {
        this.relationshipTaskWriter = relationshipTaskWriter;
    }

    @Autowired
    public void setSocialGroupTaskWriter(@Qualifier("socialGroupXmlWriter") XmlWriterTask socialGroupTaskWriter) {
        this.socialGroupTaskWriter = socialGroupTaskWriter;
    }

    @Autowired
    public void setVisitTaskWriter(@Qualifier("visitXmlWriter") XmlWriterTask visitTaskWriter) {
        this.visitTaskWriter = visitTaskWriter;
    }

}
