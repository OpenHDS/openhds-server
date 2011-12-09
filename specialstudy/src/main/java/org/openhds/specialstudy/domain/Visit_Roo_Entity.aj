package org.openhds.specialstudy.domain;

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Version;
import org.openhds.specialstudy.domain.Visit;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Visit_Roo_Entity {
    
    @Version
    @Column(name = "version")
    private Integer Visit.version;
    
    public Integer Visit.getVersion() {
        return this.version;
    }
    
    public void Visit.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Visit.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Visit attached = this.entityManager.find(Visit.class, this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Visit.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Visit.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Visit merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }
    
    public static long Visit.countVisits() {
        return (Long) entityManager().createQuery("select count(o) from Visit o").getSingleResult();
    }
    
    public static List<Visit> Visit.findAllVisits() {
        return entityManager().createQuery("select o from Visit o").getResultList();
    }
    
    public static Visit Visit.findVisit(Long id) {
        if (id == null) return null;
        return entityManager().find(Visit.class, id);
    }
    
    public static List<Visit> Visit.findVisitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Visit o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
