package org.openhds.dao.service;

import java.util.List;
import org.openhds.domain.model.Role;
import org.openhds.domain.model.User;

public interface RoleDao extends Dao<Role, String> {

	List<User> findAllUsersWithRole(Role role);
	
	List<Role> findAllRolesExcept(Role role);
}
