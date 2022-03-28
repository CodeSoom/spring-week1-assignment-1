package com.codesoom.assignment.exception;

public class NoDefaultConstructorException extends RuntimeException{
    public NoDefaultConstructorException(String message) {
        super(message);
    }
}
