package org.openhds.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEndDateAndEndEventType;
import org.openhds.domain.constraint.CheckEndDateGreaterThanStartDate;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckFieldNotBlank;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.CheckStartDateGreaterThanBirthDate;
import org.openhds.domain.constraint.GenericEndDateEndEventConstraint;
import org.openhds.domain.constraint.GenericStartEndDateConstraint;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.value.extension.ExtensionConstraint;

@Description(description="A Membership represents an Individual's association with a " +
		"particular Social Group. Memberships are identified by a uniquely generated " +
		"identifier which the system uses internally. It contains information " +
		"about the date the Membership started and ended, as well as the start and end types. " +
		"It also contains the Individual's relationship to the head of the Social Group.")
@Entity
@CheckEndDateGreaterThanStartDate(allowNull=true)
@CheckStartDateGreaterThanBirthDate
@CheckEndDateAndEndEventType
@Table(name="membership")
//@XmlRootElement(name = "membership")
public class Membership extends AuditableCollectedEntity implements GenericEndDateEndEventConstraint, GenericStartEndDateConstraint, Serializable {
	
	private static final long serialVersionUID = 6200055042380700627L;
		
	@Searchable
    @CheckEntityNotVoided
    @CheckIndividualNotUnknown
	@ManyToOne
	@Description(description="Individual the membership is associated with, identified by external id.")
	Individual individual;
	
	@Searchable
	@ManyToOne
	@Description(description="The social group of the membership, identified by external id.")
	SocialGroup socialGroup;
	
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Start date of the membership.")
	Calendar startDate;
    
    @CheckFieldNotBlank
    @Description(description="Start type of the membership.")
	String startType;
	
    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="End date of the membership.")
	Calendar endDate;
    
    @Description(description="End type of the membership.")
	String endType;
    
    @ExtensionConstraint(constraint="membershipConstraint", message="Invalid Value for membership relation to head", allowNull=true)
    @Description(description="Relationship type to the group head.")
    String bIsToA;
	
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public SocialGroup getSocialGroup() {
		return socialGroup;
	}

	public void setSocialGroup(SocialGroup socialGroup) {
		this.socialGroup = socialGroup;
	}

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public String getStartType() {
		return startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

    //@XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getEndType() {
		return endType;
	}

	public void setEndType(String endType) {
		this.endType = endType;
	}
	
	public String getbIsToA() {
		return bIsToA;
	}

	public void setbIsToA(String bIsToA) {
		this.bIsToA = bIsToA;
	}
}
