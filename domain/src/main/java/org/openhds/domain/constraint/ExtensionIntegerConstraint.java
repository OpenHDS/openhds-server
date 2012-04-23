package org.openhds.domain.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constaint.impl.ExtensionIntegerConstraintImpl;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ExtensionIntegerConstraintImpl.class)
@Documented
public @interface ExtensionIntegerConstraint {

	String message() default "Invalid value";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
	
	String constraint();
	
    boolean allowNull() default true;
}
