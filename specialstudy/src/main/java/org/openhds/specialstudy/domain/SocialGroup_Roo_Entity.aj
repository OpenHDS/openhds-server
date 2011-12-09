package org.openhds.specialstudy.domain;

import java.lang.Integer;
import javax.persistence.Column;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SocialGroup_Roo_Entity {
    
    @Version
    @Column(name = "version")
    private Integer SocialGroup.version;
    
    public Integer SocialGroup.getVersion() {
        return this.version;
    }
    
    public void SocialGroup.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void SocialGroup.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
}
