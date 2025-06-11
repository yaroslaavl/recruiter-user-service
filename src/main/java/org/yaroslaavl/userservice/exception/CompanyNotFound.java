package org.yaroslaavl.userservice.exception;

public class CompanyNotFound extends RuntimeException {
    public CompanyNotFound(String message) {
        super(message);
    }
}
