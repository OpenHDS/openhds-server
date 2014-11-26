package org.openhds.webservice.resources;

import java.io.Serializable;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.HeadOfHouseholdService;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.HeadOfHousehold;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.wrappers.Visits;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hoh")
public class HeadOfHouseholdResource extends AbstractResource<HeadOfHousehold> {
    private final HeadOfHouseholdService headOfHouseholdService;
    private final FieldBuilder fieldBuilder;
    @Autowired
    public HeadOfHouseholdResource(HeadOfHouseholdService headOfHouseholdService, FieldBuilder fieldBuilder, EntityService entityService,
            FileResolver fileResolver) {
        this.headOfHouseholdService = headOfHouseholdService;
        this.fieldBuilder = fieldBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Visits getAllVisits() {   	
    	return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody HeadOfHousehold hoh) {    	
    	return createResource(hoh);
    }
    
    @Override
    protected HeadOfHousehold copy(HeadOfHousehold item) {
    	HeadOfHousehold copy = new HeadOfHousehold();
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setOldHoh(copyIndividual(item.getOldHoh()));
        copy.setNewHoh(copyIndividual(item.getNewHoh()));
        copy.setVisit(copyVisit(item.getVisit()));
        return copy;
    }

	@Override
	protected void saveResource(HeadOfHousehold hoh)
			throws ConstraintViolations {
		headOfHouseholdService.createHeadOfHousehold(hoh);
	}

	@Override
	protected void setReferencedFields(HeadOfHousehold hoh,
			ConstraintViolations cv) {
        hoh.setCollectedBy(fieldBuilder.referenceField(hoh.getCollectedBy(), cv));
        hoh.setOldHoh(fieldBuilder.referenceField(hoh.getOldHoh(), cv,
                "Invalid Ext Id for old Group Head"));
        hoh.setNewHoh(fieldBuilder.referenceField(hoh.getNewHoh(), cv,
                "Invalid Ext Id for new Group Head"));  
        hoh.setVisit(fieldBuilder.referenceField(hoh.getVisit(), cv));
        for(Membership m : hoh.getMemberships()){
        	m.setSocialGroup(fieldBuilder.referenceField(m.getSocialGroup(), cv));
        }
                
        Death death = hoh.getDeath();
        death.setCollectedBy(fieldBuilder.referenceField(death.getCollectedBy(), cv));
        death.setIndividual(fieldBuilder.referenceField(death.getIndividual(), cv,
                "Individual external id referenced in death event is invalid"));
        death.setVisitDeath(fieldBuilder.referenceField(death.getVisitDeath(), cv));
        hoh.setDeath(death);
        
        hoh.setSocialGroup(fieldBuilder.referenceField(hoh.getSocialGroup(), cv));
	}    
}
