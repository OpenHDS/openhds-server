package org.openhds.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that identifies any of the core entities by it's id. 
 * This is used in web service calls to retrieve a list
 * of entities by their ids.
 */

public class ReferencedEntity {
	
	int count;
	List<ReferencedBaseEntity> entity = new ArrayList<ReferencedBaseEntity>();
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public List<ReferencedBaseEntity> getEntity() {
		return entity;
	}
	
	public void setEntity(List<ReferencedBaseEntity> entity) {
		this.entity = entity;
	}
	
	public void increaseCount() {
		count++;
	}
}
