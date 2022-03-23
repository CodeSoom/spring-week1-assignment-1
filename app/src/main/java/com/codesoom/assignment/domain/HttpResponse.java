package com.codesoom.assignment.domain;

public class HttpResponse {
    private final String content;
    private final int statusCode;

    public HttpResponse(String content, int statusCode) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
