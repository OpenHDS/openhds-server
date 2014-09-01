package org.openhds.events.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Description(description = "An atomic OpenHDS event")
@Entity
@Table(name = "events")
@XmlRootElement(name = "event")
public class OpenHDSEvent implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    private String uuid;

    @Description(description="Date of insertion.")
    private Calendar insertDate;

    private String actionType;

    private String entityType;

    @Column(length=65535)
    private String eventData;

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = org.openhds.events.domain.EventMetaData.class)
    @JoinColumn(name = "event_uuid")
    @JsonIgnore
    private List<EventMetaData> eventMetaData = new ArrayList<EventMetaData>();

    public OpenHDSEvent() { }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Calendar getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Calendar insertDate) {
        this.insertDate = insertDate;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public List<EventMetaData> getEventMetaData() {
        return eventMetaData;
    }

    public void setEventMetaData(List<EventMetaData> eventMetaData) {
        this.eventMetaData = eventMetaData;
    }

    public EventMetaData findMetaData(String system) {
        if (eventMetaData != null) {
            for (EventMetaData md : eventMetaData) {
                if (md.getSystem().equals(system)) {
                    return md;
                }
            }
        }
        return null;
    }
}
