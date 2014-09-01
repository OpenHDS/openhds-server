package org.openhds.events.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;

@Description(description = "Meta Data regarding an atomic event in OpenHDS")
@Entity
@Table(name = "EventMetaData")
@XmlRootElement(name = "eventMetaData")
public class EventMetaData {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    private String uuid;

    private String status;

    private String system;

    private String result;

    private Integer numTimesRead;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of insertion.")
    private Calendar insertDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of last update.")
    private Calendar lastUpdated;

    private static final long serialVersionUID = 1L;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getNumTimesRead() {
        return numTimesRead;
    }

    public void setNumTimesRead(Integer numTimesRead) {
        this.numTimesRead = numTimesRead;
    }

    public Calendar getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Calendar insertDate) {
        this.insertDate = insertDate;
    }

    public Calendar getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Calendar lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
