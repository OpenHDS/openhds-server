package org.openhds.task.support;

import java.io.File;

import org.openhds.task.TaskContext;
import org.openhds.task.XmlWriterTask;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("openhdsTaskExecutor")
public class TaskExecutorImpl implements TaskExecutor {

    private FileResolver fileResolver;
    private AsyncTaskService asyncTaskService;

    private XmlWriterTask individualTaskWriter;
    private XmlWriterTask locationTaskWriter;
    private XmlWriterTask relationshipTaskWriter;
    private XmlWriterTask socialGroupTaskWriter;
    private XmlWriterTask visitTaskWriter;
    private XmlWriterTask formTaskWriter;

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

	@Override
	public void executeFormXmlWriterTask() {
		 if (asyncTaskService.taskShouldRun(AsyncTaskService.FORM_TASK_NAME)) {
	            File formXmlFile = fileResolver.resolveFormXmlFile();
	            formTaskWriter.writeXml(new TaskContext(formXmlFile, SecurityContextHolder.getContext()));
	        }		
	}
    
    @Resource(name="individualXmlWriter")
    public void setIndividualTaskWriter(XmlWriterTask individualTaskWriter) {
        this.individualTaskWriter = individualTaskWriter;
    }

    @Resource(name="locationXmlWriter")
    public void setLocationTaskWriter(XmlWriterTask individualTaskWriter) {
        this.locationTaskWriter = individualTaskWriter;
    }

    @Resource(name="relationshipXmlWriter")
    public void setRelationshipTaskWriter(XmlWriterTask relationshipTaskWriter) {
        this.relationshipTaskWriter = relationshipTaskWriter;
    }

    @Resource(name="socialGroupXmlWriter")
    public void setSocialGroupTaskWriter(XmlWriterTask socialGroupTaskWriter) {
        this.socialGroupTaskWriter = socialGroupTaskWriter;
    }

    @Resource(name="visitXmlWriter")
    public void setVisitTaskWriter(XmlWriterTask visitTaskWriter) {
        this.visitTaskWriter = visitTaskWriter;
    }
    
    @Resource(name="formXmlWriter")
    public void setFormTaskWriter(XmlWriterTask formTaskWriter) {
        this.formTaskWriter = formTaskWriter;
    }



}
