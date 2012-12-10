package org.openhds.domain.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.model.Visit;

@XmlRootElement
public class Visits {
    private List<Visit> visits;

    @XmlElement(name = "visit")
    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}
