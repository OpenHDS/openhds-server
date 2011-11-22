package org.openhds.web.service.impl;

import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Role;
import org.openhds.domain.model.User;
import org.openhds.web.service.UserService;

public class UserServiceImpl implements UserService {
	
	private GenericDao genericDao;
	
	public UserServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public User evaluateUser(User entityItem, String password) throws ConstraintViolations {
		
		if (!checkDuplicateUsername(entityItem.getUsername())) 
    		throw new ConstraintViolations("The username specified already exists.");	
		if (!checkValidPassword(entityItem, password)) 
    		throw new ConstraintViolations("The passwords entered do not match.");		
		return entityItem;
	}
	
	public User checkUser(User entityItem, String password) throws ConstraintViolations {
		
		if (!checkValidPassword(entityItem, password)) 
    		throw new ConstraintViolations("The passwords entered do not match.");		
		
		return entityItem;
	}
	
	/**
	 * Converts a string representation of the Roles for the User
	 * into Roles of which the User belongs.
	 */
	public User convertAndSetRoles(User entityItem, List<String> roles) {

		for (String role : roles) {
			Role r = genericDao.findByProperty(Role.class, "name", role);	
			entityItem.getRoles().add(r);
		}
		return entityItem;
	}
	
	/**
	 * Checks if the passwords match.
	 */
	public boolean checkValidPassword(User entityItem, String password) {
		if (entityItem.getPassword().equals(password))
			return true;
		return false;
	}

	/**
	 * Checks if a duplicate username exists.
	 */
	public boolean checkDuplicateUsername(String username) {
		
		List<User> list = genericDao.findListByProperty(User.class, "username", username);		
		if (list.size() > 0)
			return false;		
		return true;	
	}
		
	public List<Role> getRoles() {
		return genericDao.findAllDistinct(Role.class);
	}
}
