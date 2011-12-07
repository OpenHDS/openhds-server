package org.openhds.domain.model;

import org.openhds.domain.annotations.Description;

public class AppSettings {
	
	@Description(description="Version number of the application.")
	public String versionNumber;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
}