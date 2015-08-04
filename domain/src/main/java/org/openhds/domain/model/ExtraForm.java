package org.openhds.domain.model;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;



@XmlRootElement(name="extraform")
public class ExtraForm {

	String formName;
	List<Data> data = null;
	
	String visitId;
	String roundNumber;
	String locationId;
	String fieldWorkerId;
	String individualId;
	String socialGroupId;

	public List<Data> getData() {
		return data;
	}

	@XmlElement
	public void setData(List<Data> data) {
		this.data = data;
	}
	
	void afterUnmarshal(Unmarshaller u, Object parent){
		for(Data d: data){
			String columnName = d.columnName.toLowerCase();
			if(columnName.startsWith("openhds_") || columnName.startsWith("individual_info_")){				
				if(columnName.equalsIgnoreCase("OPENHDS_VISIT_ID")){
					this.visitId = d.data;
				}
				else if(columnName.equalsIgnoreCase("OPENHDS_ROUND_NUMBER")){
					this.roundNumber = d.data;
				}
				else if(columnName.equalsIgnoreCase("OPENHDS_LOCATION_ID")){
					this.locationId = d.data;
				}
				else if(columnName.equalsIgnoreCase("OPENHDS_FIELD_WORKER_ID")){
					this.fieldWorkerId = d.data;
				}
				else if(columnName.equalsIgnoreCase("INDIVIDUAL_INFO_INDIVIDUAL_ID") 
						|| columnName.equalsIgnoreCase("OPENHDS_INDIVIDUAL_ID")) {
					this.individualId = d.data;
				}
				else if(columnName.equalsIgnoreCase("OPENHDS_HOUSEHOLD_ID")) {
					this.socialGroupId = d.data;
				}
			}
		}
	}

	public String getFormName() {
		return formName;
	}

	@XmlAttribute
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ExtraForm [formName=" + formName + "\n");
		for(Data d: data){
			sb.append(", columnName=" + d.columnName);
			sb.append(", type=" + d.type);
			sb.append(", data=" + d.data);
			sb.append("\n");
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	public String getVisitId() {
		return visitId;
	}

	public String getRoundNumber() {
		return roundNumber;
	}

	public String getLocationId() {
		return locationId;
	}

	public String getFieldWorkerId() {
		return fieldWorkerId;
	}
	
	public String getIndividualId() {
		return individualId;
	}
	
	public String getSocialGroupId() {
		return socialGroupId;
	}
	
	public static class Data{
		@XmlValue
		public String data;
		
		@XmlAttribute
		public String columnName;
		
		@XmlAttribute
		public String type;
	}
	
}
