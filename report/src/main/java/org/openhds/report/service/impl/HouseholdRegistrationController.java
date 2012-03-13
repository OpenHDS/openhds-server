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
import org.openhds.report.beans.HouseholdRegisterBean;
import org.openhds.report.service.HouseholdRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HouseholdRegistrationController implements HouseholdRegistrationService {
	GenericDao genericDao;
	ResidencyService residencyService;
	LocationHierarchyService locationService;
	SitePropertiesService properties;
	CalendarUtil calendarUtil;
	
	@Autowired
	public HouseholdRegistrationController(GenericDao genericDao, ResidencyService residencyService, 
			LocationHierarchyService locationService, SitePropertiesService properties, CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
		this.residencyService = residencyService;
		this.locationService = locationService;
		this.properties = properties;
		this.calendarUtil = calendarUtil;
	}
	
	@RequestMapping(value="/household-register.report")
	public ModelAndView getHouseholdRegistrationBook() {
		ModelAndView mv = new ModelAndView("householdRegister");
		mv.addObject("myData", getHouseholds());
		return mv;
	}
	
	private Collection<HouseholdRegisterBean> getHouseholds() {
		Collection<HouseholdRegisterBean> beans = new ArrayList<HouseholdRegisterBean>();
		
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
		
		// for each location grab the individuals who have their last known residency their
		for(Location loc : locations) {
			List<Individual> individuals = residencyService.getIndividualsByLocation(loc);
						
			for(Individual individual : individuals) {
				HouseholdRegisterBean bean = new HouseholdRegisterBean();
				bean.setLowestLevelName(lowestLevel.getName());
				bean.setLowestLevelValue(loc.getLocationLevel().getName());
				bean.setLocationId(loc.getExtId());
				bean.setFamilyName(loc.getLocationName());
				bean.setDob(calendarUtil.formatDate(individual.getDob()));
				bean.setFather(individual.getFather().getExtId());
				bean.setGender(individual.getGender());
				bean.setIndividualId(individual.getExtId());
				bean.setMother(individual.getMother().getExtId());
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

