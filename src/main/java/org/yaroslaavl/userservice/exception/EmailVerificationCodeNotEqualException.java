package org.yaroslaavl.userservice.exception;

public class EmailVerificationCodeNotEqualException extends RuntimeException {
    public EmailVerificationCodeNotEqualException(String message) {
        super(message);
    }
}
