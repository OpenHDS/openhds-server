package org.openhds.domain.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.LocationHierarchy;

@XmlRootElement
public class LocationHierarchies {

    private List<LocationHierarchy> locationHierarchies;

    @XmlElement(name = "hierarchy")
    public List<LocationHierarchy> getLocationHierarchies() {
        return locationHierarchies;
    }

    public void setLocationHierarchies(List<LocationHierarchy> locationHierarchies) {
        this.locationHierarchies = locationHierarchies;
    }

}
