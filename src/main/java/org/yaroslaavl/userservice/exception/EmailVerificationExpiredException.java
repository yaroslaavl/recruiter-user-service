package org.yaroslaavl.userservice.exception;

public class EmailVerificationExpiredException extends RuntimeException {
    public EmailVerificationExpiredException(String message) {
        super(message);
    }
}
