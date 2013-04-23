package org.openhds.domain.constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constraint.impl.CheckEndDateAndEndEventTypeImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEndDateAndEndEventTypeImpl.class)
@Documented
public @interface CheckEndDateAndEndEventType {
    String message() default "End Date and End Event Type mismatch. " +
    		"If an End Date is specified, an End Event Type other than 'NONE' is required." +
    		"If an End Date is not specified, the End Event Type must be 'NONE'. ";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
}
