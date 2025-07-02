package org.yaroslaavl.userservice.exception;

public class ApprovalRequiredException extends RuntimeException {
    public ApprovalRequiredException(String message) {
        super(message);
    }
}
