package com.codesoom.assignment.network;

public enum HttpResponseCode {
    OK(200),
    Created(201),
    NoContent(204),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500);

    public int getRawValue() {
        return rawValue;
    }

    private final int rawValue;

    HttpResponseCode(int rawValue) {
        this.rawValue = rawValue;
    }
}
