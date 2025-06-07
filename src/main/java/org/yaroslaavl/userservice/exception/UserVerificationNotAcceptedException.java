package org.yaroslaavl.userservice.exception;

public class UserVerificationNotAcceptedException extends RuntimeException {
    public UserVerificationNotAcceptedException(String message) {
        super(message);
    }
}
