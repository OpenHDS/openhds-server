package org.openhds.domain.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.model.Relationship;

@XmlRootElement
public class Relationships {

    private List<Relationship> relationships;

    @XmlElement(name = "relationship")
    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> copies) {
        this.relationships = copies;
    }
}
