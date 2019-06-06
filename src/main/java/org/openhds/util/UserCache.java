package org.openhds.util;

import org.openhds.domain.User;

/**
 * The UserCache class is meant to act as a cache to hold a reference to the current logged in user.
 * It is declared in the application context with a scope of request. In theory, each request will retrieve
 * the User instance from the database once per request, and then should be cached for the rest of the
 * request. The primary reason this class was introduced was to deal with AOP Authorization. Essentially,
 * some service classes depend on other service classes (i.e. DeathService depends on EntityService). A problem
 * arose when a user called a method on the DeathService class, and it in turn called a method on the EntityService
 * class. In this case, authorizations would be checked twice, once on the DeathService class and once on the
 * EntityService class. Checking authorizations required a DB lookup (SELECT statement for the User). When the second lookup happened
 * it was usually within a transaction. Hibernate would force a session flush (even though the transaction was not over)
 * and would throw an exception. This class will guarantee only 1 lookup for request will occur, which avoids
 * the issue of Hibernate Flushing during non-completed transaction. In addition, it should also help with performance
 * although this has not been an issue yet.
 * 
 * @author Dave Roberge
 *
 */
public class UserCache {

	private User user; // logged in user

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
