package org.openhds.domain.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.LocationHierarchyLevel;

@XmlRootElement
public class LocationHierarchyLevels {

    private List<LocationHierarchyLevel> locationHierarchyLevels;

    @XmlElement(name = "hierarchyLevel")
    public List<LocationHierarchyLevel> getLocationHierarchies() {
        return locationHierarchyLevels;
    }

    public void setLocationHierarchies(List<LocationHierarchyLevel> locationHierarchyLevels) {
        this.locationHierarchyLevels = locationHierarchyLevels;
    }

}
