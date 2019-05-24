package org.openhds.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.constraint.impl.CheckGenderOfRelationshipImpl;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckGenderOfRelationshipImpl.class)
@Documented
public @interface CheckGenderOfRelationship {

	String message() default "A relationship must only be between a man and woman.";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
}