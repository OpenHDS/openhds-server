package org.openhds.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeneralSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6338817073217742486L;
	private int minimumAgeOfParents;	
	private int minimumAgeOfHouseholdHead;
	private int minMarriageAge;	
	private int minimumAgeOfPregnancy;
	private String visitAt;
	private String earliestEventDate;
	private SyncEntities entities;

	@XmlElement(name = "minMarriageAge")
	public int getMinMarriageAge() {
		return minMarriageAge;
	}

	public void setMinMarriageAge(int minMarriageAge) {
		this.minMarriageAge = minMarriageAge;
	}

	@XmlElement(name = "minAgeOfParents")
	public int getMinimumAgeOfParents() {
		return minimumAgeOfParents;
	}

	public void setMinimumAgeOfParents(int minimumAgeOfParents) {
		this.minimumAgeOfParents = minimumAgeOfParents;
	}

	@XmlElement(name = "minAgeOfHouseholdHead")
	public int getMinimumAgeOfHouseholdHead() {
		return minimumAgeOfHouseholdHead;
	}

	public void setMinimumAgeOfHouseholdHead(int minimumAgeOfHouseholdHead) {
		this.minimumAgeOfHouseholdHead = minimumAgeOfHouseholdHead;
	}

	@XmlElement(name = "minAgeOfPregnancy")
	public int getMinimumAgeOfPregnancy() {
		return minimumAgeOfPregnancy;
	}

	public void setMinimumAgeOfPregnancy(int minimumAgeOfPregnancy) {
		this.minimumAgeOfPregnancy = minimumAgeOfPregnancy;
	}
	
	@XmlElement(name = "visitLevel")
	public String getVisitAt(){
		return this.visitAt;
	}
	
	public void setVisitAt(String visitAt){
		this.visitAt = visitAt;
	}

	@XmlElement(name = "earliestEventDate")
	public String getEarliestEventDate() {
		return earliestEventDate;
	}

	public void setEarliestEventDate(String earliestEventDate) {
		this.earliestEventDate = earliestEventDate;
	}
	
	@XmlElement(name = "entities")
	public SyncEntities getSyncEntities(){
		return this.entities;
	}
	
	public void setSyncEntities(SyncEntities entities){
		this.entities = entities;
	}
	

	@XmlRootElement(name="entites")
	public static class SyncEntities{
		private List<SyncEntity> syncEntityList;
		
		public List<SyncEntity> getEntityCount() {
			return this.syncEntityList;
		}

		@XmlElement(name="entity")
		public void setEntityCount(List<SyncEntity> syncEntityList) {
			this.syncEntityList = syncEntityList;
		}
		
		public SyncEntities(List<SyncEntity> syncEntityList){
			this.syncEntityList = syncEntityList;
		}
		
		private SyncEntities(){}
	}
	
	@XmlRootElement(name="entity")
	public static class SyncEntity{
		
		private String name;
		private int count;
		
		private SyncEntity(){}
		
		public SyncEntity(String entityName, int entityCount){
			setName(entityName);
			this.count = entityCount;
		}
		
		@XmlAttribute(name="name")
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name.replaceAll("\\s+","").replaceAll("Task","");
		}
		
		@XmlAttribute(name="count")
		public int getCount(){
			return this.count;
		}
		
		public void setCount(int count){
			this.count = count;
		}
	}
	
}
