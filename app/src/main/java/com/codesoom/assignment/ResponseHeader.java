package com.codesoom.assignment;

public class ResponseHeader {
    private int statusCode;
    private String content;

    ResponseHeader() {
    }

    ResponseHeader(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {

        return String.format("{ statusCode = %s, content = %s }", statusCode, content);
    }
}
