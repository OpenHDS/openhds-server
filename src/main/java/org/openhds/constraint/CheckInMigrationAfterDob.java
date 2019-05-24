package org.openhds.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.constraint.impl.CheckInMigrationAfterDobImpl;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckInMigrationAfterDobImpl.class)
@Documented
public @interface CheckInMigrationAfterDob {
    String message() default "The In Migration date cannot be before the Individuals Date of Birth";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};

}
