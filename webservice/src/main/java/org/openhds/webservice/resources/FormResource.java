package org.openhds.webservice.resources;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.openhds.controller.service.FormService;
import org.openhds.domain.model.wrappers.Forms;
import org.openhds.task.support.FileResolver;
import org.openhds.task.support.TaskExecutor;
import org.openhds.webservice.CacheResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/forms")
public class FormResource {

	private static final Logger logger = LoggerFactory.getLogger(FormResource.class);
		
    private FormService formService;
    private FileResolver fileResolver;
    private TaskExecutor taskExecutor;
    
   @Autowired
    public FormResource(FormService formService, FileResolver fileResolver, TaskExecutor taskExecutor) {
        this.formService = formService;
        this.fileResolver = fileResolver;
        this.taskExecutor = taskExecutor;
    }
  

    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public Forms getAllActiveForms() {
        Forms forms = new Forms();
        forms.setForms(formService.getAllActiveForms());
        return forms;
    }
    
    @RequestMapping(value = "/cached", method = RequestMethod.GET, produces = "application/xml")
    public void getCachedForms(HttpServletResponse response) {
        try {
        	taskExecutor.executeFormXmlWriterTask();  
            CacheResponseWriter.writeResponse(fileResolver.resolveFormXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing form xml file: " + e.getMessage());
        }
    }
}
