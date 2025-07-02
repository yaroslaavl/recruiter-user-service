package org.yaroslaavl.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WebsiteValidator implements ConstraintValidator<Website, String> {

    @Override
    public boolean isValid(String website, ConstraintValidatorContext constraintValidatorContext) {
        if (website == null || website.isEmpty()) {
            return false;
        }

        return website.startsWith("https://");
    }
}
