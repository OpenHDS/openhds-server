package org.openhds.domain.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constraint.impl.CheckEndDateNotBeforeStartDateImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEndDateNotBeforeStartDateImpl.class)
@Documented
public @interface CheckEndDateNotBeforeStartDate{
    String message() default "The end date cannot be before the start date";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
