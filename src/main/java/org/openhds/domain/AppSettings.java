package org.openhds.domain;

import org.openhds.annotations.Description;

public class AppSettings {
	
	@Description(description="Version number of the application.")
	String versionNumber;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
}