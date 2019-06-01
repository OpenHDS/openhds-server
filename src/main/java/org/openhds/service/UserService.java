package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Role;
import org.openhds.domain.User;

public interface UserService {

	User evaluateUser(User entityItem, String password) throws ConstraintViolations;
	
	User checkUser(User entityItem, String password) throws ConstraintViolations;
	
	boolean checkValidPassword(User entityItem, String password);
	
	List<org.openhds.domain.Role> getRoles();
	
	User convertAndSetRoles(User entityItem, List<String> roles);
}
