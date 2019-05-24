package org.openhds.constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.constraint.impl.CheckMotherFatherNotIndividualImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckMotherFatherNotIndividualImpl.class)
@Documented
public @interface CheckMotherFatherNotIndividual {
    String message() default "The individual cannot be a mother or father of themself.";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
