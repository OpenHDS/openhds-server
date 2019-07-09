package org.openhds.webservice.resources.xml;

import java.io.Serializable;
import org.openhds.exception.ConstraintViolations;
import org.openhds.service.ExtraFormService;
import org.openhds.domain.ExtraForm;
import org.openhds.domain.TableDummy;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * ExtraFormResource:
 * 
 * WebService that handles ExtraForm functionality
 *
 */
@Controller
@RequestMapping( value="extraform")
public class ExtraFormResource extends AbstractResource<TableDummy>{
	
	private ExtraFormService extraFormService;
	
	@Autowired
	public ExtraFormResource(ExtraFormService service){
		this.extraFormService = service;
	}
		
	@RequestMapping(value="create", method = RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Serializable> createExtraFormTable(@RequestBody TableDummy tableDummy) {
		return createResource(tableDummy);
    }
	
	@RequestMapping(value="insert", method = RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<?> extraFormData(@RequestBody ExtraForm extraForm) {
		try {
			extraFormService.insertExtraFormData(extraForm);
		} catch (ConstraintViolations e) {
			return buildBadRequest(e);
		}		 
		return (ResponseEntity<?>) new ResponseEntity<ExtraForm>(new ExtraForm(),HttpStatus.CREATED);
    }
	
    protected ResponseEntity<WebServiceCallException> buildBadRequest(ConstraintViolations cv) {
        return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
    }
	
	private boolean verifyKey(String key) throws ConstraintViolations{
		return extraFormService.isValidKey(key);
	}

	@Override
	protected TableDummy copy(TableDummy item) {
		TableDummy copiedItem = new TableDummy();
		copiedItem.setKey(item.getKey());
		copiedItem.setName(item.getName());
		return copiedItem;
	}

	@Override
	protected void saveResource(TableDummy item) throws ConstraintViolations {
		if(verifyKey(item.getKey())){	
			extraFormService.createTable(item);		
		}
	}

	@Override
	protected void setReferencedFields(TableDummy item, ConstraintViolations cv) {
		if(item == null)
			cv.addViolations("Could not parse Create Table Xml.");
	}
}
