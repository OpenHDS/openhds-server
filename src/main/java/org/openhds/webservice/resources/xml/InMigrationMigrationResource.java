package org.openhds.webservice.resources.xml;

import java.io.Serializable;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.InMigrationService;
import org.openhds.dao.GenericDao;
import org.openhds.dao.GenericDao.ValueProperty;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.InMigration;
import org.openhds.domain.MigrationType;
import org.openhds.domain.Residency;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/inmigrationimg")
public class InMigrationMigrationResource extends AbstractResource<InMigration> {

    private InMigrationService inMigrationService;
    private FieldBuilder fieldBuilder;
	private GenericDao genericDao; 

    @Autowired
    public InMigrationMigrationResource(InMigrationService inMigrationService, FieldBuilder fieldBuilder, GenericDao genericDao) {
        this.inMigrationService = inMigrationService;
        this.fieldBuilder = fieldBuilder;
        this.genericDao = genericDao;
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
        
        copy.setOrigin(item.getOrigin());
        copy.setReason(item.getReason());
        copy.setRecordedDate(item.getRecordedDate());
        copy.setVisit(copyVisit(item.getVisit()));
        
        return copy;
    }

    @Override
    protected void saveResource(InMigration item) throws ConstraintViolations {
        inMigrationService.createInMigrationImg(item);
    }

    @Override
    protected void setReferencedFields(InMigration item, ConstraintViolations cv) {
        FieldWorker fw = fieldBuilder.referenceField(item.getCollectedBy(), cv);
        item.setCollectedBy(fw);
        item.setVisit(fieldBuilder.referenceField(item.getVisit(), cv));
        
        if (item.getMigType() == null || MigrationType.INTERNAL_INMIGRATION.equals(item.getMigType())) {
            item.setMigTypeInternal();
        } else {
        	item.setMigTypeExternal();
        }
            item.setIndividual(fieldBuilder.referenceField(item.getIndividual(), cv, "Invalid individual id"));
            item.setResidency(genericDao.findByMultiProperty(Residency.class, 
				getValueProperty("individual", item.getIndividual()),
				getValueProperty("startDate", item.getRecordedDate())));
    }
    
    private ValueProperty getValueProperty(final String propertyName, final Object propertyValue) {
		return new ValueProperty() {

            public String getPropertyName() {
                return propertyName;
            }

            public Object getValue() {
                return propertyValue;
            }
        };		
	}
}
