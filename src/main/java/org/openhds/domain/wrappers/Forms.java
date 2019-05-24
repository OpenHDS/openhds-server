package org.openhds.domain.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.domain.Form;

@XmlRootElement
public class Forms {

    private List<Form> forms;

    @XmlElement(name = "form")
    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

}