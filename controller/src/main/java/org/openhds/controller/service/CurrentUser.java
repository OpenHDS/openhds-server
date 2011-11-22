package org.openhds.controller.service;

import java.util.Set;
import org.openhds.domain.model.Privilege;
import org.openhds.domain.model.User;

public interface CurrentUser {
	
	/**
	 * @return the OpenHDS representation of the current logged in user
	 */
    public User getCurrentUser();
    
    /**
     * @return a set of all privileges for the current logged in user
     */
    public Set<Privilege> getCurrentUserPrivileges();
    
    /**
     * Allows clients of this class to set a proxy or temporary user for the current
     * request
     * 
     * @param username of the proxy user
     * @param password of the proxy user
     * @param privileges array of any privileges that this proxy user should have
     */
    public void setProxyUser(String username, String password, String[] privileges);
}