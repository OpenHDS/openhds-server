package org.openhds.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.Searchable;

@Description(description="A Field Worker represents one who collects the data within " +
		"the study area. They can be identified by a uniquely generated " +
		"identifier which the system uses internally. Only the first and last names " +
		"are recorded.")
@Entity
@Table(name="fieldworker")
public class FieldWorker extends AuditableEntity implements Serializable {

    private static final long serialVersionUID = 1898036206514199266L;
      
    @NotNull
    @CheckFieldNotBlank
    @Searchable
    @Description(description="External Id of the field worker. This id is used internally.")
    String extId;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="First name of the field worker.")
    String firstName;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="Last name of the field worker.")
    String lastName;

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
