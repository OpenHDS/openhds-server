package org.openhds.dao;

import java.util.List;
import org.openhds.domain.Role;
import org.openhds.domain.User;

public interface RoleDao extends Dao<Role, String> {

	List<User> findAllUsersWithRole(Role role);
	
	List<Role> findAllRolesExcept(Role role);
}
