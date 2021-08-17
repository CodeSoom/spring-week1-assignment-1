package com.codesoom.assignment.models;

public class HttpResponse {
    private int httpStatusCode;
    private String content;
    private int length;

    public HttpResponse() {

    }

    public HttpResponse(int httpStatusCode, String content, int length) {
        this.httpStatusCode = httpStatusCode;
        this.content = content;
        this.length = length;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
