package org.openhds.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.constraint.impl.CheckFieldNotBlankImpl;

@Target( { METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckFieldNotBlankImpl.class)
@Documented
public @interface CheckFieldNotBlank {

	String message() default "Field cannot be left blank.";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
}
