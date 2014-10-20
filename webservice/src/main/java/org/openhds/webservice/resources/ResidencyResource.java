package org.openhds.webservice.resources;

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.ResidencyService;
import org.openhds.domain.model.Residency;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/residencyimg")
public class ResidencyResource extends AbstractResource<Residency> {

    private ResidencyService residencyService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public ResidencyResource(ResidencyService residencyService, FieldBuilder fieldBuilder) {
        this.residencyService = residencyService;
        this.fieldBuilder = fieldBuilder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody Residency residency) {
        return createResource(residency);
    }
    
    @Override
    protected Residency copy(Residency item) {
    	Residency copy = new Residency();

        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setEndDate(item.getEndDate());
        copy.setEndType(item.getEndType());
        copy.setIndividual(copyIndividual(item.getIndividual()));
       copy.setLocation(copyLocation(item.getLocation()));
        copy.setStartDate(item.getStartDate());
        copy.setStartType(item.getStartType());
        return copy;
    }



	@Override
    protected void saveResource(Residency item) throws ConstraintViolations {
    	residencyService.createResidency(item);
    }

    @Override
    protected void setReferencedFields(Residency item, ConstraintViolations cv) {
        item.setCollectedBy(fieldBuilder.referenceField(item.getCollectedBy(), cv));
        item.setIndividual(fieldBuilder.referenceField(item.getIndividual(), cv, "Residency Individual id not valid"));
    }

}
