package org.openhds.domain;

public class IndividualMerge {

	Individual primary;
	
	Individual toMergeFrom;
	
	boolean mergeInMigrations, mergeMemberships;
	
	public Individual getPrimary() {
		return primary;
	}

	public void setPrimary(Individual primary) {
		this.primary = primary;
	}

	public Individual getToMergeFrom() {
		return toMergeFrom;
	}

	public void setToMergeFrom(Individual toMergeFrom) {
		this.toMergeFrom = toMergeFrom;
	}

	public boolean isMergeInMigrations() {
		return mergeInMigrations;
	}

	public void setMergeInMigrations(boolean mergeInMigrations) {
		this.mergeInMigrations = mergeInMigrations;
	}

	public boolean isMergeMemberships() {
		return mergeMemberships;
	}

	public void setMergeMemberships(boolean mergeMemberships) {
		this.mergeMemberships = mergeMemberships;
	}
}
