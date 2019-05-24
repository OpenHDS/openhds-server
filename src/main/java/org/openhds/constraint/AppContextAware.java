package org.openhds.constraint;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This can be used to apply conditional constraints on the class level
 * field validations since it's not possible to inject the application
 * context into the the constraints themselves.
 */
public class AppContextAware implements ApplicationContextAware {
	
	static protected ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
}
