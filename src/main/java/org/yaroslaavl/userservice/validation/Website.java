package org.yaroslaavl.userservice.validation;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WebsiteValidator.class)
public @interface Website {

    String message() default "{company.request.info.website}";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
