package org.yaroslaavl.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedinLinkValidator implements ConstraintValidator<LinkedinLink, String> {

    @Override
    public boolean isValid(String link, ConstraintValidatorContext constraintValidatorContext) {
        if (link != null && !link.isEmpty()) {
            Pattern pattern = Pattern.compile("^(https?://)?(www\\.)?linkedin\\.com/in/[a-zA-Z0-9_-]+/?$");
            Matcher matcher = pattern.matcher(link);
            return matcher.matches();
        }

        return false;
    }
}
