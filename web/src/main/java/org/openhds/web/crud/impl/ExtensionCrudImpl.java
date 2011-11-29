package org.openhds.web.crud.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.openhds.domain.model.PrimitiveType;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.EntityType;
import org.openhds.controller.service.ExtensionService;

public class ExtensionCrudImpl extends EntityCrudImpl<ClassExtension, String> {
	
	EntityValidationService<ClassExtension> entityValidator;
	ExtensionService service;
    
	// flag for determining if the type of the extension is multiple choice
	boolean multipleChoice;
	
	// individual answer
	String answer;
	
	// all possible answers separated by ','
	String answerList;
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date validFrom;
    
	public ExtensionCrudImpl(Class<ClassExtension> entityClass) {
        super(entityClass);
    }
	
    @Override
    public String create() {
    	entityItem.setAnswers(answerList);
    	
    	try {
			service.evaluateClassExtension(entityItem);
	        return super.create(); 
		} catch (ConstraintViolations e) {
            jsfService.addError(e.getMessage());
		}
		return null;
    }
    
    @Override
    public String delete() {
    	
    	ClassExtension persistedItem = converter.getAsObject(FacesContext.getCurrentInstance(), null, jsfService.getReqParam("itemId"));
    	
    	try {
    		service.deleteClassExtension(persistedItem);	
	        return super.delete();		
    	}
    	catch(ConstraintViolations e) {
    		jsfService.addError(e.getMessage());
    	}
    	return null;
    }
    
    public List<SelectItem> getEntityTypes() {
    	List<SelectItem> output = new ArrayList<SelectItem>();
		List<EntityType> items = jsfService.arrayToList(EntityType.values());	
		
		for (EntityType et : items) {
			output.add(new SelectItem(et, et.name()));
		}
		return output;
    }
    
    public List<SelectItem> getPrimTypes() {
    	List<SelectItem> output = new ArrayList<SelectItem>();
		List<PrimitiveType> items = jsfService.arrayToList(PrimitiveType.values());
		
		for (PrimitiveType pt : items) {
			output.add(new SelectItem(pt, pt.name()));
		}
		return output;
    }
            
    /**
     * This method is called when the extension type value changes. This is important in
     * determining if the type is of MULTIPLECHOICE.
     */
	public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
		// this is to signal jsf to skip the validations phase and proceed to render response.
		// if this is not here, then it's likely that an error message will be displayed.
		FacesContext.getCurrentInstance().renderResponse();
		if (event.getNewValue().toString().equals("MULTIPLECHOICE"))
			multipleChoice = true;
		else
			multipleChoice = false;
	}
	
    @Override
    public String createSetup() {
    	// needed to refresh the state of the crud
    	multipleChoice = false;
    	answer = answerList = "";
    	return super.createSetup();
    }
    
    public void addAnswer() {
    	answerList += answer + ", ";
    	answer = "";
    }
    
    public void clearAnswer() {
    	answer = answerList = "";
    }
    
	public ExtensionService getService() {
		return service;
	}

	public void setService(ExtensionService service) {
		this.service = service;
	}
	
	public EntityValidationService<ClassExtension> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(EntityValidationService<ClassExtension> entityValidator) {
		this.entityValidator = entityValidator;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
    public String getAnswerList() {
		return answerList;
	}

	public void setAnswerList(String answerList) {
		this.answerList = answerList;
	}
	
    public boolean isMultipleChoice() {
		return multipleChoice;
	}

	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
	}
}