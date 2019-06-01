package org.openhds.service;

import org.openhds.annotations.Authorized;
import org.openhds.domain.GeneralSettings;
import org.openhds.domain.PrivilegeConstants;

public interface SettingsService {

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	GeneralSettings getSettings();
}
