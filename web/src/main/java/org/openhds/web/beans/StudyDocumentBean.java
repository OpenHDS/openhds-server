package org.openhds.web.beans;

public class StudyDocumentBean {
	
	String studyName;
	String studyId;
	String studyStartDate;
	String studyEndDate;
	String affiliation;
	String author;
	String email;
	String nationAbrv;
	String notes;
	
	public String getStudyName() {
		return studyName;
	}
	
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	
	public String getStudyId() {
		return studyId;
	}
	
	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	
	public String getStudyStartDate() {
		return studyStartDate;
	}
	
	public void setStudyStartDate(String studyStartDate) {
		this.studyStartDate = studyStartDate;
	}
	
	public String getStudyEndDate() {
		return studyEndDate;
	}

	public void setStudyEndDate(String studyEndDate) {
		this.studyEndDate = studyEndDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAffiliation() {
		return affiliation;
	}
	
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
		
	public String getNationAbrv() {
		return nationAbrv;
	}
	
	public void setNationAbrv(String nationAbrv) {
		this.nationAbrv = nationAbrv;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
