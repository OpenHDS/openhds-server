package org.openhds.webservice.resources;

/**
 * A service class to be used when dealing with the Baseline 
 * 
 * @author Aurelio Di Pasquale
 *
 */

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.InMigrationService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/baseline")
public class BaselineResource extends AbstractResource<InMigration> {

    private InMigrationService inMigrationService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public BaselineResource(InMigrationService inMigrationService, FieldBuilder fieldBuilder) {
        this.inMigrationService = inMigrationService;
        this.fieldBuilder = fieldBuilder;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody InMigration inMigration) {
        return createResource(inMigration);
    }

    @Override
    protected InMigration copy(InMigration item) {
        InMigration copy = new InMigration();
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setIndividual(copyIndividual(item.getIndividual()));
        copy.setMigType(item.getMigType());
        
        copy.setOrigin(item.getMigType().toString());
        copy.setReason(item.getMigType().toString());
        copy.setRecordedDate(item.getRecordedDate());
        copy.setVisit(copyVisit(item.getVisit()));
        
        return copy;
    }

    @Override
    protected void saveResource(InMigration item) throws ConstraintViolations {
        inMigrationService.createInMigration(item);
    }

    @Override
    protected void setReferencedFields(InMigration item, ConstraintViolations cv) {
        FieldWorker fw = fieldBuilder.referenceField(item.getCollectedBy(), cv);
        item.setCollectedBy(fw);
        item.setVisit(fieldBuilder.referenceField(item.getVisit(), cv));
        
            Individual individual = item.getIndividual();
            individual.setMother(fieldBuilder.referenceField(individual.getMother(), cv, "Invalid mother id "));
            individual.setFather(fieldBuilder.referenceField(individual.getFather(), cv, "Invalid father id"));
            individual.setCollectedBy(fw);
    }
}
