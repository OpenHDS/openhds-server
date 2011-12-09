package org.openhds.specialstudy.domain;

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Version;
import org.openhds.specialstudy.domain.Individual;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Individual_Roo_Entity {
    
    @Version
    @Column(name = "version")
    private Integer Individual.version;
    
    public Integer Individual.getVersion() {
        return this.version;
    }
    
    public void Individual.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Individual.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Individual attached = this.entityManager.find(Individual.class, this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Individual.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Individual.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Individual merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }
    
    public static long Individual.countIndividuals() {
        return (Long) entityManager().createQuery("select count(o) from Individual o").getSingleResult();
    }
    
    public static List<Individual> Individual.findAllIndividuals() {
        return entityManager().createQuery("select o from Individual o").getResultList();
    }
    
    public static Individual Individual.findIndividual(Long id) {
        if (id == null) return null;
        return entityManager().find(Individual.class, id);
    }
    
    public static List<Individual> Individual.findIndividualEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Individual o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
