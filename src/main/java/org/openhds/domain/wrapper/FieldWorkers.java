package org.openhds.domain.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.FieldWorker;

@XmlRootElement(name = "fieldworkers")
public class FieldWorkers {

    private List<FieldWorker> fieldWorkers;

    @XmlElement(name = "fieldworker")
    public List<FieldWorker> getFieldWorkers() {
        return fieldWorkers;
    }

    public void setFieldWorkers(List<FieldWorker> fieldWorkers) {
        this.fieldWorkers = fieldWorkers;
    }

}
