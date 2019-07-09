package org.openhds.webservice.resources.xml;

/**
 * A service class to be used when dealing with the Baseline 
 * 
 * @author Aurelio Di Pasquale
 * this class re-use the InMigrationService. This is why we have migType/setOrigin/setReason
 * To do create a BaselineService to get more clean
 *
 */

import java.io.Serializable;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.InMigrationService;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.MigrationType;
import org.openhds.service.SitePropertiesService;
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
    private SitePropertiesService siteProperties;

    @Autowired
    public BaselineResource(InMigrationService inMigrationService, FieldBuilder fieldBuilder,
            SitePropertiesService siteProperties) {
        this.inMigrationService = inMigrationService;
        this.fieldBuilder = fieldBuilder;
        this.siteProperties = siteProperties;
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
        copy.setMigType(MigrationType.BASELINE);
        
        copy.setOrigin(siteProperties.getEnumerationCode());
        copy.setReason(siteProperties.getEnumerationCode());
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
            checkIndividual(individual, cv);
            individual.setMother(fieldBuilder.referenceField(individual.getMother(), cv, "Invalid mother id "));
            individual.setFather(fieldBuilder.referenceField(individual.getFather(), cv, "Invalid father id"));
            individual.setCollectedBy(fw);
    }
    
    private void checkIndividual(Individual individual, ConstraintViolations cv){
    	if(individual.getDob() == null)
    		cv.addViolations("Dob cannot be null.");
    	if(individual.getGender() == null || individual.getGender().isEmpty())
    		cv.addViolations("Please specify a gender.");
    	if(individual.getDobAspect() == null || individual.getDobAspect().isEmpty())
    		cv.addViolations("dobAspect cannot be empty!");
    }
}
