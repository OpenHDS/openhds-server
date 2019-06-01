package org.openhds.dao.finder;

import java.lang.reflect.Method;

/**
 * Looks up named queries based on the value of the <code>DynamicFinder</code>
 * annotation. If no value is specified in the finder annotation, it falls back
 * to the same scheme as <code>SimpleNamingStrategy</code>.
 * 
 * @author brent
 * 
 */
public class AnnotationNamingStrategy extends SimpleNamingStrategy {

    @Override
    public String queryNameFromMethod(Class<?> findTargetType,
            Method finderMethod) {
        DynamicFinder finderAnnotation = finderMethod.getAnnotation(DynamicFinder.class);
        if (finderAnnotation.value() != null) {
            return finderAnnotation.value();
        }
        return super.queryNameFromMethod(findTargetType, finderMethod);
    }
}
