package org.openhds.domain.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.model.Location;

@XmlRootElement
public class Locations {

    private List<Location> locations;

    @XmlElement(name = "location")
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}