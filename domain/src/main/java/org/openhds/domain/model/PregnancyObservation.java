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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckIndividualGenderFemale;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.util.CalendarAdapter;

@Description(description="A Pregnancy Observation is used to monitor a " +
		"pregnancy. It contains information about the mother who is pregnant, " +
		"the date the pregnancy started, as well as the expected delivery date.")
@Entity
@Table(name="pregnancyobservation")
@XmlRootElement(name = "pregnancyobservation")
public class PregnancyObservation extends AuditableCollectedEntity implements Serializable {

	private static final long serialVersionUID = -4737117368371754337L;
		
	@Searchable
	@CheckIndividualNotUnknown
	@CheckIndividualGenderFemale(allowNull = true) 
    @CheckEntityNotVoided(allowNull = true)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Individual.class)
	@Description(description="The mother of the pregnancy observation, identified by the external id.")
    Individual mother;
	
	@NotNull
	@Temporal(javax.persistence.TemporalType.DATE)
	@Description(description="Expected delivery date.")
	Calendar expectedDeliveryDate;
	
	@Past
	@NotNull
	@Temporal(javax.persistence.TemporalType.DATE)
	@Description(description="Recorded date of the pregnancy observation.")
	Calendar recordedDate;
	
	@ManyToOne
	@NotNull
	@Searchable
	@Description(description="The visit this pregnancy observation was registered during")
	Visit visit;
		
	public Individual getMother() {
		return mother;
	}
	
	public void setMother(Individual mother) {
		this.mother = mother;
	}
	
    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}
	
	public void setExpectedDeliveryDate(Calendar expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}
	
    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
	public Calendar getRecordedDate() {
		return recordedDate;
	}
	
	public void setRecordedDate(Calendar recordedDate) {
		this.recordedDate = recordedDate;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
}
