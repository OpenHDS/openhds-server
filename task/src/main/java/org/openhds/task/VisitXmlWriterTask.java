package org.openhds.task;

import java.util.List;

import org.openhds.controller.service.VisitService;
import org.openhds.domain.model.Visit;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.domain.util.ShallowCopier;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("visitXmlWriter")
public class VisitXmlWriterTask extends XmlWriterTemplate<Visit> {

    private VisitService visitService;

    @Autowired
    public VisitXmlWriterTask(AsyncTaskService asyncTaskService, CalendarUtil calendarUtil, VisitService visitService) {
        super(asyncTaskService, AsyncTaskService.VISIT_TASK_NAME);
        this.visitService = visitService;
    }

    @Override
    protected Visit makeCopyOf(Visit original) {
        return ShallowCopier.copyVisit(original);
    }

    @Override
    protected List<Visit> getEntitiesInRange(TaskContext taskContext, int i, int pageSize) {
        int round = getRoundNumber(taskContext) -1;
        return visitService.getAllVisitsForRoundInRange(round, i, pageSize);
    }

    protected int getRoundNumber(TaskContext taskContext) {
        return Integer.parseInt(taskContext.getExtraData("roundNumber"));
    }

    @Override
    protected Class<?> getBoundClass() {
        return Visit.class;
    }

    @Override
    protected String getStartElementName() {
        return "visits";
    }

    @Override
    protected int getTotalEntityCount(TaskContext taskContext) {
    	int previousround=getRoundNumber(taskContext) -1;
        return (int) visitService.getTotalVisitCountForRound(previousround);
    }

}
