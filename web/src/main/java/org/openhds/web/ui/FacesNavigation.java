package org.openhds.web.ui;

public class FacesNavigation {
	
	private String navigateTo;

	public boolean setNavigateTo(String navigateTo) {
		this.navigateTo = navigateTo;
		
		return true;
	}

	public String getNavigateTo() {
		// need to check if user clicked on a regular jsf amendment form
		// or a web flow
		if (navigateTo.indexOf("flow") > 0) {
			// user clicked a flow link - just need to replace the _ with a .
			return navigateTo.replace('_', '.');
		} else {
			// user clicked a regular amendment form link
			return navigateTo.replace('_', '/') + ".faces";
		}
	}	
}
