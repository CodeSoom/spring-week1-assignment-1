package com.codesoom.assignment.utils;

/**
 * Reference: HTTP/1.1 # Status Code and Reason Phrase
 * https://datatracker.ietf.org/doc/html/rfc2616#section-6.1.1
 */
public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final Integer statusCode;

    HttpStatus(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer statusCode() { return statusCode; }
}
