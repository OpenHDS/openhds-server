package org.openhds.domain.model;

public enum FormActive {
	BLANK(""),ACTIVE("Active"),NOTACTIVE("Not Active");
	
	private String active;
	
	private FormActive(String active){
		this.setReason(active);
	}

	public String getReason() {
		return active;
	}

	public void setReason(String active) {
		this.active = active;
	}
}
