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

import org.openhds.constraint.impl.CheckIndividualGenderFemaleImpl;

@Target( { METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckIndividualGenderFemaleImpl.class)
@Documented
public @interface CheckIndividualGenderFemale {

	String message() default "The gender specified must be Female.";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
		
	boolean allowNull() default false;
}
