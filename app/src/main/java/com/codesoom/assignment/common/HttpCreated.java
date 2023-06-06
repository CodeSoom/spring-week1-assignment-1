package com.codesoom.assignment.common;

public class HttpCreated implements HttpStatus {

    private static final int HTTP_CREATED_CODE = 201;

    private final String content;

    public HttpCreated(final String content) {
        this.content = content;
    }

    public int getCode() {
        return HTTP_CREATED_CODE;
    }

    public String getContent() {
        return content;
    }

}
