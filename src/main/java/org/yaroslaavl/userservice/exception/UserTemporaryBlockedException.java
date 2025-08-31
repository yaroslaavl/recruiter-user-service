package org.yaroslaavl.userservice.exception;

public class UserTemporaryBlockedException extends RuntimeException {
    public UserTemporaryBlockedException(String message) {
        super(message);
    }
}
