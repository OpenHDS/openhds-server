package org.openhds.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppContextAware implements ApplicationContextAware {
	
	static protected ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
}
