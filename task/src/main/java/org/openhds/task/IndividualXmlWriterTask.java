package org.openhds.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhds.controller.service.IndividualService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.util.ShallowCopier;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("individualXmlWriter")
public class IndividualXmlWriterTask extends XmlWriterTemplate<Individual> {
    private IndividualService individualService;

    @Autowired
    public IndividualXmlWriterTask(AsyncTaskService asyncTaskService, IndividualService individualService) {
        super(asyncTaskService, AsyncTaskService.INDIVIDUAL_TASK_NAME);
        this.individualService = individualService;
    }

    @Override
    protected Individual makeCopyOf(Individual original) {
        return ShallowCopier.shallowCopyIndividual(original);
    }

    @Override
    protected List<Individual> getEntitiesInRange(TaskContext taskContext, int start, int pageSize) {
        return getAllIndividualsWithResidencies(start, pageSize);
    }

    private List<Individual> getAllIndividualsWithResidencies(int start,
			int pageSize) {
		Individual indiv;
		List<Individual> indivList= individualService.getAllIndividualsInRange(start,pageSize);
		Iterator<Individual> it= indivList.iterator();
		List<Individual> indivList2 = new ArrayList<Individual>();
		while(it.hasNext()){
			indiv=(Individual)it.next();
			if(indiv.getCurrentResidency()!=null){
				indivList2.add(indiv);
			}
		}
		return indivList2;
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
