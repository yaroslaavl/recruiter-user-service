package org.yaroslaavl.userservice.exception;

public class MailSendException extends RuntimeException {
    public MailSendException(String message) {
        super(message);
    }
}
