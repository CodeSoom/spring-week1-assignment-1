package com.codesoom.assignment.http;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private int status;

    HttpStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
