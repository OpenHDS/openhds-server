package org.openhds.controller.service.impl;

import org.openhds.controller.service.SettingsService;
import org.openhds.domain.model.GeneralSettings;
import org.openhds.domain.service.SitePropertiesService;

public class SettingsServiceImpl implements SettingsService {

	private SitePropertiesService siteProperties;
	
	public SettingsServiceImpl(SitePropertiesService siteProperties){
		this.siteProperties = siteProperties;
	}
	
	@Override
	public GeneralSettings getSettings() {
		GeneralSettings gs = new GeneralSettings();
		int minimumAgeOfParents = siteProperties.getMinimumAgeOfParents();
		int minimumAgeOfHouseholdHead = siteProperties.getMinimumAgeOfHouseholdHead();
		int minMarriageAge = siteProperties.getMinimumAgeOfMarriage();
		int minimumAgeOfPregnancy = siteProperties.getMinimumAgeOfPregnancy();
		
		gs.setMinimumAgeOfParents(minimumAgeOfParents);
		gs.setMinimumAgeOfHouseholdHead(minimumAgeOfHouseholdHead);
		gs.setMinMarriageAge(minMarriageAge);
		gs.setMinimumAgeOfPregnancy(minimumAgeOfPregnancy);
		return gs;
	}
}
