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

import org.openhds.domain.constaint.impl.CheckHouseholdHeadAgeImpl;

@Target( { METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckHouseholdHeadAgeImpl.class)
@Documented
public @interface CheckHouseholdHeadAge {

	String message() default "Age";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
	
	boolean allowNull() default false;
}

