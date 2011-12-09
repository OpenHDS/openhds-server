package org.openhds.specialstudy.domain;

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.openhds.specialstudy.domain.EndUser;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EndUser_Roo_Entity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long EndUser.id;
    
    @Version
    @Column(name = "version")
    private Integer EndUser.version;
    
    public Long EndUser.getId() {
        return this.id;
    }
    
    public void EndUser.setId(Long id) {
        this.id = id;
    }
    
    public Integer EndUser.getVersion() {
        return this.version;
    }
    
    public void EndUser.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void EndUser.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EndUser.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EndUser attached = this.entityManager.find(EndUser.class, this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EndUser.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EndUser.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EndUser merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }
    
    public static long EndUser.countEndUsers() {
        return (Long) entityManager().createQuery("select count(o) from EndUser o").getSingleResult();
    }
    
    public static List<EndUser> EndUser.findAllEndUsers() {
        return entityManager().createQuery("select o from EndUser o").getResultList();
    }
    
    public static EndUser EndUser.findEndUser(Long id) {
        if (id == null) return null;
        return entityManager().find(EndUser.class, id);
    }
    
    public static List<EndUser> EndUser.findEndUserEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from EndUser o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
