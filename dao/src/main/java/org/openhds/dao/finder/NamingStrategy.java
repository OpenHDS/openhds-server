package org.openhds.dao.finder;

import java.lang.reflect.Method;

/**
 * Used to locate a named query based on the called finder method
 */
public interface NamingStrategy {
	public String queryNameFromMethod(Class<?> findTargetType,
			Method finderMethod);
}
