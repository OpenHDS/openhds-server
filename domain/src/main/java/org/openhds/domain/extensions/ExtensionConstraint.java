package org.openhds.domain.extensions;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ExtensionConstraintImpl.class)
@Documented
public @interface ExtensionConstraint {

	String message() default "Invalid value";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
	
	String constraint();
	
	boolean allowNull() default true;
}

