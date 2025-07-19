package org.yaroslaavl.userservice.exception;

public class RecruiterAccountAlreadyApprovedException extends RuntimeException {
    public RecruiterAccountAlreadyApprovedException(String message) {
        super(message);
    }
}
