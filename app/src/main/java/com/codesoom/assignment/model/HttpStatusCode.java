package com.codesoom.assignment.model;

public enum HttpStatusCode {

    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int statusCode;
    HttpStatusCode(int statusCode) { this.statusCode = statusCode; }
    public int getStatusCode() { return statusCode; }


}
