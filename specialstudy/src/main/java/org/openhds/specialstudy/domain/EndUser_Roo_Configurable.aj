package org.openhds.specialstudy.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect EndUser_Roo_Configurable {
    
    declare @type: EndUser: @Configurable;
    
}
