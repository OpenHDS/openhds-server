package org.openhds.dao.finder;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * The <code>Advisor</code> and <code>Interceptor</code> are used to 
 * refer back to the <code>BaseDaoImpl.</code> All invocations where 
 * the method name starts with 'find' are passed on to the DAO and 
 * the single method <code>executeFinder().</code>
 */

public class Advisor extends DefaultIntroductionAdvisor {

	private static final long serialVersionUID = 7548715857748372217L;

	public Advisor() {
		super(new Interceptor());
	}
}
