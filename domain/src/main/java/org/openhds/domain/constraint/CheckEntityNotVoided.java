package org.openhds.domain.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constaint.impl.CheckEntityNotVoidedImpl;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEntityNotVoidedImpl.class)
@Documented
public @interface CheckEntityNotVoided {
    String message() default "This entity has been deleted";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
