package org.openhds.domain.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.Round;

@XmlRootElement
public class Rounds {
    private List<Round> rounds;

    @XmlElement(name = "round")
    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }
}
