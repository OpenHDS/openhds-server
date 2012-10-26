package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.util.UserCache;
import org.openhds.dao.service.Dao;
import org.openhds.domain.model.Privilege;
import org.openhds.domain.model.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Simple bean that provides access to the current logged in user
 *
 * Reasoning behind this class can be found at:
 * http://forum.springsource.org/showthread.php?t=49686
 *
 *
 * @author Dave
 *
 */
public class CurrentUserImpl implements CurrentUser, BeanFactoryAware {

    private Dao<org.openhds.domain.model.User, String> userDao;
    private BeanFactory beanFactory;
    
	public void setProxyUser(String username, String password, String[] privileges) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(String privilege : privileges) {
			authorities.add(new GrantedAuthorityImpl(privilege));
		}
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

    public org.openhds.domain.model.User getCurrentUser() {
   		return  findUser();
    }

	public Set<Privilege> getCurrentUserPrivileges() {
		Set<Privilege> privileges = new HashSet<Privilege>();

		for(GrantedAuthority authority : getGrantedAuthorities()) {
			Privilege privilege = new Privilege(authority.getAuthority());
			privileges.add(privilege);
		}
			

		return privileges;
	}
	
	/**
	 * @return the user name of the current logged in user
	 */
    public String getName() {
        Object obj = getSpringSecurityUser();
        if (obj instanceof User) {
            return ((User) obj).getUsername();
        }

        return null;
    }

    /**
     * @return the collection of granted authorites for the current logged in user
     */
	private Collection<GrantedAuthority> getGrantedAuthorities() {
		return (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}

    /**
     * Finder for retrieving the OpenHDS User
     * NOTE: The Spring Security user differs from the OpenHDS User
     * @return the User object for the current logged in Spring user
     */
	private org.openhds.domain.model.User findUser() {
		// attempt to read user from the request cache
		// NOTE: had to use bean factory because the bean has scope "request"
		UserCache cache = (UserCache) beanFactory.getBean("userCache");
		if (cache.getUser() == null) {
			Object obj = getSpringSecurityUser();
	
	        if (obj instanceof User) {
	            String username = ((User) obj).getUsername();
	            cache.setUser(userDao.findByProperty("username", username));
	        }
		}
        
        return cache.getUser();
	}

	/**
	 * @return The spring security principle (user)
	 */
	private Object getSpringSecurityUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return obj;
	}	
	
    public Dao<org.openhds.domain.model.User, String> getUserDao() {
        return userDao;
    }

    public void setUserDao(Dao<org.openhds.domain.model.User, String> userDao) {
        this.userDao = userDao;
    }

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}