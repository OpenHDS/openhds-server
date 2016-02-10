package org.openhds.web.crud.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openhds.controller.exception.AuthorizationException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.ExtraForm;
import org.openhds.domain.model.ExtraForm.Data;
import org.openhds.domain.model.Form;
import org.openhds.controller.service.ExtraFormService;
import org.openhds.controller.service.FormService;

public class FormCrudImpl extends EntityCrudImpl<Form, String> {

	FormService service;
	ExtraFormService extraFormService;
	private List<ExtraForm> extraForms;
	private String formName;
	final String EXPORT_DATE_PATTERN = "yyyy-MM-dd_HHmmss";

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
	 
	public String submissionSetup() {		 
	    showListing=false;
	    navMenuBean.setNextItem(entityClass.getSimpleName());
	    navMenuBean.addCrumb(entityClass.getSimpleName() + " Submissions");
	    String result = scalarSetup(outcomePrefix + "_submissions");
		
		String currentForm = entityItem.getFormName();
		if(extraForms == null || !formName.equals(currentForm)){
			 formName = currentForm;
			 try {
				extraForms = extraFormService.getForms(currentForm);
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
				session.setAttribute("submissionError", "Exception while trying to load submissions");
			}
		}

	    return result;
	  }
	
	public void removeSessionAttributeAfterRender(PhaseEvent event){
	    if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
	        FacesContext.getCurrentInstance().getExternalContext()
	            .getSessionMap().remove("submissionError");
	    }
	}
	  
	 public int getSubmissionCount(){
		 int noOfSubmissions = -1;
		 if(extraForms != null){
			 noOfSubmissions = extraForms.size();
		 }
		 return noOfSubmissions;
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

	public ExtraFormService getExtraFormService() {
		return extraFormService;
	}

	public void setExtraFormService(ExtraFormService extraFormService) {
		this.extraFormService = extraFormService;
	}
	
    /**
     * This method writes back CSV file as a response.
     * @throws IOException
     */
    public void downloadCsv(){	    	
    	if(extraForms != null){
        	String formName = entityItem.getFormName();
	    	Date date = new Date();
	    	SimpleDateFormat dt1 = new SimpleDateFormat(EXPORT_DATE_PATTERN);
	    	String formattedDate = dt1.format(date);
	        
	    	String exportFileName = formName + "_submissions_" + formattedDate + ".csv";
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	
	        response.reset(); 
	        response.setHeader("Content-Type", "text/csv"); 
	        response.setHeader("Content-Disposition","attachment; filename=" + exportFileName);
	         
			try {
		        OutputStream responseOutputStream;
				responseOutputStream = response.getOutputStream();
		        StringBuffer dataBuffer;
		        
		        for(int i = 0; i < extraForms.size(); i++){
		        	dataBuffer = new StringBuffer();
		        	
		        	ExtraForm extraForm = extraForms.get(i);
		        	List<Data> dataList = extraForm.getData();
		        	
		        	if(i == 0){
		        		StringBuffer headers = extractColumnHeaders(dataList);
		        		headers.deleteCharAt(0);
		        		headers.append("\n");
		        		writeStringToOutputStream( headers.toString(), responseOutputStream);
		        	}
		        	
		        	for(int j = 0; j < dataList.size(); j++){
		        		Data data = dataList.get(j);
		        		dataBuffer.append(";");
		        		dataBuffer.append(data.value);
		        	}
		        	dataBuffer.deleteCharAt(0);
		        	dataBuffer.append("\n");
		        	        	
		        	writeStringToOutputStream(dataBuffer.toString(), responseOutputStream);
		        }	
		        responseOutputStream.flush();        
		        responseOutputStream.close();
		        
			} catch (IOException e) {
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
				session.setAttribute("submissionError", "Exception while trying to load submissions");
			}	         
	        // JSF doc:
	        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated
	        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
	        // as soon as the current phase is completed.
	        facesContext.responseComplete();
    	}
    }  
    
    private StringBuffer extractColumnHeaders(List<Data> dataList){
    	StringBuffer dataBuffer = new StringBuffer();
    	for(int j = 0; j < dataList.size(); j++){
    		Data data = dataList.get(j);
    		dataBuffer.append(";");
    		dataBuffer.append(data.columnName);
    	}   	
    	return dataBuffer;
    }
    
    private void writeStringToOutputStream(String inputString, OutputStream outputStream) throws IOException{
        InputStream inputStream; 
    	inputStream = new ByteArrayInputStream(inputString.getBytes("UTF-8"));

        byte[] bytesBuffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = inputStream.read(bytesBuffer)) > 0) {
        	outputStream.write(bytesBuffer, 0, bytesRead);
        }
        
        inputStream.close();
    }
}

