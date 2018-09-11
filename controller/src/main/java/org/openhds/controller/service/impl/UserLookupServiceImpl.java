package org.openhds.controller.service.impl;

import java.util.List;

import org.openhds.controller.service.UserLookupService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.User;

public class UserLookupServiceImpl implements UserLookupService {
	private GenericDao genericDao;

	public UserLookupServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public List<User> getUsers() {
		return genericDao.findAll(User.class, true);
	}
	
	public User getUser(String username) {
		return genericDao.findByProperty(User.class, "username", username);
	}
}
