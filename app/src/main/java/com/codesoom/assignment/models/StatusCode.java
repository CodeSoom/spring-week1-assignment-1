package com.codesoom.assignment.models;

public enum StatusCode {
    OK(200),
    CREATED(201);

    private int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
