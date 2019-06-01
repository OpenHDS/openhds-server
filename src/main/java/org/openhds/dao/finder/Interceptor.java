package org.openhds.dao.finder;

import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

/**
 * Connects the Spring AOP magic with the Hibernate DAO magic. First, only
 * methods annotated with the <code>DynamicFinder</code> annotation are
 * considered. Depending on the result type, it delegates the method call to a
 * method that returns a <code>List</code> or an <code>Iterator</code>.
 */
public class Interceptor implements IntroductionInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		FinderExecutor<?> executor = (FinderExecutor<?>) methodInvocation
				.getThis();

		Method finderMethod = methodInvocation.getMethod();
		DynamicFinder finderAnnotation = methodInvocation.getMethod()
				.getAnnotation(DynamicFinder.class);

		if (finderAnnotation == null)
			return methodInvocation.proceed();

		if (finderMethod.getReturnType() == List.class) {
			Object[] arguments = methodInvocation.getArguments();
			return executor.executeFinder(methodInvocation.getMethod(),
					arguments);
		} else {
			Object[] arguments = methodInvocation.getArguments();
			return executor.iterateFinder(methodInvocation.getMethod(),
					arguments);
		}

	}

	public boolean implementsInterface(Class<?> intf) {
		return intf.isInterface()
				&& FinderExecutor.class.isAssignableFrom(intf);
	}
}
