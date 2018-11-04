package org.openhds.domain.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.model.CensusIndividual;
import org.openhds.domain.model.Individual;

@XmlRootElement
public class CensusIndividuals {
    private List<CensusIndividual> individuals;

    @XmlElement(name = "censusIndividual")
    public List<CensusIndividual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<CensusIndividual> individuals) {
        this.individuals = individuals;
    }
}