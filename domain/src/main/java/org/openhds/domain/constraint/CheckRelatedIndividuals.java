package org.openhds.domain.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constaint.impl.CheckRelatedIndividualsImpl;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckRelatedIndividualsImpl.class)
@Documented
public @interface CheckRelatedIndividuals {

	String message() default "No two related individuals can be the same.";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default {};
}