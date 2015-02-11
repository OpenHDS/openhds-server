package org.openhds.controller.service;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.GeneralSettings;
import org.openhds.domain.model.PrivilegeConstants;

public interface SettingsService {

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	GeneralSettings getSettings();
}
