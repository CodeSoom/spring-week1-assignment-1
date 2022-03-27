package com.codesoom.assignment.http.exception;

public class HttpNotFoundException extends IllegalStateException {

    private static final String MESSAGE = "Http Not Found";

    public HttpNotFoundException() {
        super(MESSAGE);
    }
}
