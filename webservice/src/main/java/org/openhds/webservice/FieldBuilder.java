package org.openhds.webservice;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.FieldWorkerService;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.controller.service.VisitService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Builder class to aggregate the the operations involved in retrieving previously saved entities
 */
@Component
public class FieldBuilder {
    private static final String INVALID_VISIT_ID = "Invalid Visit Id";
    private static final String INVALID_FIELD_WORKER_ID = "Invalid Field Worker Id";

    private VisitService visitService;
    private IndividualService individualService;
    private FieldWorkerService fieldWorkerService;
    private LocationHierarchyService locationHierarchyService;
    private SocialGroupService socialGroupService;

    public Visit referenceField(Visit visit, ConstraintViolations violations) {
        if (visit == null || visit.getExtId() == null) {
            violations.addViolations("No visit id provided");
        } else {
            try {
                return visitService.findVisitById(visit.getExtId(), INVALID_VISIT_ID);
            } catch (Exception e) {
                violations.addViolations(INVALID_VISIT_ID);
            }
        }

        return null;
    }

    public Individual referenceField(Individual individual, ConstraintViolations violations, String msg) {
        if (individual.getExtId() == null) {
            violations.addViolations(msg);
        } else {
            try {
                return individualService.findIndivById(individual.getExtId(), msg);
            } catch (Exception e) {
                violations.addViolations(msg);
            }
        }

        return null;
    }

    public FieldWorker referenceField(FieldWorker collectedBy, ConstraintViolations violations) {
        if (collectedBy == null || collectedBy.getExtId() == null) {
            violations.addViolations("No field worker id provided");
        } else {
            try {
                return fieldWorkerService.findFieldWorkerById(collectedBy.getExtId(), INVALID_FIELD_WORKER_ID);
            } catch (Exception e) {
                violations.addViolations(INVALID_FIELD_WORKER_ID);
            }
        }

        return null;
    }

    public Location referenceField(Location location, ConstraintViolations violations) {
        if (location == null || location.getExtId() == null) {
            violations.addViolations("No location id provided");
            return null;
        } else {
            Location persisted = locationHierarchyService.findLocationById(location.getExtId());
            if (persisted == null) {
                violations.addViolations("Invalid Location Id");
            }

            return persisted;
        }
    }

    public LocationHierarchy referenceField(LocationHierarchy locationLevel, ConstraintViolations violations) {
        if (locationLevel == null || locationLevel.getExtId() == null) {
            violations.addViolations("No location hierarchy level provided");
        } else {
            try {
                return locationHierarchyService.findLocationHierarchyById(locationLevel.getExtId(),
                        "Invalid Location Hierarchy Id");
            } catch (Exception e) {
                violations.addViolations("Invalid Location Hierarchy Id");
            }
        }

        return null;
    }

    public SocialGroup referenceField(SocialGroup socialGroup, ConstraintViolations cv) {
        if (socialGroup == null || socialGroup.getExtId() == null) {
            cv.addViolations("No social group ext id provided");
        } else {
            try {
                return socialGroupService.findSocialGroupById(socialGroup.getExtId(), "Invalid Social Group ext id");
            } catch (Exception e) {
                cv.addViolations("Invalid Social Group ext id");
            }
        }

        return null;
    }

    @Autowired
    public void setVisitService(VisitService visitService) {
        this.visitService = visitService;
    }

    @Autowired
    public void setIndividualService(IndividualService individualService) {
        this.individualService = individualService;
    }

    @Autowired
    public void setFieldWorkerService(FieldWorkerService fieldWorkerService) {
        this.fieldWorkerService = fieldWorkerService;
    }

    @Autowired
    public void setLocationHierarchyService(LocationHierarchyService locationHierarchyService) {
        this.locationHierarchyService = locationHierarchyService;
    }

    @Autowired
    public void setSocialGroupService(SocialGroupService socialGroupService) {
        this.socialGroupService = socialGroupService;
    }
}
