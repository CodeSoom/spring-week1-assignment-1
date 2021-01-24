package com.codesoom.assignment.models;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);
    private int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
