package org.openhds.web.crud.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.openhds.exception.AuthorizationException;
import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.ExtraForm;
import org.openhds.domain.ExtraForm.Data;
import org.openhds.domain.Form;
import org.openhds.web.beans.ExtraFormFile;
import org.openhds.service.ExtraFormService;
import org.openhds.service.FormService;

public class FormCrudImpl extends EntityCrudImpl<Form, String> {

	FormService service;
	ExtraFormService extraFormService;
	private List<ExtraForm> extraForms;
	private String formName;
	final String EXPORT_DATE_PATTERN = "yyyy-MM-dd_HHmmss";
	private int submissionCount;
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");


	public FormCrudImpl(Class<Form> entityClass) {
        super(entityClass);
    }
	
	private String getResourceBundleString(String key){
        String resourceText = "";
        try{
		FacesContext facesContext = FacesContext.getCurrentInstance();
        String messageBundleName = facesContext.getApplication().getMessageBundle();
        Locale locale = facesContext.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
        
        resourceText = bundle.getString(key);
        }
        catch(Exception e){
        	resourceText = "Could not load value from Resource Bundle.";
        }
        return resourceText;
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
				submissionCount = extraForms.size();
			} catch (Exception e) {
				submissionCount = -1; 
				String errorMessage = getResourceBundleString("submissionLoadError");
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
				session.setAttribute("submissionError", errorMessage);
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
		 return submissionCount;
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
		
	public boolean isNewFileRequired(){
		Date date = DateTime.now().minusDays(1).toDate();
		List<ExtraFormFile> list = getAvailableExports();
		
		if (list != null && !list.isEmpty()) {
			ExtraFormFile latestExport = list.get(list.size()-1);
			if(FileUtils.isFileNewer(latestExport, date)){
				return false;
			}
		}
		
		return true;
	}
	
	private String generatetExportFileName(String formName){
    	Date date = new Date();
    	SimpleDateFormat dt1 = new SimpleDateFormat(EXPORT_DATE_PATTERN);
    	String formattedDate = dt1.format(date);   
    	String exportFileName = formName + "_submissions_" + formattedDate + ".csv";
    	return exportFileName;
	}
	
	private void writeCsvFileToDisk(String formName){			
		String exportFileName = generatetExportFileName(formName);
        File f = new File(TMP_DIR, exportFileName);
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
            generateAndWriteCsvToFile(fos);
            fos.flush();
		} catch (FileNotFoundException e) {} 
		catch (IOException e) {}
		finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}		
	}
	
	private void generateAndWriteCsvToFile(OutputStream responseOutputStream) throws IOException{
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
	}
	
	public List<ExtraFormFile> getAvailableExports(){
		ArrayList<ExtraFormFile> files = new ArrayList<ExtraFormFile>();
		
	    String pathToScan = TMP_DIR;
	    String fileThatYouWantToFilter;
	    File folderToScan = new File(pathToScan);
	    File[] listOfFiles = folderToScan.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {
	            fileThatYouWantToFilter = listOfFiles[i].getName();
	            if (formName != null && fileThatYouWantToFilter.startsWith(formName)
	                    && fileThatYouWantToFilter.endsWith(".csv")) {	
	                ExtraFormFile f = new ExtraFormFile(pathToScan, fileThatYouWantToFilter);
	                files.add(f);
	            }
	        }
	    }	    
	    //Sort according to last modified date
	    Collections.sort(files, new Comparator<ExtraFormFile>(){
	        public int compare(ExtraFormFile f1, ExtraFormFile f2)
	        {
	            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
	        } });
		return files;
	}
		
	public void deleteAllExports(){
	    String pathToScan = TMP_DIR;
	    String fileThatYouWantToFilter;
	    File folderToScan = new File(pathToScan);
	    File[] listOfFiles = folderToScan.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {
	            fileThatYouWantToFilter = listOfFiles[i].getName();
	            if (fileThatYouWantToFilter.startsWith(formName)
	                    && fileThatYouWantToFilter.endsWith(".csv")) {
	                File f = new File(pathToScan, fileThatYouWantToFilter);
	            	try{
	            		f.delete();
	            	}
	            	catch(Exception e){}
	            }
	        }
	    }		
	}
	
	public void deleteSubmissionCsv(){
		FacesContext fc = FacesContext.getCurrentInstance();
	    Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	    String fileName =  params.get("submissionFileName"); 

	    if(fileName != null && !fileName.isEmpty()){
			File f = new File(TMP_DIR, fileName);
			if(f.exists() && f.getName().startsWith(formName)){
				try{
					f.delete();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
	    }
	}
	
	public void downloadSubmissionCsv(){
		FacesContext fc = FacesContext.getCurrentInstance();
	    Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	    String fileName =  params.get("submissionFileName"); 

	    if(fileName != null && !fileName.isEmpty()){			
			File f = new File(TMP_DIR, fileName);
			if(f.exists() && f.getName().startsWith(formName)){
				try{     
			        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			        response.reset(); 
			        response.setHeader("Content-Type", "text/csv"); 
			        response.setHeader("Content-Disposition","attachment; filename=" + fileName);
			    	
			        OutputStream responseOutputStream;
					responseOutputStream = response.getOutputStream();
	
					FileInputStream fis = new FileInputStream(f);
					
					byte[] outputByte = new byte[4096];
					//copy binary contect to output stream
					while(fis.read(outputByte, 0, 4096) != -1)
					{
						responseOutputStream.write(outputByte, 0, 4096);
					}
					fis.close();
					
			        responseOutputStream.flush();        
			        responseOutputStream.close();
			        
			        // JSF doc:
			        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated
			        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
			        // as soon as the current phase is completed.
			        fc.responseComplete();
				} catch (IOException e) {	
					String errorMessage = getResourceBundleString("submissionCsvGenerateError");
				    fc.addMessage("submissionForm:button", new FacesMessage(FacesMessage.SEVERITY_WARN, errorMessage, null)); 
				}
			}
	    }
	}	
	
    /**
     * This method writes back CSV file as a response.
     * @throws IOException
     */
    public void createNewExportCsv(){	   
       	String formName = entityItem.getFormName(); 	
       	writeCsvFileToDisk(formName); 
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

