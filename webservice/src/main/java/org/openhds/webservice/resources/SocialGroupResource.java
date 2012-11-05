package org.openhds.webservice.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.SocialGroup;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/socialgroups")
public class SocialGroupResource {

    private SocialGroupService socialGroupService;
    private FieldBuilder fieldBuilder;

    @Autowired
    public SocialGroupResource(SocialGroupService socialGroupService, FieldBuilder fieldBuilder) {
        this.socialGroupService = socialGroupService;
        this.fieldBuilder = fieldBuilder;
    }

    @XmlRootElement
    public static class SocialGroups {

        private List<SocialGroup> socialGroups;

        @XmlElement(name = "socialgroup")
        public List<SocialGroup> getSocialGroups() {
            return socialGroups;
        }

        public void setSocialGroups(List<SocialGroup> socialGroups) {
            this.socialGroups = socialGroups;
        }

    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SocialGroups getAllSocialGroups() {
        List<SocialGroup> allSocialGroups = socialGroupService.getAllSocialGroups();
        List<SocialGroup> copies = new ArrayList<SocialGroup>();

        for (SocialGroup sg : allSocialGroups) {
            SocialGroup copy = copySocialGroup(sg);
            copies.add(copy);
        }

        SocialGroups sgs = new SocialGroups();
        sgs.setSocialGroups(copies);

        return sgs;
    }

    private SocialGroup copySocialGroup(SocialGroup sg) {
        SocialGroup copy = new SocialGroup();
        copy.setExtId(sg.getExtId());

        Individual groupHead = new Individual();
        groupHead.setExtId(sg.getGroupHead().getExtId());
        copy.setGroupHead(groupHead);
        copy.setGroupName(sg.getGroupName());
        return copy;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends Serializable> insert(@RequestBody SocialGroup socialGroup) {
        ConstraintViolations cv = new ConstraintViolations();

        socialGroup.setCollectedBy(fieldBuilder.referenceField(socialGroup.getCollectedBy(), cv));
        socialGroup.setGroupHead(fieldBuilder.referenceField(socialGroup.getGroupHead(), cv,
                "Invalid Ext Id for Group Head"));

        if (cv.hasViolations()) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        try {
            socialGroupService.createSocialGroup(socialGroup);
        } catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SocialGroup>(copySocialGroup(socialGroup), HttpStatus.CREATED);
    }

}
