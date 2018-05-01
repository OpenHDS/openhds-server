package org.openhds.webservice.resources.api2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.wrappers.SocialGroups;
import org.openhds.domain.util.JsonShallowCopier;
import org.openhds.domain.util.ShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.CacheResponseWriter;
import org.openhds.webservice.FieldBuilder;
import org.openhds.webservice.WebServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/socialgroups2")
public class SocialGroupResourceApi2 {
    private static final Logger logger = LoggerFactory.getLogger(SocialGroupResourceApi2.class);

    private SocialGroupService socialGroupService;
    private FieldBuilder fieldBuilder;
    private FileResolver fileResolver;

    @Autowired
    public SocialGroupResourceApi2(SocialGroupService socialGroupService, FieldBuilder fieldBuilder,
            FileResolver fileResolver) {
        this.socialGroupService = socialGroupService;
        this.fieldBuilder = fieldBuilder;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SocialGroups getAllSocialGroups() {
        List<SocialGroup> allSocialGroups = socialGroupService.getAllSocialGroups();
        List<SocialGroup> copies = new ArrayList<SocialGroup>();

        for (SocialGroup sg : allSocialGroups) {
            SocialGroup copy = JsonShallowCopier.copySocialGroup(sg);
            copies.add(copy);
        }

        SocialGroups sgs = new SocialGroups();
        sgs.setSocialGroups(copies);

        return sgs;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<? extends Serializable> insert(@RequestBody SocialGroup socialGroup) {
        ConstraintViolations cv = new ConstraintViolations();

        socialGroup.setCollectedBy(fieldBuilder.referenceField(socialGroup.getCollectedBy(), cv));
        socialGroup.setGroupHead(fieldBuilder.referenceField(socialGroup.getGroupHead(), cv,
                "Invalid Ext Id for Group Head"));

        if (cv.hasViolations()) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(cv), HttpStatus.BAD_REQUEST);
        }

        try {
            socialGroupService.createSocialGroup(socialGroup, null);
        } catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SocialGroup>(ShallowCopier.copySocialGroup(socialGroup), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET)
    public void getCachedSocialGroups(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolvesocialGroupXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing social group xml file: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/zipped", method = RequestMethod.GET)
    public void getZippedSocialGroups(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolvesocialGroupZipFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing social group zip file: " + e.getMessage());
        }
    }

}
