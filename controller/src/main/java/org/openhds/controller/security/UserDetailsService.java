package org.openhds.controller.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openhds.dao.service.UserDao;
import org.openhds.domain.model.Privilege;
import org.openhds.domain.model.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Spring Securities {@link org.springframework.security.core.userdetails.UserDetailsService}
 * Looks up a user based on username and returns a {@link UserDetails} instance that is
 * used by Spring Security
 * 
 * @author Dave Roberge
 *
 */
@SuppressWarnings("deprecation")
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private UserDao userDao;

	// hard coded spring roles - may change in future
	// it is not clear how we plan to integrate with spring security since we are defining our own
	// security mechanism using privileges
	private final String SPRING_ROLE = "ROLE_AUTHENTICATED";

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		List<org.openhds.domain.model.User> users = userDao.findByUsername(username);

		if (users == null || users.size() == 0) { // no user found by the name
			throw new UsernameNotFoundException("user " + username + " was not found");
		}
		
		org.openhds.domain.model.User user = users.get(0);

		return convertUser(user);
	}

	/**
	 * Convert a {@link org.openhds.model.User} instance (defined in OpenHDS) to a {@link UserDetails} instance used by Spring Security
	 * @param user the {@link org.openhds.model.User} instance within OpenHDS
	 * @return a {@link UserDetails} instance compatible with Spring Security
	 */
	private UserDetails convertUser(org.openhds.domain.model.User user) {
		UserDetails details = new User(user.getUsername(), user.getPassword(), true, true, true, true, convertAuthorities(user.getRoles()));
		return details;
	}

	/**
	 * Get a collection of {@link GrantedAuthority} which is required by {@link UserDetails} instance for Spring Security
	 * @return a collection of {@link GrantedAuthority} instances
	 */
	private Collection<GrantedAuthority> convertAuthorities(Set<Role> roles) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl(SPRING_ROLE));

		for(Role role : roles) {
			for(Privilege privilege: role.getPrivileges()) {
				authorities.add(new GrantedAuthorityImpl(privilege.getPrivilege()));
			}
		}
		
		return authorities;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}
}
