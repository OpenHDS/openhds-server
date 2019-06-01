package org.openhds.dao.finder;

import java.lang.reflect.Method;
import org.openhds.dao.finder.NamingStrategy;

/**
 * Looks up Hibernate named queries based on the simple name of the invoked
 * class and the method name of the invocation
 */
public class SimpleNamingStrategy implements NamingStrategy {
	public String queryNameFromMethod(Class<?> findTargetType, Method finderMethod) {
		return findTargetType.getSimpleName() + "." + finderMethod.getName();
	}
}
