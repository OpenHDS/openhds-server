package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Past;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckIndividualGenderFemale;
import org.openhds.domain.constraint.CheckIndividualGenderMale;
import org.openhds.domain.constraint.CheckIndividualParentAge;
import org.openhds.domain.constraint.CheckMotherFatherNotIndividual;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.extensions.ExtensionConstraint;

@Description(description="An Individual represents one who is a part of the study. " +
		"Each Individual is identified by a uniquely generated external identifier which " +
		"the system uses internally. Information about the Individual such as name, gender, " +
		"date of birth, and parents are stored here. An Individual may be associated with many " +
		"Residencies, Relationships, and Memberships.")
@CheckMotherFatherNotIndividual
@Entity
@Table(name="individual")
public class Individual extends AuditableCollectedEntity implements Serializable {
    public Individual() {
		super();
		allResidencies = new HashSet<Residency>();
	}
    
    public Individual(String firstName, String lastName, String genderType) {
		this.firstName = firstName;
		this.lastName = lastName;	
		this.gender = genderType;
    }
       
    private static final long serialVersionUID = 9058114832143454609L;
      
    @Searchable
    @Column(nullable = false, unique = true)
    @Description(description="External Id of the individual. This id is used internally.")
    String extId;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="First name of the individual.")
    String firstName;
    
    @Searchable
    @Description(description="Middle name of the individual.")
    String middleName;
    
    @CheckFieldNotBlank
    @Searchable
    @Description(description="Last name of the individual.")
    String lastName;

    @ExtensionConstraint(constraint="genderConstraint", message="Invalid Value for gender", allowNull=true)
    @Description(description="Gender of the individual.")
    String gender;
        
    @Past(message="Date of birth must a date in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Birth date of the individual.")
    Calendar dob;
       
	// Gender seems like a reasonable constraint to enforce
    // But since there is a predefine unknown Individual, there is no way
    // currently to masquerade this unknown individual as both male and female
    @CheckIndividualGenderFemale(allowNull = true, message="The mother specified is not female gender") 
    @CheckIndividualParentAge(allowNull = true,message="The mother is younger than the minimum age required in order to be a parent")
    @CheckEntityNotVoided(allowNull = true,message="The mother has been voided")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Individual.class)
    @Description(description="The individual's mother, identified by the external id.")
    Individual mother;
   
    @CheckIndividualGenderMale(allowNull = true, message="The father specified is not male gender")
    @CheckIndividualParentAge(allowNull = true, message="The father is younger than the minimum age required in order to be a parent")
    @CheckEntityNotVoided(allowNull = true,message="The father has been voided")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Individual.class)
    @Description(description="The individual's father, identified by the external id.")
    Individual father;
    
    @ExtensionConstraint(constraint="dobAspectConstraint", message="Invalid Value for partial date", allowNull=true)
    @Description(description="Identifer for determining if the birth date is partially known.")
    String dobAspect;
    
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="individual")
    @OrderBy("startDate")
    @Description(description="The set of all residencies that the individual may have.")
    Set<Residency> allResidencies;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="individualA")
    @Description(description="The set of all relationships that the individual may have with another individual.")
    Set<Relationship> allRelationships1 = new HashSet<Relationship>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="individualB")
    @Description(description="The set of all relationships where another individual may have with this individual.")
    Set<Relationship> allRelationships2 = new HashSet<Relationship>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy="individual")
    @Description(description="The set of all memberships the individual is participating in.")
    Set<Membership> allMemberships = new HashSet<Membership>();
	
    public String getDobAspect() {
		return dobAspect;
	}

    public void setDobAspect(String dobAspect) {
        this.dobAspect = dobAspect;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
    }
   
    public Individual getMother() {
        return mother;
    }

    public void setMother(Individual mother) {
        this.mother = mother;
    }

    public Individual getFather() {
        return father;
    }

    public void setFather(Individual father) {
        this.father = father;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }
    
    /**
     * The Set of residencies is annotated with OrderBy on start date
     * This annotation will order the set of residencies for this individual by start
     * date of residency in ascending order 
     * 
     * @return the residency with the most recent start date
     */
    public Residency getCurrentResidency() {
        if (this.allResidencies.size() == 0) {
        	return null;
        }
        
        // since the set of residencies are ordered by start date in ascending
        // order, iterating over all the residencies and grabbing the last one
        // should in theory be the most recent residency
        Iterator<Residency> itr = allResidencies.iterator();
        Residency residency = null;
        
        while(itr.hasNext()) {
        	residency = itr.next();
        }
        
        return residency;
    }

    public void setCurrentResidency(Residency currentResidency) {
        allResidencies.add(currentResidency);
    }
    
    public void setAllRelationships1(Set<Relationship> allRelationships) {
        this.allRelationships1 = allRelationships;
    }

    public Set<Relationship> getAllRelationships1() {
        return allRelationships1;
    }
    
    public void setAllRelationships2(Set<Relationship> allRelationships) {
        this.allRelationships2 = allRelationships;
    }

    public Set<Relationship> getAllRelationships2() {
        return allRelationships2;
    }
    
    public Set<Relationship> getAllRelationships() {
    	Set<Relationship> all = new HashSet<Relationship>(getAllRelationships2()) ;
    	all.addAll(allRelationships1);
    	return all;
    }
 
    public Set<Membership> getAllMemberships() {
        return allMemberships;
    }

    public void setAllMemberships(Set<Membership> allMemberships) {
        this.allMemberships = allMemberships;
    }
    
    public Set<Residency> getAllResidencies() {
        return allResidencies;
    }

    public void setAllResidencies(Set<Residency> allResidencies) {
        this.allResidencies = allResidencies;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    
    // get total Person Days of Observation
	public Long getPD(Calendar start, Calendar end){
    	Long pd = 0L;
    	if (this.getAllResidencies().size() > 0) {
			for (Residency r : this.getAllResidencies()) {
				try {
					pd += r.findDuration(start, end);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return pd;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Individual))
			return false;
		Individual other = (Individual) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender != other.gender)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
