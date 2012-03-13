package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.controller.service.ResidencyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.OrderProperty;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchyLevel;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.IdentificationBookBean;
import org.openhds.report.service.IdentificationBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IdentificationBookController implements IdentificationBookService {
	
	GenericDao genericDao;
	SitePropertiesService properties;
	ResidencyService residencyService;
	LocationHierarchyService locationService;
	CalendarUtil calendarUtil;
	
	@Autowired
	public IdentificationBookController(GenericDao genericDao, SitePropertiesService properties,  
			ResidencyService residencyService, LocationHierarchyService locationService,
			CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
		this.properties = properties;
		this.residencyService = residencyService;
		this.locationService = locationService;
		this.calendarUtil = calendarUtil;
	}
	
	@RequestMapping(value="/identification-book.report")
	public ModelAndView getHouseholdRegistrationBook() {
		ModelAndView mv = new ModelAndView("identificationBook");
		mv.addObject("myData", getIds());
		return mv;
	}
	
	private Collection<IdentificationBookBean> getIds() {
		Collection<IdentificationBookBean> beans = new ArrayList<IdentificationBookBean>();
		
		// retrieve all locations
		// setup order properties
		OrderProperty locLevel = new OrderProperty() {

			public String getPropertyName() {
				return "locationLevel";
			}

			public boolean isAscending() {
				return true;
			}	
		};
		
		OrderProperty locExtId = new OrderProperty() {

			public String getPropertyName() {
				return "extId";
			}

			public boolean isAscending() {
				return true;
			}	
		};
		
		List<Location> locations = genericDao.findAllWithOrder(Location.class, locLevel, locExtId);
		LocationHierarchyLevel lowestLevel = locationService.getLowestLevel();
		
		// for each location grab the individuals who have their last known residency there
		for(Location loc : locations) {
			List<Individual> individuals = residencyService.getIndividualsByLocation(loc);
			
			for(Individual individual : individuals) {
				IdentificationBookBean bean = new IdentificationBookBean();
				bean.setLowestLevelName(lowestLevel.getName());
				bean.setLowestLevelValue(loc.getLocationLevel().getName());
				bean.setLocExtId(loc.getExtId());
				bean.setDob(calendarUtil.formatDate(individual.getDob()));
				bean.setGender(individual.getGender().toString());
				bean.setIndivExtId(individual.getExtId());
				String name = individual.getFirstName();
				name += (individual.getMiddleName() == null ? "" : " " + individual.getMiddleName());
				name += " " + individual.getLastName();
				bean.setName(name);
				beans.add(bean);				
			}
		}
		return beans;
	}
}

