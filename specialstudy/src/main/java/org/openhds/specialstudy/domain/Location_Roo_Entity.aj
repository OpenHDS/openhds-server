package org.openhds.specialstudy.domain;

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Version;
import org.openhds.specialstudy.domain.Location;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Location_Roo_Entity {
    
    @Version
    @Column(name = "version")
    private Integer Location.version;
    
    public Integer Location.getVersion() {
        return this.version;
    }
    
    public void Location.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Location.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Location attached = this.entityManager.find(Location.class, this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Location.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Location.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Location merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }
    
    public static long Location.countLocations() {
        return (Long) entityManager().createQuery("select count(o) from Location o").getSingleResult();
    }
    
    public static List<Location> Location.findAllLocations() {
        return entityManager().createQuery("select o from Location o").getResultList();
    }
    
    public static Location Location.findLocation(Long id) {
        if (id == null) return null;
        return entityManager().find(Location.class, id);
    }
    
    public static List<Location> Location.findLocationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Location o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
