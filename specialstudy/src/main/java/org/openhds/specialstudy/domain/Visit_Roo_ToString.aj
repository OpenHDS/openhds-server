package org.openhds.specialstudy.domain;

import java.lang.String;

privileged aspect Visit_Roo_ToString {
    
    public String Visit.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("ExtId: ").append(getExtId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
    
}
