package org.openhds.specialstudy.domain;

import java.lang.String;

privileged aspect EndUser_Roo_ToString {
    
    public String EndUser.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ").append(getUsername()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
    
}
