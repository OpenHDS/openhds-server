package org.openhds.controller.service;

import java.util.List;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.User;
public interface UserLookupService {
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	User getUser(String username);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<User> getUsers();
}
