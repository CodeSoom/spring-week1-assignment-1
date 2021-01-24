package com.codesoom.assignment;

public enum HttpStatusCode {
    OK(200), CREATED(201), NOT_FOUND(404);

    private final int statusCode;

    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return statusCode;
    }
}
