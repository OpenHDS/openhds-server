package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEndDateAndEndEventType;
import org.openhds.domain.constraint.CheckEndDateGreaterThanStartDate;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckGenderOfRelationship;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.CheckRelatedIndividuals;
import org.openhds.domain.constraint.CheckRelationshipAge;
import org.openhds.domain.constraint.GenericEndDateEndEventConstraint;
import org.openhds.domain.constraint.GenericStartEndDateConstraint;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.value.extension.ExtensionConstraint;

@Description(description="A Relationship is used to associate an Individual " +
		"with another Indivual in some way. It can be identified by a uniquely " +
		"generated identifier which the system uses internally. It contains " +
		"information about the two Indivuals involved, the start and end dates, " +
		"and the start and end types of the Relationship.")
@Entity
@CheckGenderOfRelationship
@CheckRelatedIndividuals
@CheckEndDateGreaterThanStartDate(allowNull=true)
@CheckEndDateAndEndEventType
@Table(name="relationship")
public class Relationship extends AuditableCollectedEntity implements GenericEndDateEndEventConstraint, GenericStartEndDateConstraint, Serializable {
    static final long serialVersionUID = 19L;
        
    @Searchable
    @CheckEntityNotVoided
    @CheckIndividualNotUnknown
    @CheckRelationshipAge(allowNull = false, message="Individual A is younger than the minimum age required in order to be in a relationship")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Description(description="One of the individuals participating in the relationship, identified by external id.")
    Individual individualA;
    
    @Searchable
    @CheckEntityNotVoided
    @CheckIndividualNotUnknown
    @CheckRelationshipAge(allowNull = false, message="Individual B is younger than the minimum age required in order to be in a relationship")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Description(description="One of the individuals participating in the relationship, identified by external id.")
    Individual individualB;
    
    @CheckFieldNotBlank
    @ExtensionConstraint(constraint="maritalStatusConstraint", message="Invalid Value for relationship type",allowNull=false)
    @Description(description="Relationship type.")
    String aIsToB;
       
	@NotNull
    @Past(message = "Start date should be in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Start date of the relationship.")
    Calendar startDate;
    
    @Past(message = "End date should be in the past")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="End date of the relationship.")
    Calendar endDate;
    
    @Description(description="End type of the relationship.")
    String endType;

    public Individual getIndividualA() {
        return individualA;
    }

    public Individual getIndividualB() {
        return individualB;
    }

    public void setIndividualB(Individual individualB) {
        this.individualB = individualB;
    }

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

   // @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public void setIndividualA(Individual individualA) {
        this.individualA = individualA;
    }

    public String getEndType() {
		return endType;
	}

	public void setEndType(String endType) {
		this.endType = endType;
	}

	public String getaIsToB() {
		return aIsToB;
	}

	public void setaIsToB(String aIsToB) {
		this.aIsToB = aIsToB;
	}
}
