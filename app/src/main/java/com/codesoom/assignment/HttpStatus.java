package com.codesoom.assignment;

public enum HttpStatus {
    OK(200),
    Created(201),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500);

    private int httpStatus;

    HttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
