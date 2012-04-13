package org.openhds.domain.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constaint.impl.CheckIntegerImpl;

@Target( { FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckIntegerImpl.class)
@Documented
public @interface CheckInteger {
    String message() default "The value must be an integer greater than 0";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
	
	int min() default 0;
}
