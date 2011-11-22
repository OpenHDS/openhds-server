package org.openhds.dao.service;

import java.util.List;
import org.openhds.dao.finder.DynamicFinder;
import org.openhds.domain.model.User;

public interface UserDao extends Dao<User, Long> {
	
	/** This is mapped to the hibernate mapping file for specifying a 
	 * query to find a <code>User</code> type by the specified 
	 * <code>username</code> */
	@DynamicFinder("User.findByUsername")
	List<User> findByUsername(String username);
}
