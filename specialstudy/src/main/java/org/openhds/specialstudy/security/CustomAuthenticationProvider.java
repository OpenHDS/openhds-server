package org.openhds.specialstudy.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

import org.openhds.specialstudy.domain.EndUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		   String password = (String) authentication.getCredentials();
	        if (!StringUtils.hasText(password)) {
	            throw new BadCredentialsException("Please enter password");
	        }
	        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	        try {
	            EndUser user = (EndUser) EndUser.findEndUsersByUsernameAndPasswordEquals(username, password).getSingleResult();       	            
	            authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
	        } catch (EmptyResultDataAccessException e) {
	            throw new BadCredentialsException("Invalid username or password");
	        } catch (EntityNotFoundException e) {
	            throw new BadCredentialsException("Invalid user");
	        } catch (NonUniqueResultException e) {
	            throw new BadCredentialsException("Non-unique user, contact administrator");
	        }
	        return new User(username, password, true, // enabled
	                true, // account not expired
	                true, // credentials not expired
	                true, // account not locked
	                authorities);
	}

}
