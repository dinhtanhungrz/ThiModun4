package com.codegym.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Sets the default HTTP status code
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}