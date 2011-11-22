package org.openhds.dao.finder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A runtime annotation that marks an interface method as a dynamic finder
 * method. Dynamic finder methods are not implemented in Java code, but are
 * serviced by named hibernate queries.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicFinder {
	String value();
}
