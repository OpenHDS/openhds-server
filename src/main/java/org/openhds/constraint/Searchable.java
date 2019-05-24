package org.openhds.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Target(FIELD)
@Retention(RUNTIME)
/** Marks a field of an entity as searchable.
 * 
 * Documentation: see how_search_works.txt . */
public @interface Searchable {
		
}

