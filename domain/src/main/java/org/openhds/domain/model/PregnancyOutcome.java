package org.openhds.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.CheckEntityNotVoided;
import org.openhds.domain.constraint.CheckIndividualGenderFemale;
import org.openhds.domain.constraint.CheckIndividualGenderMale;
import org.openhds.domain.constraint.CheckIndividualNotUnknown;
import org.openhds.domain.constraint.CheckInteger;
import org.openhds.domain.constraint.Searchable;
import org.openhds.domain.util.CalendarAdapter;

@Description(description="A Pregnancy Outcome represents the results of the " +
		"pregnancy. It contains information about the Visit that is associated, " +
		"the date of the outcome, and who the parents are. One Pregnancy Outcome " +
		"can have multiple Outcomes. ")
@Entity
@Table(name="pregnancyoutcome")
@XmlRootElement(name = "pregnancyoutcome")
public class PregnancyOutcome extends AuditableCollectedEntity implements Serializable {

    private static final long serialVersionUID = -8901037436653805795L;
        
    @ManyToOne(cascade = CascadeType.MERGE)
    @Description(description="Visit that is associated with the pregnancy outcome, identified by the external id.")
    private Visit visit;
   
    @CheckInteger
    @Description(description="Total number of children born, including live and still births.")
    private Integer childEverBorn = 0;
   
    @CheckInteger
    @Description(description="Total number of live births.")
    private Integer numberOfLiveBirths = 0;
   
    @Temporal(javax.persistence.TemporalType.DATE)
    @Past
    @Description(description="Date of the pregnancy outcome.")
    private Calendar outcomeDate;
   
    @Searchable
    @ManyToOne
    @CheckIndividualGenderFemale(allowNull = false)
    @CheckIndividualNotUnknown
    @CheckEntityNotVoided
    @Description(description="Mother of the pregnancy outcome.")
    private Individual mother;
    
    @Searchable
    @ManyToOne
    @CheckIndividualGenderMale(allowNull = false)
    @CheckEntityNotVoided
    @Description(description="Father of the pregnancy outcome.")
    private Individual father;
   
    @OneToMany(cascade = CascadeType.ALL)
    @Description(description="List of all outcomes for the pregnancy.")
    private List<Outcome> outcomes = new ArrayList<Outcome>();

    public Integer getChildEverBorn() {
        return childEverBorn;
    }

    public void setChildEverBorn(Integer childEverBorn) {
        this.childEverBorn = childEverBorn;
    }

    public Integer getNumberOfLiveBirths() {
        return numberOfLiveBirths;
    }

    public void setNumberOfLiveBirths(Integer numberOfLiveBirths) {
        this.numberOfLiveBirths = numberOfLiveBirths;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
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

    @XmlElement(name="outcome")
    @XmlElementWrapper(name="outcomes")
    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

    @XmlJavaTypeAdapter(value=CalendarAdapter.class) 
    public Calendar getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(Calendar outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    /**
     * Helper method to add an outcome to a pregnancy.
     *
     * @param type the type of outcome
     * @param child the child for the outcome (only for live births)
     */
    public Outcome addOutcome(String type, Individual child) {
        Outcome outcome = new Outcome();
        outcome.setType(type);
        outcome.setChild(child);
        
        return addOutcome(outcome);
    }
    
    /**
     * Add an outcome to this pregnancy outcome
     * This method will increase the count for the number of child for this pregnancy, 
     * as well as increase the count for the number of live births if the outcome is of type live birth
     * 
     * @param outcome the outcome to add to the pregnancy
     * @return the outcome added to the pregnancy oucome
     */
    public Outcome addOutcome(Outcome outcome) {
        outcomes.add(outcome);
        childEverBorn++;

        return outcome;    	
    }
    
    public Outcome addLiveBirthOutcome(Outcome outcome) {
    	addOutcome(outcome);
        numberOfLiveBirths++;
        
        return outcome;
    }
}
