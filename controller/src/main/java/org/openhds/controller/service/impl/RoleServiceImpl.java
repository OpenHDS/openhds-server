package org.openhds.controller.service.impl;

import java.util.List;
import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.RoleDao;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.RoleService;
import org.openhds.domain.model.Privilege;
import org.openhds.domain.model.Role;
import org.openhds.domain.model.User;
import org.springframework.transaction.annotation.Transactional;

public class RoleServiceImpl implements RoleService {
	
	private GenericDao genericDao;
	private EntityService service;
	private RoleDao roleDao;
	
	public RoleServiceImpl(RoleDao roleDao, GenericDao genericDao, EntityService service) {
		this.roleDao = roleDao;
		this.genericDao = genericDao;
		this.service = service;
	}
	
	public Role evaluateRole(Role entityItem) throws ConstraintViolations {
		
		if (!checkDuplicateRoleName(entityItem.getName())) 
    		throw new ConstraintViolations("The Role name specified already exists.");	
		
		return entityItem;
	}

	/**
	 * Checks if a duplicate Role name exists.
	 */
	public boolean checkDuplicateRoleName(String name) {
		
		List<Role> list = genericDao.findListByProperty(Role.class, "name", name);		
		if (list.size() > 0)
			return false;		
		return true;	
	}

	/**
	 * Converts a string representation of the Privileges for the Role.
	 */
	public Role convertAndSetPrivileges(Role entityItem, List<String> privileges) {

		for (String p : privileges) {
			Privilege privilege = genericDao.findByProperty(Privilege.class, "privilege", p);	
			entityItem.getPrivileges().add(privilege);
		}
		return entityItem;
	}
	
	public List<Privilege> getPrivileges() {
		return genericDao.findAll(Privilege.class, false);
	}

	public List<User> findUsersWithRole(Role role) {
		return roleDao.findAllUsersWithRole(role);
	}
	
	public List<Role> findRolesExcluding(Role role) {
		return roleDao.findAllRolesExcept(role);
	}

	@Transactional
	public void updateUserRoles(User[] users, String[] roles, Role oldRole) {
		for(int i = 0; i < users.length; i++) {
			User persistedUser = genericDao.read(User.class, users[i].getUuid());
			
			removeRoleFromUser(persistedUser, oldRole);
			
			if (needToAddNewRole(roles[i])) {
				Role persistedRole = genericDao.read(Role.class, roles[i]);
				persistedUser.getRoles().add(persistedRole);
			}
			
			genericDao.update(persistedUser);
		}
	}

	private void removeRoleFromUser(User persistedUser, Role oldRole) {
		Role peristedRole = roleDao.read(oldRole.getUuid());
		persistedUser.getRoles().remove(peristedRole);
	}

	private boolean needToAddNewRole(String string) {
		return !NOROLE_VALUE.equals(string);
	}
}