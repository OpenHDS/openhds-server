package org.openhds.domain.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.model.SocialGroup;

@XmlRootElement
public class SocialGroups {

    private List<SocialGroup> socialGroups;

    @XmlElement(name = "socialgroup")
    public List<SocialGroup> getSocialGroups() {
        return socialGroups;
    }

    public void setSocialGroups(List<SocialGroup> socialGroups) {
        this.socialGroups = socialGroups;
    }

}
