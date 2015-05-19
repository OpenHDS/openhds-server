package org.openhds.controller.service.impl;

import org.openhds.controller.service.SettingsService;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.GeneralSettings;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.service.SitePropertiesService;

public class SettingsServiceImpl implements SettingsService {

	private SitePropertiesService siteProperties;
	
	public SettingsServiceImpl(SitePropertiesService siteProperties){
		this.siteProperties = siteProperties;
	}
	
	@Override
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	public GeneralSettings getSettings() {
		GeneralSettings gs = new GeneralSettings();
		int minimumAgeOfParents = siteProperties.getMinimumAgeOfParents();
		int minimumAgeOfHouseholdHead = siteProperties.getMinimumAgeOfHouseholdHead();
		int minMarriageAge = siteProperties.getMinimumAgeOfMarriage();
		int minimumAgeOfPregnancy = siteProperties.getMinimumAgeOfPregnancy();
		String visitAt = siteProperties.getVisitAt();
		
		gs.setMinimumAgeOfParents(minimumAgeOfParents);
		gs.setMinimumAgeOfHouseholdHead(minimumAgeOfHouseholdHead);
		gs.setMinMarriageAge(minMarriageAge);
		gs.setMinimumAgeOfPregnancy(minimumAgeOfPregnancy);
		gs.setVisitAt(visitAt);
		return gs;
	}
}
