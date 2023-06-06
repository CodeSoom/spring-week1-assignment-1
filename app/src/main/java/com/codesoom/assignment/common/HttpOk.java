package com.codesoom.assignment.common;

public class HttpOk implements HttpStatus {

    private static final int HTTP_OK_CODE = 200;

    private final String content;

    public HttpOk(final String content) {
        this.content = content;
    }

    public int getCode() {
        return HTTP_OK_CODE;
    }

    public String getContent() {
        return content;
    }

}
