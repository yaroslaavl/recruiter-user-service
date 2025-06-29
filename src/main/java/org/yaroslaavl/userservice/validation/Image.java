package org.yaroslaavl.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {

    ImageType type();
    String message() default "{image.upload}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
