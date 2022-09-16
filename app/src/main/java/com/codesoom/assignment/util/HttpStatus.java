package com.codesoom.assignment.util;

public enum HttpStatus {
    HTTP_OK(200, "Created"),

    HTTP_CREATED(201, "Accepted"),

    HTTP_NO_CONTENT(204, "Reset Content"),

    HTTP_BAD_REQUEST(400, "Unauthorized"),

    HTTP_NOT_FOUND(404, "Method Not Allowed");

    private final int statusCode;
    private final String description;

    HttpStatus(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
