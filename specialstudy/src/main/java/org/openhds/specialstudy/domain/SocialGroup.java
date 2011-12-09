package org.openhds.specialstudy.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class SocialGroup {

    @PersistenceContext
    transient EntityManager entityManager;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull
    private String extId;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SocialGroup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }
    
    public static SocialGroup findSocialGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(SocialGroup.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public static List<SocialGroup> findSocialGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from SocialGroup o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static long countSocialGroups() {
        return (Long) entityManager().createQuery("select count(o) from SocialGroup o").getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	public static List<SocialGroup> findAllSocialGroups() {
        return entityManager().createQuery("select o from SocialGroup o").getResultList();
    }
    
	public static List<SocialGroup> findAllSocialGroupsByHouseholdId(String id) {
		List<SocialGroup> list = findAllSocialGroups();
		List<SocialGroup> filteredList = new ArrayList<SocialGroup>();
		
		for (SocialGroup item : list) {
			if (item.getExtId().equals(id.toUpperCase())) 
				filteredList.add(item);
		}
		return filteredList;
    }
	
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SocialGroup attached = this.entityManager.find(SocialGroup.class, this.id);
            this.entityManager.remove(attached);
        }
    }

    public static final EntityManager entityManager() {
        EntityManager em = new SocialGroup().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
}
