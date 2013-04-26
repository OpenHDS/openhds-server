package org.openhds.domain.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constraint.impl.CheckIndividualNotUnknownImpl;

/**
 * A generic constraint to check that 2 fields are not equal
 * In other words, this constraint will enforce that 2 fields
 * specified by their field names, are not equal in a class
 * 
 * @author Dave
 *
 */
@Target( { FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckIndividualNotUnknownImpl.class)
@Documented
public @interface CheckIndividualNotUnknown {
    String message() default "This individual cannot be saved as Unknown";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
}
