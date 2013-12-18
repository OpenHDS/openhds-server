package org.openhds.web.crud.impl;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Form;
import org.openhds.controller.service.FormService;

public class FormCrudImpl extends EntityCrudImpl<Form, String> {

	FormService service;

	public FormCrudImpl(Class<Form> entityClass) {
        super(entityClass);
    }
	
	@Override
	public String createSetup() {
        reset(false, true);
        showListing=false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }


	
	 public List<SelectItem> getFormActive() {
	    	List<SelectItem> output = new ArrayList<SelectItem>();
	
			output.add(new SelectItem(null, ""));
			output.add(new SelectItem("Yes", "Active"));
			output.add(new SelectItem("No", "Not Active"));
			return output;
	    }

	 
	 public List<SelectItem> getFormGender() {
	    	List<SelectItem> output = new ArrayList<SelectItem>();
	
			output.add(new SelectItem(null, ""));
			output.add(new SelectItem("M", "Male"));
			output.add(new SelectItem("F", "Female"));
			output.add(new SelectItem("All", "All"));
			return output;
	    }

	
    @Override
    public String create() {

    	try {
    		service.evaluateForm(entityItem);
	        return super.create();
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	} catch(AuthorizationException e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
	public FormService getService() {
		return service;
	}

	public void setService(FormService service) {
		this.service = service;
	}
}

