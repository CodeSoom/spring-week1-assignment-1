package com.codesoom.assignment;

/**
 * http status codes
 */
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

    private int rawValue;

    HttpStatusCode(int rawValue) {
        this.rawValue = rawValue;
    }

    public int getRawValue() {
        return this.rawValue;
    }
}