package org.openhds.web.util;

public class Locale {
	
	String locale;
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String setSwahili() {
		this.locale = "Swahili";
		return null;
	}
	
	public String setEnglish() {
		this.locale = "English";
		return null;
	}
}
