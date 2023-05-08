package com.codesoom.assignment.common;

public class HttpNotFound implements HttpStatus {

    private static final int HTTP_NOT_FOUND_CODE = 200;

    private final String content;

    public HttpNotFound(final String content) {
        this.content = content;
    }

    public int getCode() {
        return HTTP_NOT_FOUND_CODE;
    }

    public String getContent() {
        return content;
    }

}
