package org.openhds.web.service;

import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.Role;
import org.openhds.domain.model.User;

public interface UserService {

	User evaluateUser(User entityItem, String password) throws ConstraintViolations;
	
	User checkUser(User entityItem, String password) throws ConstraintViolations;
	
	boolean checkValidPassword(User entityItem, String password);
	
	List<Role> getRoles();
	
	User convertAndSetRoles(User entityItem, List<String> roles);
}
