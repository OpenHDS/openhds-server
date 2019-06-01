package org.openhds.service.impl;


import org.openhds.idgeneration.IdScheme;
import org.openhds.service.SiteConfigService;

public class SiteConfigServiceImpl implements SiteConfigService {
	
	private String propertyFile;
	private int visitIdLength = -1;
	private IdScheme visitIdScheme;
	private String visitLevel = "location";
	
	public void setPropertyFile(String propertyFile){
		this.propertyFile = propertyFile;
	}
	
	public String getPropertyFile(){
		return this.propertyFile;
	}
	
	public int getVisitIdLength(){
		return this.visitIdLength;
	}
	
	public void setVisitIdLength(int visitIdLength){			
		if(this.visitIdLength == -1){
			this.visitIdLength = visitIdLength;
			return;
		}
		else if(visitIdScheme != null){
			this.visitIdLength = visitIdLength;
			visitIdScheme.setLength(this.visitIdLength);
		}
	}
	
	public IdScheme getVisitIdScheme(){
		return this.visitIdScheme;
	}
	
	public void setVisitIdScheme(IdScheme visitIdScheme){
		this.visitIdScheme = visitIdScheme;		
	}

	@Override
	public String getVisitAt() {
		return this.visitLevel;
	}

	@Override
	public void setVisitAt(String visitAt) {
		this.visitLevel = visitAt;
	}
}
