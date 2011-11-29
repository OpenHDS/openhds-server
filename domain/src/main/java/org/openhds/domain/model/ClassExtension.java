package org.openhds.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckInteger;
import org.openhds.domain.constraint.Searchable;

@Description(description="A Class Extension represents modeling a custom attribute for a " +
		"particular entity. If an attribute (class extension) is not supported in the core " +
		"data model i.e. Longitude, Latitude then it can be represented here. ")
@Entity
@Table(name="classExtension")
public class ClassExtension implements Serializable {

	private static final long serialVersionUID = 5217442538717720482L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    String uuid;
  
   	@Enumerated(EnumType.STRING)
   	@Description(description="The target entity that this class extension is for.")
    EntityType entityClass;
  
	@NotNull
	@Searchable
    @CheckFieldNotBlank
    @Description(description="The name given to the class extension.")
    String name;
	
    @Description(description="The description given to the class extension.")
    String description;
    
	@Enumerated(EnumType.STRING)
    @Description(description="The assigned type for the class extension. Even though the " +
    		"actual type persisted is of String, the validation phase will treat it as of the " +
    		"primitive type specified.")
    PrimitiveType primType;
	
	@Description(description="A string of acceptable answers separated by ',' only if the " +
			"the primType is specified as MULTIPLE_CHOICE. Otherwise this value is null.")
	String answers;
	
	@CheckInteger(min=1)
	@Description(description="Round number that the Class Extension is valid from.")
	private Integer roundNumber;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

    public EntityType getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(EntityType entityClass) {
		this.entityClass = entityClass;
	}

	public String getName() {
		return name;
	}
		
	public void setName(String name) {
		this.name = name;
	}

	public PrimitiveType getPrimType() {
		return primType;
	}

	public void setPrimType(PrimitiveType primType) {
		this.primType = primType;
	}
	
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}
	
	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}
	
	public List<String> getExtensionAnswers() {
		List<String> output = new ArrayList<String>();
		String[] array = answers.split(", ");
		
		for (String s : array)
			output.add(s);
		
		return output;
	}
}