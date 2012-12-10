package org.openhds.task;

import java.util.List;

import org.openhds.controller.service.IndividualService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.domain.util.ShallowCopier;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("individualXmlWriter")
public class IndividualXmlWriterTask extends XmlWriterTemplate<Individual> {
    private IndividualService individualService;

    @Autowired
    public IndividualXmlWriterTask(AsyncTaskService asyncTaskService, IndividualService individualService,
            CalendarUtil calendarUtil) {
        super(asyncTaskService, calendarUtil, AsyncTaskService.INDIVIDUAL_TASK_NAME);
        this.individualService = individualService;
    }

    @Override
    protected Individual makeCopyOf(Individual original) {
        return ShallowCopier.shallowCopyIndividual(original);
    }

    @Override
    protected List<Individual> getEntitiesInRange(TaskContext taskContext, int start, int pageSize) {
        return individualService.getAllIndividualsInRange(start, pageSize);
    }

    @Override
    protected Class<?> getBoundClass() {
        return Individual.class;
    }

    @Override
    protected String getStartElementName() {
        return "individuals";
    }

    @Override
    protected int getTotalEntityCount(TaskContext taskContext) {
        return (int) individualService.getTotalIndividualCount();
    }

}
