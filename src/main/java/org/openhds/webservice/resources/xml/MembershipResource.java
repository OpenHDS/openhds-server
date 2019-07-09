package org.openhds.webservice.resources.xml;

import java.io.Serializable;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.MembershipService;
import org.openhds.domain.Membership;
import org.openhds.webservice.FieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/memberships")
public class MembershipResource extends AbstractResource<Membership> {

    private MembershipService membershipService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public MembershipResource(MembershipService membershipService, FieldBuilder fieldBuilder) {
        this.membershipService = membershipService;
        this.fieldBuilder = fieldBuilder;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/xml")
    public ResponseEntity<? extends Serializable> insert(@RequestBody Membership membership) {
        ResponseEntity<? extends Serializable> response = createResource(membership);

        return response;
    }

    @Override
    protected Membership copy(Membership item) {
        Membership copy = new Membership();

        copy.setbIsToA(item.getbIsToA());
        copy.setCollectedBy(copyFieldWorker(item.getCollectedBy()));
        copy.setEndDate(item.getEndDate());
        copy.setEndType(item.getEndType());
        copy.setIndividual(copyIndividual(item.getIndividual()));
        copy.setSocialGroup(copySocialGroup(item.getSocialGroup()));
        copy.setStartDate(item.getStartDate());
        copy.setStartType(item.getStartType());

        return copy;
    }

    @Override
    protected void saveResource(Membership item) throws ConstraintViolations {
        membershipService.createMembership(item);
    }

    @Override
    protected void setReferencedFields(Membership item, ConstraintViolations cv) {
        item.setCollectedBy(fieldBuilder.referenceField(item.getCollectedBy(), cv));
        item.setIndividual(fieldBuilder.referenceField(item.getIndividual(), cv, "Membership Individual id not valid"));
        item.setSocialGroup(fieldBuilder.referenceField(item.getSocialGroup(), cv));
    }

}
