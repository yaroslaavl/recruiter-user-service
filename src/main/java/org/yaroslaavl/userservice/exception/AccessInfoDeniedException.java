package org.yaroslaavl.userservice.exception;

public class AccessInfoDeniedException extends RuntimeException {
    public AccessInfoDeniedException(String message) {
        super(message);
    }
}
