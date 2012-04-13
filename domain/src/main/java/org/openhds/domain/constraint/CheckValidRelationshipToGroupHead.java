package org.openhds.domain.constraint;

import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

import org.openhds.domain.constaint.impl.CheckValidRelationshipToGroupHeadImpl;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;

@Target( { TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckValidRelationshipToGroupHeadImpl.class)
public @interface CheckValidRelationshipToGroupHead {
    String message() default "Invalid Relationship specified to the Group Head.";

	Class<?>[] groups() default{};

	Class<? extends Payload>[] payload() default {};
}
