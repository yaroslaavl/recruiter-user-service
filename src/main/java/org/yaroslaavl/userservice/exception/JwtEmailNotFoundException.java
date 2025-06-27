package org.yaroslaavl.userservice.exception;

public class JwtEmailNotFoundException extends RuntimeException {
  public JwtEmailNotFoundException(String message) {
    super(message);
  }
}
