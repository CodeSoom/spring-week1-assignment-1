package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatus;

public class Response {
    private final String content;
    private final HttpStatus httpStatus;

    private static final String EMPTY_CONTENT = "";

    public Response() {
        content = EMPTY_CONTENT;
        httpStatus = HttpStatus.InternalServerError;
    }

    public Response(HttpStatus httpStatus) {
        content = EMPTY_CONTENT;
        this.httpStatus = httpStatus;
    }

    public Response(String content, HttpStatus httpStatus) {
        this.content = content;
        this.httpStatus = httpStatus;
    }

    public byte[] getContentBytes() {
        return content.getBytes();
    }

    public Integer getStatusCode() {
        return httpStatus.code();
    }
}
