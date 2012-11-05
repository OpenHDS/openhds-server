package org.openhds.webservice.resources;

import java.io.Serializable;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.PregnancyService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Outcome;
import org.openhds.domain.model.PregnancyOutcome;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pregnancyoutcomes")
public class PregnancyOutcomeResource extends AbstractResource<PregnancyOutcome> {

    private FieldBuilder fieldBuilder;
    private PregnancyService pregnancyService;
    private SitePropertiesService siteProperties;

    @Autowired
    public PregnancyOutcomeResource(PregnancyService pregnancyService, FieldBuilder fieldBuilder,
            SitePropertiesService siteProperties) {
        this.pregnancyService = pregnancyService;
        this.fieldBuilder = fieldBuilder;
        this.siteProperties = siteProperties;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody PregnancyOutcome pregOutcome) {
        return createResource(pregOutcome);
    }

    @Override
    protected PregnancyOutcome copy(PregnancyOutcome item) {
        PregnancyOutcome copy = new PregnancyOutcome();
        copy.setChildEverBorn(item.getChildEverBorn());
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setFather(copyIndividual(item.getFather()));
        copy.setMother(copyIndividual(item.getMother()));
        copy.setNumberOfLiveBirths(item.getNumberOfLiveBirths());
        copy.setOutcomeDate(item.getOutcomeDate());
        copy.setVisit(copyVisit(item.getVisit()));

        for (Outcome outcome : item.getOutcomes()) {
            Outcome copyOutcome = new Outcome();
            copyOutcome.setChild(copyIndividual(outcome.getChild()));
            copyOutcome.setType(outcome.getType());
            copy.addOutcome(copyOutcome);
        }

        return copy;
    }

    @Override
    protected void saveResource(PregnancyOutcome item) throws ConstraintViolations {
        pregnancyService.createPregnancyOutcome(item);
    }

    @Override
    protected void setReferencedFields(PregnancyOutcome item, ConstraintViolations cv) {
        item.setVisit(fieldBuilder.referenceField(item.getVisit(), cv));
        
        FieldWorker fw = fieldBuilder.referenceField(item.getCollectedBy(), cv);
        item.setCollectedBy(fw);
        
        Individual father = fieldBuilder.referenceField(item.getFather(), cv, "Invalid father id");
        item.setFather(father);
        Individual mother = fieldBuilder.referenceField(item.getMother(), cv, "Invalid mother id");
        item.setMother(mother);
        
        for (Outcome outcome : item.getOutcomes()) {
            if (outcome.getChild() != null) {
                outcome.getChild().setFather(father);
                outcome.getChild().setMother(mother);
                outcome.getChild().setCollectedBy(fw);

                Membership membership = outcome.getChildMembership();
                if (membership == null) {
                    cv.addViolations("All children must specify a membership");
                    return;
                }
                
                SocialGroup sg = fieldBuilder.referenceField(membership.getSocialGroup(), cv);
                membership.setCollectedBy(fw);
                membership.setIndividual(outcome.getChild());
                membership.setSocialGroup(sg);
                membership.setStartDate(item.getOutcomeDate());
                membership.setStartType(siteProperties.getBirthCode());
                membership.setEndType(siteProperties.getNotApplicableCode());
            }
        }
    }
}
