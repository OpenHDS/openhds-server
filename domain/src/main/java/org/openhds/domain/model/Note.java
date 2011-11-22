package org.openhds.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;

@Description(description="A note represents any comments the Field Worker may have during " +
		"a round. A note could contain any modifications that must be made to the database " +
		"such as a name change. This information is reviewed by the Data Manager who will " +
		"make the modifications to the system. ")
@Entity
@Table(name="note")
public class Note extends AuditableCollectedEntity implements Serializable {

	private static final long serialVersionUID = -805712653733682858L;
    
    @CheckFieldNotBlank
    @Description(description="The note description.")
    String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
