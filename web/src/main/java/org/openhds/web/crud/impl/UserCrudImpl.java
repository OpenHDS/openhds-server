package org.openhds.web.crud.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;
import org.openhds.domain.model.Role;
import org.openhds.domain.model.User;
import org.openhds.web.service.UserService;

public class UserCrudImpl extends EntityCrudImpl<User, String> {

	UserService service;
	String retypedPassword;
	List<String> roles;

	public UserCrudImpl(Class<User> entityClass) {
        super(entityClass);
        roles = new ArrayList<String>();
    }
	
    @Override
    public String create() {

        try {
        	service.evaluateUser(entityItem, retypedPassword);	
        	service.convertAndSetRoles(entityItem, roles);
        	roles = null;
        	super.create();
        } catch (Exception e) {
            jsfService.addError(e.getMessage());
        }
        return null;
    }
    
    @Override
    public String edit() {
    	
        try {
        	entityItem.getRoles().clear();
        	service.checkUser(entityItem, retypedPassword);
        	service.convertAndSetRoles(entityItem, roles);
        	entityService.save(entityItem);
        } 
        catch(Exception e) {
        	jsfService.addError(e.getMessage());
		} 

        return detailSetup();
    }
      
    /**
     * Retrieves the available roles to be displayed as checkboxes on the UI.
     */
    public List<SelectItem> getRoleSelectItems() {
    	List<SelectItem> rolesSelectItems = new ArrayList<SelectItem>();
    	List<Role> roles = service.getRoles();
    	
    	for(Role role : roles) {
    		rolesSelectItems.add(new SelectItem(role.getName()));
    	}
    	return rolesSelectItems;
    }
       
	public String getRetypedPassword() {
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

	public UserService getService() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}
	
	/**
	 * Get all roles of which the entityItem belongs to.
	 */
	public List<String> getRoles() {
		Set<Role> roles = entityItem.getRoles();
		List<String> list = new ArrayList<String>();
		for (Role r : roles) {
			list.add(r.getName());
		}
		return list;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
