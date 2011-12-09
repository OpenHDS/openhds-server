package org.openhds.specialstudy.domain;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.NotNull;

@Entity
@RooJavaBean
@RooToString
@RooEntity(finders = { "findEndUsersByUsernameAndPasswordEquals" })
public class EndUser {
	
    @PersistenceContext
    transient EntityManager entityManager;

    @NotNull
    private String username;

    @NotNull
    private String password;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Query findEndUsersByUsernameAndPasswordEquals(String username, String password) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        if (password == null || password.length() == 0) throw new IllegalArgumentException("The password argument is required");
        EntityManager em = EndUser.entityManager();
        Query q = em.createQuery("SELECT EndUser FROM EndUser AS enduser WHERE enduser.username = :username AND enduser.password = :password");
        q.setParameter("username", username);
        q.setParameter("password", password);
        return q;
    }
    
    public static final EntityManager entityManager() {
        EntityManager em = new EndUser().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
}
