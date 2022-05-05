package com.codesoom.assignment;

public class HttpResponse {
    private final int statusCode;
    private final String content;

    HttpResponse(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("{ statusCode = %s, content = %s }", statusCode, content);
    }
}
