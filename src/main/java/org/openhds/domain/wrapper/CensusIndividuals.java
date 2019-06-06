package org.openhds.domain.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.CensusIndividual;

@XmlRootElement
public class CensusIndividuals {
    private List<CensusIndividual> individuals;
    private long updateTimestamp;

    @XmlElement(name = "censusIndividual")
    public List<CensusIndividual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<CensusIndividual> individuals) {
        this.individuals = individuals;
    }

	public long getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(long updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}