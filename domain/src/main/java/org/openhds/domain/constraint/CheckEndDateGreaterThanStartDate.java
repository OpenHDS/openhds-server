package org.openhds.domain.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constraint.impl.CheckEndDateGreaterThanStartDateImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEndDateGreaterThanStartDateImpl.class)
@Documented
public @interface CheckEndDateGreaterThanStartDate {
    String message() default "The end date cannot be prior to or equal to the start date";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
