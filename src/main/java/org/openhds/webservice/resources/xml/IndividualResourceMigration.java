package org.openhds.webservice.resources.xml;

import java.io.Serializable;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.IndividualService;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Individual;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/individual")
public class IndividualResourceMigration extends AbstractResource<Individual> {

    private IndividualService individualService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public IndividualResourceMigration(IndividualService individualService, FieldBuilder fieldBuilder) {
        this.individualService = individualService;
        this.fieldBuilder = fieldBuilder;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody Individual individual) {
        return createResource(individual);
    }

    @Override
    protected Individual copy(Individual item) {
    	Individual copy = new Individual();
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setFirstName(item.getFirstName());
        copy.setLastName(item.getLastName());
        String middleName = item.getMiddleName() == null ? "" : item.getMiddleName();
        copy.setMiddleName(middleName);
        String religion = item.getReligion() == null ? "" : item.getReligion();
        copy.setReligion(religion);
        copy.setExtId(item.getExtId());
        copy.setFather(item.getFather());
        copy.setMother(item.getMother());
        copy.setDob(item.getDob());
        copy.setGender(item.getGender());
        copy.setDobAspect(item.getDobAspect());
       
        return copy;
    }

    @Override
    protected void saveResource(Individual item) throws ConstraintViolations {
    	individualService.createIndividual(item);
    }

    @Override
    protected void setReferencedFields(Individual item, ConstraintViolations cv) {
        FieldWorker fw = fieldBuilder.referenceField(item.getCollectedBy(), cv);
        item.setCollectedBy(fw);
        
       
            Individual individual = item;
            individual.setMother(fieldBuilder.referenceField(individual.getMother(), cv, "Invalid mother id "));
            individual.setFather(fieldBuilder.referenceField(individual.getFather(), cv, "Invalid father id"));
            individual.setCollectedBy(fw);
        
    }
}
