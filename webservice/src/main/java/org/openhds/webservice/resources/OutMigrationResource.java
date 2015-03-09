package org.openhds.webservice.resources;

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.OutMigrationService;
import org.openhds.domain.model.OutMigration;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/outmigrations")
public class OutMigrationResource extends AbstractResource<OutMigration> {

    private OutMigrationService outMigrationService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public OutMigrationResource(OutMigrationService outMigrationService, FieldBuilder fieldBuilder) {
        this.outMigrationService = outMigrationService;
        this.fieldBuilder = fieldBuilder;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/xml")
    public ResponseEntity<? extends Serializable> insert(@RequestBody OutMigration outMigration) {
        return createResource(outMigration);
    }

    @Override
    protected OutMigration copy(OutMigration item) {
        OutMigration copy = new OutMigration();
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setDestination(item.getDestination());
        copy.setIndividual(copyIndividual(item.getIndividual()));
        copy.setReason(item.getReason());
        copy.setRecordedDate(item.getRecordedDate());
        copy.setVisit(copyVisit(item.getVisit()));

        return copy;
    }

    @Override
    protected void saveResource(OutMigration outMigration) throws ConstraintViolations {
    	outMigrationService.evaluateOutMigration(outMigration);
        outMigrationService.createOutMigration(outMigration);
    }

    @Override
    protected void setReferencedFields(OutMigration outMigration, ConstraintViolations cv) {
        outMigration.setCollectedBy(fieldBuilder.referenceField(outMigration.getCollectedBy(), cv));
        outMigration.setIndividual(fieldBuilder.referenceField(outMigration.getIndividual(), cv,
                "Out Migration individual ext id is invalid"));
        outMigration.setVisit(fieldBuilder.referenceField(outMigration.getVisit(), cv));
    }
}
