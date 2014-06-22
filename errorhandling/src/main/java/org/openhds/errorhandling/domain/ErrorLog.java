package org.openhds.errorhandling.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.model.FieldWorker;

@Description(description = "An error log")
@Entity
@Table(name = "errorlog")
@XmlRootElement(name = "errorlog")
public class ErrorLog implements Serializable {

    private static final long serialVersionUID = 2447734552586256198L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length=32)
    private String uuid;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Description(description="Date of insertion.")
    private Calendar insertDate;

    @Column(length=65535)
    private String dataPayload;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = org.openhds.errorhandling.domain.Error.class)
    @JoinColumn(name = "error_uuid")
    private List<Error> errors;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=FieldWorker.class)
    @Description(description="The field worker who collected the data, identified by external id.")
    private FieldWorker fieldWorker;

    private String assignedTo;

    private String entityType;

    private String resolutionStatus;

    private Calendar dateOfResolution;

    public Calendar getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Calendar insertDate) {
        this.insertDate = insertDate;
    }

    public String getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(String dataPayload) {
        this.dataPayload = dataPayload;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public FieldWorker getFieldWorker() {
        return fieldWorker;
    }

    public void setFieldWorker(FieldWorker fieldWorker) {
        this.fieldWorker = fieldWorker;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public Calendar getDateOfResolution() {
        return dateOfResolution;
    }

    public void setDateOfResolution(Calendar dateOfResolution) {
        this.dateOfResolution = dateOfResolution;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
