package org.openhds.task;

import java.util.List;

import org.openhds.service.FormService;
import org.openhds.domain.Form;
import org.openhds.util.ShallowCopier;
import org.openhds.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("formXmlWriter")
public class FormsXmlWriterTask extends XmlWriterTemplate<Form> {

    private FormService formService;

    @Autowired
    public FormsXmlWriterTask(AsyncTaskService asyncTaskService, 
    		FormService formService) {
        super(asyncTaskService, AsyncTaskService.FORM_TASK_NAME);
        this.formService = formService;
    }

    @Override
    protected Form makeCopyOf(Form original) {
        return ShallowCopier.copyForm(original);
    }

    @Override
    protected List<Form> getEntitiesInRange(TaskContext taskContext, int i, int pageSize) {
        return formService.getAllActiveForms();
    }

  

    @Override
    protected String getStartElementName() {
        return "forms";
    }

    @Override
    protected int getTotalEntityCount(TaskContext taskContext) {
        return (int) formService.getTotalFormCount();
    }

	@Override
	protected Class<?> getBoundClass() {
		// TODO Auto-generated method stub
		return Form.class;
	}

}
