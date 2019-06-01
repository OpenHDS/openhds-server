package org.openhds.service;

import java.util.List;

import org.openhds.annotations.Authorized;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.domain.User;
public interface UserLookupService {
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	User getUser(String username);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<User> getUsers();
}
