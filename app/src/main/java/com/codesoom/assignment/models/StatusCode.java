package com.codesoom.assignment.models;

public enum StatusCode {
    OK(200), Created(201), NO_CONTENT(204), NOT_FOUND(404);

    private int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getValue() {
        return statusCode;
    }
}
