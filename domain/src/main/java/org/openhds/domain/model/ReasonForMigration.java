package org.openhds.domain.model;

public enum ReasonForMigration {
	BLANK(""),AGRICULTURE("AGRICULTURE"),ANIMAL_HUSBANDRY("ANIMAL HUSBANDRY"),NEW_HOUSE("NEW HOUSE"),MARITAL_CHANGE("MARITAL CHANGE"),
	EDUCATION("EDUCATION"),HEALTH_REASONS("HEALTH REASONS"),WORK("WORK"),OTHER("OTHER");
	
	private String reason;
	
	private ReasonForMigration(String reason){
		this.setReason(reason);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
