package com.sitionix.stsssox.domain.exception;

public class AuthenticationRequiredException extends RuntimeException {

    public AuthenticationRequiredException(final String message) {
        super(message);
    }
}
