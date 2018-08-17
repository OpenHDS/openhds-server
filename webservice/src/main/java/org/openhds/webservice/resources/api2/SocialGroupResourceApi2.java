package org.openhds.webservice.resources.api2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.IndividualService;
import org.openhds.controller.service.SocialGroupService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.wrappers.Locations;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/socialgroups2")
public class SocialGroupResourceApi2 {
	private static final Logger logger = LoggerFactory.getLogger(SocialGroupResourceApi2.class);

	private SocialGroupService socialGroupService;
	private IndividualService individualService;
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
		sgs.setTimestamp(new Date().getTime());

		return sgs;
	}

	@RequestMapping(value="/pushUpdates", method = RequestMethod.PUT, consumes= {"application/json"}, produces = { "application/json" })
	public ResponseEntity<? extends Serializable> pushUpdate(@RequestBody SocialGroups socialGroups) throws Exception {
		long lastClientUpdate = socialGroups.getTimestamp();
		long time = new Date().getTime();

		ConstraintViolations cv = new ConstraintViolations();

		for(SocialGroup group: socialGroups.getSocialGroups()) {
			try {
				group.setCollectedBy(fieldBuilder.referenceField(group.getCollectedBy(), cv));
				boolean indivExists = false;
				Individual ind = null;

				ind =fieldBuilder.referenceField(group.getGroupHead(), cv, "Individual doesn't exist");
				if(ind != null) {
					indivExists = true;
				}


				if(!indivExists) {
					this.individualService.createIndividual(group.getGroupHead());
				}

				group.setGroupHead(fieldBuilder.referenceField(group.getGroupHead(), cv, "Group head doesn't exist."));
				group.setServerUpdateTime(new Date().getTime());
				this.update(group, lastClientUpdate, time);
			} catch(ConstraintViolations e){
				return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.EXPECTATION_FAILED);
			}
		}


		return new ResponseEntity<>(time, HttpStatus.ACCEPTED);
	}

	public void update(SocialGroup socialGroup, long currentTimestamp, long lastClientUpdate) throws Exception {

		boolean doesExist = false;
		SocialGroup sg = null;
		try {
			sg = socialGroupService.findSocialGroupById(socialGroup.getExtId(), "");
			if(sg != null) {
				doesExist = true;
			}
		} catch(Exception e) {

		}

		if(!doesExist) {
			this.insert(socialGroup);
		} 
		else if(doesExist){
			//Set UUID to prevent assignment of a new one.
			socialGroup.setUuid(socialGroupService.findSocialGroupById(socialGroup.getExtId(),"").getUuid());
			if(lastClientUpdate > currentTimestamp) {
				//Convert timestamp to calendar instance
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(currentTimestamp);
				socialGroup.setInsertDate(c);
			}
			this.socialGroupService.updateSocialGroup(socialGroup);
		}
		else if(socialGroup.isDeleted()) {
			if(socialGroup.getUuid() == null)
				throw new ConstraintViolations("The social group uuid is null.");

			this.socialGroupService.updateSocialGroup(socialGroup);
		} 
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

	@RequestMapping(method = RequestMethod.GET, value = "/pull/{timestamp}", produces = "application/json")
	public ResponseEntity<SocialGroups> getUpdatedLocations(@PathVariable long timestamp) {
		long time = new Date().getTime();

		List<SocialGroup> socialGroup = socialGroupService.getAllSocialGroups();
		List<SocialGroup> copies = new ArrayList<SocialGroup>(socialGroup.size());

		for (SocialGroup group : socialGroup) {
			long compTime;
			if(group.getServerUpdateTime() == 0)
				compTime = group.getServerInsertTime();
			else 
				compTime = group.getServerUpdateTime();

			if(timestamp <= compTime && timestamp < time) {
				copies.add(JsonShallowCopier.copySocialGroup(group));
			}
		}
		SocialGroups all = new SocialGroups();
		ArrayList<SocialGroup> allSocialGroups = new ArrayList<SocialGroup>();
		allSocialGroups.addAll(copies);
		all.setSocialGroups(allSocialGroups);
		all.setTimestamp(time);
		return new ResponseEntity<SocialGroups>(all, HttpStatus.ACCEPTED);
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
