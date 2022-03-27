package com.codesoom.assignment.http.exception;

public class HttpBadRequestException extends IllegalArgumentException {

    private static final String MESSAGE = "Http Bad Request";

    public HttpBadRequestException() {
        super(MESSAGE);
    }
}
