package org.yaroslaavl.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LinkedinLinkValidator.class)
public @interface LinkedinLink {

    String message() default "{candidate.registration.linkedinLink}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
