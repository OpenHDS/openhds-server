package org.openhds.service;

public interface SiteConfigService {

	void setVisitIdLength(int visitIdLength);
	int getVisitIdLength();
	
	String getVisitAt();
	void setVisitAt(String visitAt);
}
