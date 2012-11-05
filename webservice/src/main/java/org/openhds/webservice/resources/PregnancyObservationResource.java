package org.openhds.webservice.resources;

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.PregnancyService;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pregnancyobservations")
public class PregnancyObservationResource extends AbstractResource<PregnancyObservation> {

    private PregnancyService pregnancyService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public PregnancyObservationResource(PregnancyService pregnancyService, FieldBuilder fieldBuilder) {
        this.pregnancyService = pregnancyService;
        this.fieldBuilder = fieldBuilder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody PregnancyObservation pregObs) {
        return createResource(pregObs);
    }

    @Override
    protected PregnancyObservation copy(PregnancyObservation item) {
        PregnancyObservation copy = new PregnancyObservation();
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setExpectedDeliveryDate(item.getExpectedDeliveryDate());
        copy.setMother(copyIndividual(item.getMother()));
        copy.setRecordedDate(item.getRecordedDate());
        copy.setVisit(copyVisit(item.getVisit()));

        return copy;
    }

    @Override
    protected void saveResource(PregnancyObservation pregObs) throws ConstraintViolations {
        pregnancyService.createPregnancyObservation(pregObs);
    }

    @Override
    protected void setReferencedFields(PregnancyObservation pregObs, ConstraintViolations cv) {
        pregObs.setCollectedBy(fieldBuilder.referenceField(pregObs.getCollectedBy(), cv));
        pregObs.setMother(fieldBuilder.referenceField(pregObs.getMother(), cv,
                "Mother ext id not valid for pregnancy observation"));
        pregObs.setVisit(fieldBuilder.referenceField(pregObs.getVisit(), cv));
    }
}
