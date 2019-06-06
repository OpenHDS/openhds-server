package org.openhds.domain.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.SocialGroup;

@XmlRootElement
public class SocialGroups {

    private List<SocialGroup> socialGroups;
    private long updateTimestamp;

    @XmlElement(name = "socialgroup")
    public List<SocialGroup> getSocialGroups() {
        return socialGroups;
    }

    public void setSocialGroups(List<SocialGroup> socialGroups) {
        this.socialGroups = socialGroups;
    }

    public long getTimestamp() {
		return updateTimestamp;
	}

	public void setTimestamp(long timestamp) {
		this.updateTimestamp = timestamp;
	}
    
}
