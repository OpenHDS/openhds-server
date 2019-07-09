package org.openhds.service.impl;
import java.util.List;

import org.openhds.service.UserLookupService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.User;

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