package org.openhds.domain.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.Individual;

@XmlRootElement
public class Individuals {
    private List<Individual> individuals;

    @XmlElement(name = "individual")
    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }
}
