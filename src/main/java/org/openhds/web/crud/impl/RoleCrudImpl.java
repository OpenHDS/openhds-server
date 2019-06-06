package org.openhds.web.crud.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.openhds.domain.Privilege;
import org.openhds.domain.Role;
import org.openhds.domain.User;
import org.openhds.service.RoleService;

public class RoleCrudImpl extends EntityCrudImpl<Role, String> {

	RoleService service;
	List<String> privileges;
	private final List<UserAndRole> usersAndRoles;
	private Role selectedRole;

	public RoleCrudImpl(Class<Role> entityClass) {
        super(entityClass);
        privileges = new ArrayList<String>();
        usersAndRoles = new ArrayList<UserAndRole>();
    }
	
	@Override
	public String createSetup() {
        reset(false, true);
        showListing=false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }

	
	/**
	 * Helper class that is used primarily to bind
	 * the role list box to a value
	 */
	public static class UserAndRole {
		User user;
		String role;
		
		public UserAndRole(User user) {
			this.user = user;
		}
		
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public User getUser() {
			return user;
		}
	}
	
	@Override
    public String create() {

        try {
        	service.evaluateRole(entityItem);
        	service.convertAndSetPrivileges(entityItem, privileges);
        	privileges = null;
        	super.create();
        } catch (Exception e) {
            jsfService.addError(e.getMessage());
        }
        return null;
    }
    
    @Override
    public String edit() {
    	
        try {
        	entityItem.getPrivileges().clear();
        	service.convertAndSetPrivileges(entityItem, privileges);
        	entityService.save(entityItem);
        } 
        catch(Exception e) {
        	jsfService.addError(e.getMessage());
		} 

        return detailSetup();
    }
        
    @Override
    public String delete() {
    	String entityId = jsfService.getReqParam("itemId");
    	
    	if (entityId.equals("ROLE1")) {
    		// ROLE1 is the built in administrator role, and should not be deleted
    		jsfService.addError("Cannot delete the Administrator Role");
    		return null;
    	}
    	
    	selectedRole = (Role)converter.getAsObject(FacesContext.getCurrentInstance(), null, entityId);
    	
    	List<User> usersWithRole = service.findUsersWithRole(selectedRole);
    	
    	if (usersWithRole.size() == 0) {
    		return super.delete();
    	} else {
    		// since there are active users assigned this role, they will need to be
    		// reassigned a new role or have the role removed
    		showListing = false;
    		usersAndRoles.clear();
    		
    		for(User user : usersWithRole) {
    			usersAndRoles.add(new UserAndRole(user));
    		}
    		
    		return "role_assign";
    	}
    }
    
    public String cancelUserAssignment() {
    	showListing = true;
    	return listSetup();
    }
    
    public String updateUserRoles() {
    	User[] users = new User[usersAndRoles.size()];
    	String[] roles = new String[usersAndRoles.size()];
    	for(int i =0; i < usersAndRoles.size(); i++) {
    		users[i] = usersAndRoles.get(i).user;
    		roles[i] = usersAndRoles.get(i).role;
    	}
    	
    	service.updateUserRoles(users, roles, selectedRole);
    	
    	showListing = true;
    	return listSetup();
    }
    
    public List<SelectItem> getAllRolesExceptSelected() {
    	List<SelectItem> selectItems = new ArrayList<SelectItem>();
    	selectItems.add(new SelectItem(RoleService.NOROLE_VALUE, "Remove Role from User"));
    	
    	List<Role> roles = service.findRolesExcluding(selectedRole);
    	for (Role role : roles) {
    		selectItems.add(new SelectItem(role.getUuid(), role.getName()));
    	}
    	
    	return selectItems;
    }
    
	/**
	 * Get all privileges of which the entityItem belongs to.
	 */
    public List<String> getPrivileges() {
    	Set<Privilege> privileges = entityItem.getPrivileges();
		List<String> list = new ArrayList<String>();
		for (Privilege p : privileges) {
			list.add(p.getPrivilege());
		}
		return list;
	}
    
    /**
     * Retrieves the privileges to be displayed as checkboxes on the UI.
     */
    public List<SelectItem> getPrivilegeSelectItems() {
    	List<SelectItem> privilegesSelectItems = new ArrayList<SelectItem>();
    	List<Privilege> privileges = service.getPrivileges();
    	
    	for(Privilege p : privileges) {
    		privilegesSelectItems.add(new SelectItem(p.getPrivilege()));
    	}
    	return privilegesSelectItems;
    }

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	public RoleService getService() {
		return service;
	}

	public void setService(RoleService service) {
		this.service = service;
	}

	public List<UserAndRole> getUsersAndRoles() {
		return usersAndRoles;
	}
}
