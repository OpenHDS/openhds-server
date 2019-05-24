package org.openhds.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.constraint.impl.CheckRecordedDateGreaterThanInsertDateImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckRecordedDateGreaterThanInsertDateImpl.class)
@Documented
public @interface CheckRecordedDateGreaterThanInsertDate {
    String message() default "The recorded date must be greater than the insert date of the mother.";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
}
