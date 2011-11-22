package org.openhds.domain.constraint;

import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckStartDateGreaterThanBirthDateImpl.class)
public @interface CheckStartDateGreaterThanBirthDate {
    String message() default "The Start Date must be greater than the Birth Date.";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
}
